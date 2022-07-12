package nextstep.subway.applicaion.common;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {

	private LocalDateTime timestamp = LocalDateTime.now();

	private String message;

	private HttpStatus status;

	public ErrorResponse(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}
}
