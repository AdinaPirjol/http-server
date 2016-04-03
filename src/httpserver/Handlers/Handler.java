/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver.Handlers;

import httpserver.Exception.HttpException;
import httpserver.Model.HttpRequest;
import httpserver.Model.HttpRequestBuilder;
import httpserver.Model.HttpResponse;
import httpserver.HandlerModel.FactoryHandler;
import httpserver.HandlerModel.GenericHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Handler extends HandlerProxyInterface {
    
    public static final String GET_REQUEST_TYPE = "GET";
    public static final String POST_REQUEST_TYPE = "POST";
    public static final String HEAD_REQUEST_TYPE = "HEAD";
    public static final String DELETE_REQUEST_TYPE = "DELETE";
    public static final String PUT_REQUEST_TYPE = "PUT";
    
    protected HttpRequestBuilder httpRequestBuilder;
    protected Socket connection;
    protected DataOutputStream writer;
    
    public Handler(Socket connection) 
            throws IOException, SocketException, HttpException {
        connection.setKeepAlive(true);
        this.connection = connection;
        this.writer = new DataOutputStream(connection.getOutputStream());;
        this.httpRequestBuilder = new HttpRequestBuilder(connection);
        
        this.handle();
    }
    
    @Override
    public final void handle()
    {
        if (this.connection.isClosed()) {
            System.out.println("Socket is closed...");
        }

        try {
            this.parseRequest();
            this.createResponse();
            this.determineHandler();
            this.respond();
        } catch (IOException | HttpException e) {
            e.printStackTrace();
        }
    }
    
    public void parseRequest() throws HttpException, IOException
    {
        this.request = this.httpRequestBuilder.parseRequest();
    }
    
    public void createResponse() throws IOException, HttpException
    {
        this.response = new HttpResponse();
    }
    
    public void determineHandler() {
        GenericHandler handler = FactoryHandler.determineHandler(request);
        handler.handle(request, response);
    }
    
    public void respond() {
        try {
            if (connection == null || this.connection.isClosed()) {
                throw new HttpException("Socket is null");
            }

            System.out.println(response.toString());
            
            // headers
            this.writeLine("HTTP/1.1 " + response.getResponseCodeMessage(response.getCode()));
            this.writeLine("Server: localhost");
            this.writeLine("Content-Type: " + response.getContentType());
            this.writeLine("Connection: close");
            this.writeLine("Content-Size: " + response.getBody().length);
            
            if (!response.getHeaders().isEmpty()) {
                StringBuilder b = new StringBuilder();
                for (String key : response.getHeaders().keySet()) {
                    b.append(key);
                    b.append(": ");
                    b.append(response.getHeaders().get(key));
                    b.append("\n");
                }
                this.writeLine(b.toString());
            }
            
            this.writer.write(response.getBody());
        } catch (Exception e) {
            // log to gui
            System.err.println("error");
            e.printStackTrace();
        } finally {
            try {
                this.writer.close();
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected void writeLine(String line) throws IOException {
        this.writer.writeBytes(line + "\n");
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }
}
