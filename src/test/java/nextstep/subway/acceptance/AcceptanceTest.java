package nextstep.subway.acceptance;

import static io.restassured.http.Method.*;
import static nextstep.subway.utils.AcceptanceTestUtils.*;
import static org.apache.http.HttpHeaders.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

	final String requestBasePath;
	final String idFieldKey;

	@LocalServerPort
	int port;

	@Autowired
	DatabaseCleanup databaseCleanup;

	protected AcceptanceTest(String requestBasePath, String idFieldKey) {
		this.requestBasePath = requestBasePath;
		this.idFieldKey = idFieldKey;
	}

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
	}

	// == Request Methods == //
	protected ExtractableResponse<Response> 목록_조회_요청() {
		return request(GET, requestBasePath, Collections.emptyMap());
	}

	protected ExtractableResponse<Response> 단건_조회_요청(long id) {
		return request(GET, createRequestPathWithVariable(requestBasePath, id), Collections.emptyMap());
	}

	protected ExtractableResponse<Response> 생성_요청(Map<String, String> params) {
		return request(POST, requestBasePath, params);
	}

	protected ExtractableResponse<Response> 삭제_요청(long id) {
		return request(DELETE, createRequestPathWithVariable(requestBasePath, id), Collections.emptyMap());
	}

	protected ExtractableResponse<Response> 수정_요청(long id, Map<String, String> params) {
		return request(PUT, createRequestPathWithVariable(requestBasePath, id), params);
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

	protected long 아이디_추출(ExtractableResponse<Response> response) {
		return extractId(response, idFieldKey);
	}

	// == Response Validation Methods == //
	protected void 생성_요청_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header(LOCATION)).isNotEmpty();
	}

	protected void 삭제_요청_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
	}

	protected void 조회_요청_목록_검증(ExtractableResponse<Response> response, String fieldName, List<String> expectedValues) {
		assertThat(response.statusCode()).isEqualTo(OK.value());
		assertThat(response.jsonPath().getList(fieldName)).containsExactlyInAnyOrderElementsOf(expectedValues);
	}

	protected void 조회_요청_단건_검증(ExtractableResponse<Response> response, String fieldName, String expectedValues) {
		assertThat(response.statusCode()).isEqualTo(OK.value());
		assertThat(response.jsonPath().getString(fieldName)).isEqualTo(expectedValues);
	}

	protected void 수정_요청_검증(ExtractableResponse<Response> response, String fieldName, String expectedValue) {
		조회_요청_단건_검증(response, fieldName, expectedValue);
	}

	protected void 중복_생성_요청_실패_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
	}
}
