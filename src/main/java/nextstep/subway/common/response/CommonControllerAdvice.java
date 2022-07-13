package nextstep.subway.common.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.common.exception.BusinessException;

@RestControllerAdvice
public class CommonControllerAdvice {

	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity<Map<String, Object>> badArgumentException(BusinessException e) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("errorCode", e.getErrorCode());
		return ResponseEntity.ok().body(responseBody);
	}

}
