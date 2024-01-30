package subway.line;

public class LineNotFoundException extends Exception{
    public LineNotFoundException() {
        super();
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
