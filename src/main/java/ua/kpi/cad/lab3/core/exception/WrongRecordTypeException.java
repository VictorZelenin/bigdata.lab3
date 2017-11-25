package ua.kpi.cad.lab3.core.exception;

public class WrongRecordTypeException extends RuntimeException {
    public WrongRecordTypeException(String message) {
        super(message);
    }
}
