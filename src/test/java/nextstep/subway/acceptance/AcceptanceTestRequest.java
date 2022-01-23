package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.Map;

import static io.restassured.http.Method.*;
import static nextstep.subway.utils.AcceptanceTestUtils.createRequestPathWithVariable;
import static nextstep.subway.utils.AcceptanceTestUtils.extractId;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AcceptanceTestRequest {

	final String requestBasePath;
	final String idFieldKey;

	protected AcceptanceTestRequest(String requestBasePath, String idFieldKey) {
		this.requestBasePath = requestBasePath;
		this.idFieldKey = idFieldKey;
	}

	public ExtractableResponse<Response> 단건_조회_요청(ExtractableResponse<Response> 생성_응답) {
		return 단건_조회_요청(아이디_추출(생성_응답));
	}

	public ExtractableResponse<Response> 목록_조회_요청() {
		return request(GET, requestBasePath, Collections.emptyMap());
	}

	public ExtractableResponse<Response> 단건_조회_요청(long id) {
		return request(GET, createRequestPathWithVariable(requestBasePath, id), Collections.emptyMap());
	}

	public ExtractableResponse<Response> 생성_요청(Map<String, String> params) {
		return request(POST, requestBasePath, params);
	}

	public ExtractableResponse<Response> 삭제_요청(ExtractableResponse<Response> response) {
		return request(DELETE, createRequestPathWithVariable(requestBasePath, 아이디_추출(response)), Collections.emptyMap());
	}

	public ExtractableResponse<Response> 수정_요청(long id, Map<String, String> params) {
		return request(PUT, createRequestPathWithVariable(requestBasePath, id), params);
	}

	public long 아이디_추출(ExtractableResponse<Response> response) {
		return extractId(response, idFieldKey);
	}

	private ExtractableResponse<Response> request(Method method, String path, Map<String, String> params) {
		return RestAssured
				.given().log().all()
				.contentType(APPLICATION_JSON_VALUE)
				.body(params)
				.when()
				.request(method, path)
				.then().log().all()
				.extract();
	}
}

