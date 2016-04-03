package httpserver.HandlerModel;

import httpserver.HandlerModel.HttpHandler;
import httpserver.Exception.HttpException;
import httpserver.Filter.Route;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;

public class MessageHandler extends GenericHandler {

    private String body;
    private int code;

    public MessageHandler(int code, String message) {
        this.body = message;
        this.code = code;
    }

    public MessageHandler(String message) {
        this(200, message);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        //
    }
}