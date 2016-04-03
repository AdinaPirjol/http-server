/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver.Filter;

import static httpserver.Filter.HttpRouter.defaultRoutes;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nnao9_000
 */
public class RouteFilterPath implements RouteFilterInterface {
    RouteFilterMethod filter = new RouteFilterMethod();

    @Override
    public Map<String, ArrayList<Route>> getRoutes(HttpRequest request, Map<String, ArrayList<Route>> routes) {
        routes = filter.getRoutes(request, routes);
        
        if (routes.keySet().isEmpty()) { 
            return routes;
        }
        
        Map<String, ArrayList<Route>> routes2 = new HashMap<>();
        
        int bestFit = 0;
        String method = request.getMethod();
        routes2.put(method, new ArrayList<>());
        for (Route r : routes.get(method)) {
            if (r.equals(request.getSplitPath())) {
                routes2.get(method).add(r);
                break;
            }

            int testScore = r.testPath(request.getSplitPath());
            if (testScore > bestFit) {
                bestFit = testScore;
                routes2.get(method).add(r);
            }
        }
        
        return routes2;
    }
}
