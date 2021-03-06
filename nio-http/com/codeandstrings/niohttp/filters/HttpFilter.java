package com.codeandstrings.niohttp.filters;

import com.codeandstrings.niohttp.request.Request;
import com.codeandstrings.niohttp.response.Response;
import com.codeandstrings.niohttp.response.ResponseMessage;

public abstract class HttpFilter {

    public abstract boolean shouldFilter(Request request, Response response);
    public abstract void filter(Request request, ResponseMessage message);
    public abstract void cleanup(long sessionId);

}
