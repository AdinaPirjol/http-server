/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver.Filter;

import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author nnao9_000
 */
public interface RouteFilterInterface {
    public abstract Map<String, ArrayList<Route>> getRoutes(HttpRequest request, Map<String, ArrayList<Route>> routes);
}
