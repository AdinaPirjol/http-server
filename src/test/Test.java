/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import httpserver.Filter.Route;
import httpserver.HttpServer;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.util.Map;

/**
 *
 * @author nnao9_000
 */
public class Test {
    protected static String headerString(Map<String, String> headers) {
        StringBuilder b = new StringBuilder();
        for (String key: headers.keySet()) {
            b.append("\n");
            b.append(key);
            b.append(":\t");
            b.append(headers.get(key));
            b.append("\n");
        }

        return b.toString();
    }
    
    public static void main(String[] args) 
    {
        HttpServer server = new HttpServer();
        (new Thread(server)).start();
        
        server.addRoute("GET", new Route("/showHeaders") {
            @Override public void handle(HttpRequest request, HttpResponse response) {
                response.setBody("Headers:" + headerString(request.getHeaders()));
            }
        });

        server.addRoute("GET", new Route("/hello") {
            @Override public void handle(HttpRequest request, HttpResponse response) {
                response.setBody("Hello world!");
            }
        });
        
        server.addRoute("POST", new Route("/welcome") {
            @Override public void handle(HttpRequest request, HttpResponse response) {
                response.setBody("Have a nice day, " + request.getParameterBag().get("firstName") + " " + request.getParameterBag().get("lastName") + "!");
            }
        });
    }
}
