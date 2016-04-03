
package httpserver.HandlerModel;

import httpserver.Filter.Route;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;

public abstract class GenericHandler {
    protected Route route;

    public abstract void handle(HttpRequest request, HttpResponse response);
}
