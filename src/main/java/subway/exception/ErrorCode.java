package subway.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	HttpStatus getStatusCode();

	String getMessage();

	String getCode();
}
