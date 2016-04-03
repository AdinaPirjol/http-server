package httpserver.Handlers;

import httpserver.Exception.HttpException;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import httpserver.Filter.HttpRouter;
import httpserver.Filter.Route;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


public class HandlerProxy extends HandlerProxyInterface{
    private Handler handler;

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }
    
    public HandlerProxy(Socket connection) throws IOException, SocketException, HttpException 
    {
        super();
        this.handler = new Handler(connection);
        this.request = this.handler.getRequest();
        this.response = this.handler.getResponse();
    }
    
    @Override
    public void handle() {
        this.handler.handle();
    }
}
