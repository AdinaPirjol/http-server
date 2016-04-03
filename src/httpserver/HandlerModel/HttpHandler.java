package httpserver.HandlerModel;

import httpserver.Exception.HttpException;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import httpserver.Filter.Route;
import httpserver.Handlers.Handler;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class HttpHandler extends GenericHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        this.route.invoke(request, response);
    }
}