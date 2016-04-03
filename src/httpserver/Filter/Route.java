package httpserver.Filter;

import httpserver.Model.HttpRequest;
import httpserver.Model.HttpResponse;
import java.util.List;
import java.util.ArrayList;

public abstract class Route {
    private List<String> routePath = new ArrayList<>();

    public Route(String path) {
        String[] pathSegments = path.trim().split("/");

        for (int i = 0; i < pathSegments.length; i++) {
            if (pathSegments[i].isEmpty() || pathSegments[i].equals("")) {
                continue; 
            }

            this.routePath.add(pathSegments[i]);
        }
    }

    public void invoke(HttpRequest request, HttpResponse response) {
        try {
            this.handle(request, response);
        } catch (Exception e) {
            response.message(500, e.getMessage());
        }
    }

    public int testPath(List<String> path) {
        int score = 0;
        
        if (path.size() != this.routePath.size()) {
            return 0;
        } else {
            score = 1;
        }
        
        for (int i = 0; i < this.routePath.size(); i++) {
            if (this.routePath.get(i).equals(path.get(i))) {
                score++;
            }
        }
        
        return score;
    }

    public abstract void handle(HttpRequest request, HttpResponse response);
}