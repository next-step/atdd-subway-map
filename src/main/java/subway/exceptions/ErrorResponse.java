package subway.exceptions;

import java.util.List;
import org.apache.catalina.connector.Response;

public class ErrorResponse {

    private String message;
    private int status;
    private String code;


    public static ErrorResponse of(ErrorCode code, Exception error) {
        final ErrorResponse response = new ErrorResponse();

        response.message = error.getMessage();
        response.status = code.getStatus();
        response.code = code.getCode();

        return response;
    }

    public static ErrorResponse of(ErrorCode code) {
        final ErrorResponse response = new ErrorResponse();

        response.message = code.getMessage();
        response.status = code.getStatus();
        response.code = code.getCode();

        return response;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
