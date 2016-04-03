package httpserver.Handlers;

import httpserver.Filter.HttpRouter;
import httpserver.Filter.Route;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.io.IOException;


public abstract class HandlerProxyInterface {
    protected HttpRequest request;
    protected HttpResponse response;
    
    public HandlerProxyInterface() throws IOException
    {
        this.request = new HttpRequest();
        this.response = new HttpResponse();
    }
    
    public abstract void handle();
}
