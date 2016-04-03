package httpserver.Exception;

public class HttpException extends Exception {
    public HttpException(String message, Exception e) {
        super(message, e);
    }
    
    public HttpException(String message) {
        super(message);
    }
}