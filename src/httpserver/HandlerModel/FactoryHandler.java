package httpserver.HandlerModel;

import httpserver.Filter.HttpRouter;
import httpserver.Filter.Route;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

public class FactoryHandler {
    public static GenericHandler determineHandler(HttpRequest request)
    {
        Map<String, ArrayList<Route>> routes = HttpRouter.getInstance().getRoutes(request, HttpRouter.routes);
        
        if (!routes.keySet().contains(request.getMethod())) {
            return new ErrorHandler("No route for request method " + request.getMethod());
        }
        
        ArrayList<Route> r = routes.get(request.getMethod());
        
        if (r.isEmpty()) {
            return new ErrorHandler("No matching route.");
        }
        
        Route route = r.get(r.size()-1);
        
        if (route == null) {
            return new ErrorHandler();
        }
        
        HttpHandler handler = new HttpHandler();
        handler.route = route;
        
        return handler;
    }
}
