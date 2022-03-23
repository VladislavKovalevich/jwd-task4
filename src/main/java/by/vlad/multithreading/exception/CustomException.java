package by.vlad.multithreading.exception;

public class CustomException extends Exception {
    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Exception cause) {
        super(message, cause);
    }

    public CustomException(Exception cause) {
        super(cause);
    }
}
