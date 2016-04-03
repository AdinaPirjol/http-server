package httpserver.Model;

import httpserver.Exception.HttpException;
import static httpserver.Model.HttpRequest.DELETE_METHOD;
import static httpserver.Model.HttpRequest.PATCH_METHOD;
import static httpserver.Model.HttpRequest.POST_METHOD;
import static httpserver.Model.HttpRequest.PUT_METHOD;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Map;
import java.net.Socket;
import java.util.HashMap;

public class HttpRequestBuilder {
    String hostAddress;
    HttpRequest request;
    StringBuilder requestBuilder;
    BufferedReader input;
    
    public HttpRequestBuilder(Socket connection) 
            throws IOException, SocketException, HttpException {
        this.hostAddress = connection.getLocalAddress().getHostAddress();
        this.request = new HttpRequest();
        this.requestBuilder = new StringBuilder();
        this.input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }
    
    public HttpRequest parseRequest() throws HttpException, IOException
    {
        this.initSetters()
            .initHeader()
            .initRequestData()
            .initRequest();
        
        return this.request;
    }
    
    // [request type] [path] [protocol]
    private HttpRequestBuilder initSetters() 
            throws HttpException, IOException {
        
        String line = this.input.readLine();
        
        while (line.isEmpty()) {
            line = input.readLine();
        }
        
        String[] split = line.trim().split(" ");
        if (split.length != 3) {
            throw new HttpException("Request line has a number of spaces other than 3.");
        }

        // Set the request type
        request.setMethod(split[0].toUpperCase());

        // set the path
        this.setFullPath(split[1]);

        // set the protocol type
        request.setRequestProtocol(split[2]);
        
        request.setHostAddress(this.hostAddress);
        
        requestBuilder.append(line);
        requestBuilder.append("\n");
        
        return this;
    }
    
    private HttpRequestBuilder initHeader() throws IOException, HttpException {
        // headers => "key: value"
        for (String line = input.readLine(); line != null && !line.isEmpty(); line = input.readLine()) {
//            String line = input.readLine();
            this.requestBuilder.append(line);
            this.requestBuilder.append("\n");

            line = line.trim();
            String[] split = line.split(": ");
            
            if (split.length == 1) {
                throw new HttpException("No key value pair in \n\t" + line);
            }
            
            String key = split[0];
            String value = split[1];

            for (int i = 2; i < split.length; i++) {
                value += ": " + split[i];
            }

            this.request.getHeaders().put(key, value);
        }
        
        return this;
    }
    
    private HttpRequestBuilder initRequestData() throws IOException {
        // request data
        if ((this.request.getMethod().equals(POST_METHOD) 
                || this.request.getMethod().equals(DELETE_METHOD) 
                || this.request.getMethod().equals(PUT_METHOD) 
                || this.request.getMethod().equals(PATCH_METHOD)) 
                && this.request.getHeaders().containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(request.getHeaders().get("Content-Length"));
            StringBuilder b = new StringBuilder();

            for (int i = 0; i < contentLength; i++) {
                b.append((char)this.input.read());
            }

            this.requestBuilder.append(b.toString());

            this.request.setRequestBody(b.toString());

            String[] data = this.request.getRequestBody().trim().split("&");
            if (data.length <= 1) {
                return this;
            }
            System.out.println(data.length);
            Map<String,String> inputData = this.parseInputData(data);
            this.request.getParameterBag().putAll(inputData);
        }
        
        return this;
    }
    
    private HttpRequestBuilder initRequest()
    {
        this.request.setHttpRequest(this.requestBuilder.toString());
        
        return this;
    }
    
    private Map<String, String> parseInputData(String[] data) 
    {
        Map<String, String> inputData = new HashMap<>();
        for (String str : data) {
            String[] split = str.split("=");
            System.out.println(split[0]);
            String value = split[1];
            String key = split[0];
            inputData.put(key, value);
        }

        return inputData;
    }
    
    public void setFullPath(String path) {
        this.request.setPath(path);
        this.setSplitPath(path);
    }
    
    public void setSplitPath(String fullPath) {
        String[] split = fullPath.substring(1).split("/");
        
        for (String segment : split) {
            if (segment.isEmpty()) {
                continue;
            }

            this.request.getSplitPath().add(segment);
        }

        if (this.request.getSplitPath().isEmpty()) {
            return;
        }
        
        int len = request.getSplitPath().size() - 1;
        String last = request.getSplitPath().get(len);
        if (last.indexOf('?') != -1) {
            request.getSplitPath().set(len, last.substring(0, last.indexOf('?')));
            String[] getData = last.substring(last.indexOf('?') + 1, last.length()-1).split("&");
            System.out.println("here"+getData.length);
//            if (getData.length <= 1) {
//                return;
//            }
            request.getParameterBag().putAll(this.parseInputData(getData));
        }
    }
}
