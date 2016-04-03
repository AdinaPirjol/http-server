package httpserver;

import gui.Observer;
import httpserver.Handlers.Handler;
import gui.ServerGUI;
import gui.Subject;
import httpserver.Exception.HttpException;
import httpserver.Filter.HttpRouter;
import httpserver.Filter.Route;
import httpserver.Handlers.HandlerProxy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer implements Runnable,Subject {

    public static final int defaultPort = 8000;
    
    private HandlerProxy handler;
    private ServerSocket socket = null;
    private List<Observer> observers;
    private boolean running = true;
    public int port;
    private Logger logger = Logger.getLogger("java-httpserver");
    
    public HttpServer() {
        this(defaultPort);
    }
    
    public HttpServer(String name, String version) {
        this(defaultPort, name, version);
    }
    
    public HttpServer(int port, String name, String version) {
        this(port);
    }
    
    public HttpServer(int port) {
        this.setPort(port);
        this.observers = new ArrayList<>();
    }
    
    @Override
    public void run() {
        ServerGUI serverGUI = new ServerGUI();
        
        try {
            serverGUI.setVisible(true);
            this.registerObserver(serverGUI);
            
            this.running = true;
            this.socket = new ServerSocket();
            
            this.notifyObservers("localhost:" + getPort());
            this.logger.info("localhost:" + getPort());
            
            this.socket.setReuseAddress(true);
            this.socket.bind(new InetSocketAddress(getPort()));

            while (running) {
                Socket connection = null;
                try {
                    connection = socket.accept();
                    
                    this.handler = new HandlerProxy(connection);
                    
                    logger.info(String.format("Http request from %s:%d", connection.getInetAddress(), connection.getPort()));
                    this.notifyObservers(String.format("Http request from %s:%d", connection.getInetAddress(), connection.getPort()));
                    
                    java.util.Date date= new java.util.Date();
                    this.notifyObservers(new Timestamp(date.getTime()).toString());
                    this.notifyObservers("Request: " + this.handler.getRequest().toString());
                    this.notifyObservers("Response: " + this.handler.getResponse().toString() + "\n");
                    
                } catch (SocketException e) {
                    logger.log(Level.WARNING, "Client broke connection early!", e);
                    this.notifyObservers("Client broke connection early! " + e.getMessage());
                } catch (IOException e) {
                    logger.log(Level.WARNING, "IOException. Probably an HttpRequest issue.", e);
                    this.notifyObservers("IOException. Probably an HttpRequest issue. " + e.getMessage());
                } catch (HttpException e) {
                    logger.log(Level.SEVERE, "Server exception", e);
                    this.notifyObservers("Server exception! " + e.getMessage());
                    break;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Generic Exception!", e);
                    this.notifyObservers("Generic Exception! " + e.getMessage());
                    break;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Something bad happened", e);
            this.notifyObservers("Something bad happened " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Couldnt close connection", e);
                this.notifyObservers("Couldnt close connection " + e.getMessage());
            }
        }
        logger.info("Server shutting down.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        serverGUI.addNotification("Server shutting down.");
        serverGUI.stop();
        
        this.stop();
    }
    
    @Override
    public void registerObserver(Observer observer)
    {
        this.observers.add(observer);
    }
    
    @Override
    public void unregisterObserver(Observer observer)
    {
        this.observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(String message)
    {
        for(Observer o:observers) {
            o.addNotification(message);
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public HandlerProxy getHandler() {
        return handler;
    }

    public void setHandler(HandlerProxy handler) {
        this.handler = handler;
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public void setSocket(ServerSocket socket) {
        this.socket = socket;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void stop() {
        running = false;

        try {
            socket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing socket.", e);
        }
    }
    
    public void addRoute(String method, Route route) {
        HttpRouter.getInstance().addRoute(method, route);
    }
}