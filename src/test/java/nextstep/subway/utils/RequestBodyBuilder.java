package nextstep.subway.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 요청 본문을 생성하는 빌더
 */
public class RequestBodyBuilder {

	private final Map<String, String> body;

	private RequestBodyBuilder() {
		body = new HashMap<>();
	}

	private RequestBodyBuilder(Map<String, String> body) {
		this.body = body;
	}

	public static RequestBodyBuilder builder() {
		return new RequestBodyBuilder();
	}

	public RequestBodyBuilder put(String fieldName, int fieldValue) {
		return put(fieldName, String.valueOf(fieldValue));
	}

	public RequestBodyBuilder put(String fieldName, long fieldValue) {
		return put(fieldName, String.valueOf(fieldValue));
	}

	public RequestBodyBuilder put(String fieldName, String fieldValue) {
		final Map<String, String> body = new HashMap<>(this.body);
		body.put(fieldName, fieldValue);
		return new RequestBodyBuilder(body);
	}

	public Map<String, String> build() {
		return body;
	}

}
