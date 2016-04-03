package httpserver.Filter;

import httpserver.Filter.RouteFilterInterface;
import httpserver.Filter.RouteFilterPath;
import httpserver.Model.HttpRequest;
import httpserver.HandlerModel.HttpHandler;
import httpserver.Model.HttpResponse;
import httpserver.Filter.Route;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// route handler
public class HttpRouter implements RouteFilterInterface {
    private static HttpRouter instance;
            
    public static final List<String> DEFAULT_PATH = new ArrayList<>();

    public static HashMap<String, ArrayList<Route>> routes = new HashMap<>();
    public static HashMap<String, Route> defaultRoutes = new HashMap<>();

    private HttpRouter() {
        routes = new HashMap<>();
        defaultRoutes = new HashMap<>();
    }
    
    public static HttpRouter getInstance()
    {
        if (instance == null) {
            instance = new HttpRouter();
        }
        
        return instance;
    }
    
    // add routes to server
    public void get(Route route) {
        addRoute(HttpRequest.GET_METHOD, route);
    }

    public void post(Route route) {
        addRoute(HttpRequest.POST_METHOD, route);
    }

    public void delete(Route route) {
        addRoute(HttpRequest.DELETE_METHOD, route);
    }

    public void addRoute(String httpMethod, Route route) {
        httpMethod = httpMethod.toUpperCase();

        if (!routes.containsKey(httpMethod)) {
            routes.put(httpMethod, new ArrayList<Route>());
        }

        routes.get(httpMethod).add(route);

        if (route.equals(DEFAULT_PATH)) {
            defaultRoutes.put(httpMethod, route);
        }
    }

    @Override
    public Map<String, ArrayList<Route>> getRoutes(HttpRequest request, Map<String, ArrayList<Route>> routes) {
        return new RouteFilterPath().getRoutes(request, routes);
    }
}