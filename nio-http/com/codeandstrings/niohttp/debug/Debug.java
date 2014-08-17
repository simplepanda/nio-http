package com.codeandstrings.niohttp.debug;

import com.codeandstrings.niohttp.Server;
import com.codeandstrings.niohttp.handlers.impl.StreamingFileSystemHandler;

public class Debug {

	public static void main(String args[]) {

		Server server = new Server();

        try {
            server.addRequestHandler("/pictures/.*", DebugPictureHandler.class);
            server.addRequestHandler("/version", DebugVersionHandler.class);
            server.addRequestHandler(".*", DebugRootHandler.class);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        server.run();
	}

}
