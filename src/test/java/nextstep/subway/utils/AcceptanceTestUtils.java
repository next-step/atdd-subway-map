package nextstep.subway.utils;

import static java.lang.String.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AcceptanceTestUtils {

	/**
	 * 응답 본문에서 아이디 필드에 해당하는 아이디를 추출합니다.
	 * @param response 응답 본문
	 * @param fieldName 아이디 필드 이름
	 * @return 아이디
	 */
	public static long extractId(ExtractableResponse<Response> response, String fieldName) {
		return response.jsonPath().getLong(fieldName);
	}

	/**
	 * Path Variable 형태로 요청 URL 을 생성합니다.
	 * @param id
	 * @return
	 */
	public static String createRequestPathWithVariable(String requestBasePath, long id) {
		return format("%s/%s", requestBasePath, id);
	}
}
