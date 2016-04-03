package httpserver.Model;

import httpserver.Exception.HttpException;
import httpserver.HandlerModel.HttpHandler;
import httpserver.HandlerModel.ErrorHandler;
import httpserver.Filter.HttpRouter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    public static final String HEAD_METHOD = "HEAD";
    public static final String DELETE_METHOD = "DELETE";
    public static final String PUT_METHOD = "PUT";
    public static final String PATCH_METHOD = "PATCH";
    
    private String hostAddress;
    private String httpRequest;// entire request
    private String requestBody;// request body
    private String method;
    private String requestProtocol;
    private Map<String, String> headers = new HashMap<>();
    private List<String> splitPath = new ArrayList<>();
    private String path;
    private Map<String, String> parameterBag = new HashMap<>();

    public HttpRequest(){}

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }
    
    public boolean isMethod(String method) {
        method = method.toUpperCase();
        return this.method.equals(method);
    }
    
    public String getFullPath() {
        return this.path;
    }
    
    public void setSplitPath(List<String> path) {
        this.splitPath = path;
    }
    
    public List<String> getSplitPath() {
        return this.splitPath;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setParams(Map<String, String> data) {
        this.parameterBag = data;
    }
    public Map<String, String> getParams() {
        return this.parameterBag;
    }
    public void mergeParams(Map<String, String> data) {
        this.parameterBag.putAll(data);
    }
    public String getParam(String key) {
        return this.parameterBag.get(key);
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return this.method;
    }

    public void setRequestProtocol(String requestProtocol) {
        this.requestProtocol = requestProtocol;
    }
    public String getRequestProtocol() {
        return this.requestProtocol;
    }
    
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestBody() {
        return this.requestBody;
    }
    
     public String getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(String httpRequest) {
        this.httpRequest = httpRequest;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getParameterBag() {
        return parameterBag;
    }

    public void setParameterBag(Map<String, String> parameterBag) {
        this.parameterBag = parameterBag;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpRequest from ");
        builder.append(this.hostAddress);
        builder.append("\n\t");
        builder.append("Request headers: ");
        builder.append(this.getHeaders().toString());
        builder.append("\n\t");
        builder.append("Request line: ");
        builder.append(this.getMethod() + " " + this.getRequestProtocol() + " " + this.getFullPath());
        builder.append("\n\t\t");
        builder.append("Request body ");
        builder.append(this.getRequestBody());

        return builder.toString();
    }
}