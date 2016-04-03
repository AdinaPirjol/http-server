package httpserver.HandlerModel;

import httpserver.Exception.HttpException;
import httpserver.Filter.Route;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.util.ArrayList;

public class ErrorHandler extends GenericHandler{
    private int code;
    private String message;

    public ErrorHandler() {
        this.code = 500;
    }
    
    public ErrorHandler(String message) {
        super();
        this.message = message;
    }
    
    public ErrorHandler(String message, int code) {
        this.code = code;
        this.message = message;
    }
    
    public ErrorHandler(HttpException e) {
        super();  
        this.message = e.getMessage();
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        response.message(this.code, this.message);
    }
}