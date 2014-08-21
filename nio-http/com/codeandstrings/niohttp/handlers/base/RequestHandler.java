package com.codeandstrings.niohttp.handlers.base;

import com.codeandstrings.niohttp.request.Request;
import com.codeandstrings.niohttp.response.ResponseContent;
import com.codeandstrings.niohttp.wire.RequestReader;
import com.codeandstrings.niohttp.wire.RequestWriter;
import com.codeandstrings.niohttp.wire.ResponseContentReader;
import com.codeandstrings.niohttp.wire.ResponseContentWriter;

import java.io.*;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;

public abstract class RequestHandler implements Runnable {

    private Pipe aPipe;
    private Pipe bPipe;

    private Pipe.SourceChannel engineSource;
    private Pipe.SourceChannel handlerSource;
    private Pipe.SinkChannel engineSink;
    private Pipe.SinkChannel handlerSink;

    private RequestReader requestReader;
    private RequestWriter requestWriter;
    private ResponseContentReader responseContentReader;
    private ResponseContentWriter responseContentWriter;

    private Thread handlerThread;

    public RequestHandler()  {

        try {
            this.aPipe = Pipe.open();
            this.bPipe = Pipe.open();

            this.engineSource = aPipe.source();
            this.handlerSink = aPipe.sink();

            this.handlerSource = bPipe.source();
            this.engineSink = bPipe.sink();

            this.handlerSource.configureBlocking(false);
            this.engineSource.configureBlocking(false);
            this.handlerSink.configureBlocking(false);
            this.engineSink.configureBlocking(false);

            this.requestReader = new RequestReader(this.handlerSource);
            this.requestWriter = new RequestWriter(this.engineSink);
            this.responseContentReader = new ResponseContentReader(this.engineSource);
            this.responseContentWriter = new ResponseContentWriter(this.handlerSink);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void startThread() {
        this.handlerThread = new Thread(this);
        this.handlerThread.setName("NIO-HTTP Handler Thread: " + this.getHandlerDescription());
        this.handlerThread.start();
    }

    public Request executeRequestReadEvent() throws IOException, ClassNotFoundException {
        return this.requestReader.readRequestFromChannel();
    }

    public boolean executeRequestWriteEvent() throws IOException {
        return this.requestWriter.executeRequestWriteEvent();
    }

    public void sendRequest(Request r) {
        this.requestWriter.sendRequest(r);
    }

    public void sendBufferContainer(ResponseContent b) {
        this.responseContentWriter.sendBufferContainer(b);
    }

    public boolean executeBufferWriteEvent() throws IOException {
        return this.responseContentWriter.executeBufferWriteEvent();
    }

    public ResponseContent executeBufferReadEvent() throws IOException, ClassNotFoundException {
        return this.responseContentReader.readBufferFromChannel();
    }

    public Pipe.SourceChannel getEngineSource() {
        return engineSource;
    }

    public Pipe.SinkChannel getEngineSink() {
        return engineSink;
    }

    @Override
    public void run() {
        this.listenForRequests();
    }

    protected abstract void listenForRequests();

    protected abstract String getHandlerDescription();

    protected SelectableChannel getHandlerReadChannel() {
        return this.handlerSource;
    }

    protected SelectableChannel getHandlerWriteChannel() {
        return this.handlerSink;
    }

    public int getConcurrency() {
        return 1;
    }

}