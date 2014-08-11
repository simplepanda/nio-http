nio-http
========

A non-blocking I/O HTTP server library for Java 7.

### Requirements

* Java 7
* Eclipse for build environment

### Can I help?

Sure! Please place any pull requests against the devel branch.

### Current Status

Functional for basic text requests via a StringRequestHandler (see example below) - using GET and HEAD requests over HTTP/1.0 and HTTP/1.1.
GET/HEAD/POST requests working, as do parameter value and header value passing via GET.

File stream responses are not yet implemented.

### Issues

There are lots of enhancements underway. See https://github.com/simplepanda/nio-http/issues for specifics.

### Example Server

Demo code is up and running at http://sky.codeandstrings.com and demonstrates the server available and handling requests in it's current state.

### How To Use

To build the library, either checout into Eclipse and package your own JAR file (or use the code as-is in your project), or build from the command line using Apache Ant:

	# ant package
	# file nio-http.jar 
	nio-http.jar: Java archive data (JAR)

The server library is designed to be very easy to integrate with your project. At the simplest level, simply create an instance of a RequestHandler object (in this case, a StringRequestHandler to return simple String answers) and pass it to the server as a request handler.

	StringRequestHandler handler = new StringRequestHandler() {
	
		@Override
		public String getContentType() {
			return "text/html";
		}

		@Override
		public String handleRequest(Request request) {				
			return "Thanks for connecting from " + request.getRemoteAddr();				
		}
			
	};	
		
	Server server = new Server();
	server.setRequestHandler(handler);
		
	server.run();
