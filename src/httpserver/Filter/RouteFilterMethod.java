/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver.Filter;

import static httpserver.Filter.HttpRouter.routes;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nnao9_000
 */
public class RouteFilterMethod implements RouteFilterInterface{

    @Override
    public  Map<String, ArrayList<Route>> getRoutes(HttpRequest request, Map<String, ArrayList<Route>> routes) {
        Map<String, ArrayList<Route>> r = new HashMap<>();
        
        String method = request.getMethod().toUpperCase();
        
        if (routes.containsKey(method)) {
            r.put(method, routes.get(method));
        }
        
        return r;
    }
}
