package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 노선 인수테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationLineAcceptanceTest {
	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회시 생성한 노선을 찾을수 있다.
	 */
	@DisplayName("지하철 노선 생성 테스트")
	@Test
	void createStationLintTest() {

		//given
		long upStationId = 지하철_생성_요청(Map.of("name", "판교역")).jsonPath().getLong("id");
		long downStationId = 지하철_생성_요청(Map.of("name", "정자역")).jsonPath().getLong("id");

		Map<String, Object> param =
			Map.of("name", "신분당선", "color", "bg-red-600", "upStationId", upStationId, "downStationId", downStationId, "distance", 10);

		//when
		ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(param);

		//then
		assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		List<Long> stationsIdList = createLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationsIdList).contains(upStationId, downStationId);

	}

	private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> param) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.post("/lines")
			.then().log().all().extract();
	}

	/**
	 *
	 * given 2개의 지하철 노선을 생성하고
	 * when 지하철 노선 목록을 조회하면
	 * then 지하철 조선 목록 조회시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선 목록 조회 테스트")
	@Test
	void getAllLinesTest() {

		//given
		long 신분당선_상행종점역_번호 = 지하철_생성_요청(Map.of("name", "판교역")).jsonPath().getLong("id");
		long 신분당선_하행종점역_번호 = 지하철_생성_요청(Map.of("name", "정자역")).jsonPath().getLong("id");
		Map<String, Object> param =
			Map.of("name", "신분당선", "color", "bg-red-600", "upStationId", 신분당선_상행종점역_번호, "downStationId", 신분당선_하행종점역_번호, "distance", 10);
		지하철_노선_생성_요청(param);

		long 분당선_상행종점역_번호 = 지하철_생성_요청(Map.of("name", "서현역")).jsonPath().getLong("id");
		long 분당선_하행종점역_번호 = 지하철_생성_요청(Map.of("name", "이매역")).jsonPath().getLong("id");
		param =
			Map.of("name", "분당선", "color", "yellow", "upStationId", 분당선_상행종점역_번호, "downStationId", 분당선_하행종점역_번호, "distance", 10);
		지하철_노선_생성_요청(param);

		//when
		ExtractableResponse<Response> getAllLineResponse = 지하철_노선_목록_조회();

		//then
		assertThat(getAllLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(getAllLineResponse.jsonPath().getList("[0].stations.id", Long.class)).contains(신분당선_상행종점역_번호, 신분당선_하행종점역_번호);
		assertThat(getAllLineResponse.jsonPath().getList("[1].stations.id", Long.class)).contains(분당선_상행종점역_번호, 분당선_하행종점역_번호);

	}

	private ExtractableResponse<Response> 지하철_노선_목록_조회() {
		return RestAssured.given().log().all()
			.when()
			.get("/lines")
			.then().log().all().extract();
	}

	/**
	 * given 지하철 노선을 생성하고
	 * when 생성한 지하철 노선 목록을 조회하면
	 * then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 * @throws Exception
	 */
	@DisplayName("지하철 노선 조회 테스트")
	@Test
	void getLineTest() throws Exception {

		//given
		long 신분당선_상행종점역_번호 = 지하철_생성_요청(Map.of("name", "판교역")).jsonPath().getLong("id");
		long 신분당선_하행종점역_번호 = 지하철_생성_요청(Map.of("name", "정자역")).jsonPath().getLong("id");
		Map<String, Object> param =
			Map.of("name", "신분당선", "color", "bg-red-600", "upStationId", 신분당선_상행종점역_번호, "downStationId", 신분당선_하행종점역_번호, "distance", 10);
		long 신분당선_노선_번호 = 지하철_노선_생성_요청(param).jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(신분당선_노선_번호);

		//then
		assertThat(getLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 지하철_노선_조회_요청(long lineId) {
		return RestAssured.given().log().all()
			.when()
			.get("/lines/{id}", lineId)
			.then().log().all().extract();
	}

}
