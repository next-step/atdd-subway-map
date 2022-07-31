package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() {
		// given
		String lineName = "우이신설선";

		// when
		ExtractableResponse<Response> response = callCreateLine(makeCreateLineParams(lineName, "bg-red-600", 1L, 2L, 10));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> lineNames = callGetLines().jsonPath().getList("name", String.class);
		assertThat(lineNames).containsAnyOf(lineName);
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
	 */
	@DisplayName("지하철 노선 목록을 조회한다.")
	@Test
	void getLines() {
		// given
		String lineShinbundang = "신분당선";
		callCreateLine(makeCreateLineParams(lineShinbundang, "bg-red-600", 1L, 2L, 10));

		String lineBundang = "분당선";
		callCreateLine(makeCreateLineParams(lineBundang, "bg-green-600", 1L, 3L, 10));

		// when
		ExtractableResponse<Response> response = callGetLines();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		List<String> lineNames = response.jsonPath().getList("name", String.class);
//		assertThat(lineNames).hasSize(2);// TODO 인수 테스트 격리
		assertThat(lineNames).containsOnlyOnce(lineShinbundang, lineBundang);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
	 */
	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void getLine() {
		// given
		String lineName = "수인분당선";
		long lineId = callCreateLine(makeCreateLineParams(lineName, "bg-red-600", 1L, 2L, 10))
			.jsonPath()
			.getLong("id");

		// when
		ExtractableResponse<Response> response = callGetLine(lineId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		String responseLineName = response.jsonPath().get("name");
		assertThat(responseLineName).isEqualTo(lineName);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLine() {
		// given
		long lineId = callCreateLine(makeCreateLineParams("경의중앙선", "bg-red-600", 1L, 2L, 10))
			.jsonPath()
			.getLong("id");

		String updateLineName = "다른분당선";
		String updateLineColor = "bg-green-600";

		// when
		ExtractableResponse<Response> response = callUpdateLine(lineId, makeUpdateLineParams(updateLineName, updateLineColor));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		ExtractableResponse<Response> lineResponse = callGetLine(lineId);
		String responseLineName = lineResponse.jsonPath().get("name");
		String responseLineColor = lineResponse.jsonPath().get("color");

		assertThat(responseLineName).isEqualTo(updateLineName);
		assertThat(responseLineColor).isEqualTo(updateLineColor);
	}

	@DisplayName("지하철 노선을 삭제한다.")
	@Test
	void deleteLine() {
		String lineName = "경춘선";
		long lineId = callCreateLine(makeCreateLineParams(lineName, "bg-red-600", 1L, 2L, 10))
			.jsonPath()
			.getLong("id");

		// when
		ExtractableResponse<Response> response = callDeleteLine(lineId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		// then
		ExtractableResponse<Response> allLinesResponse = callGetLines();
		List<String> allLineNames = allLinesResponse.jsonPath().getList("name", String.class);
		List<Long> allLineIds = allLinesResponse.jsonPath().getList("id", Long.class);

		assertThat(allLineNames).doesNotContain(lineName);
		assertThat(allLineIds).doesNotContain(lineId);
	}

	private Map<String, String> makeCreateLineParams(String lineName, String color, Long upStationId, Long downStationId, Integer distance) {
		Map<String, String> params = new HashMap<>();
		params.put("name", lineName);
		params.put("color", color);
		params.put("upStationId", String.valueOf(upStationId));
		params.put("downStationId", String.valueOf(downStationId));
		params.put("distance", String.valueOf(distance));
		return params;
	}

	private Map<String, String> makeUpdateLineParams(String lineName, String color) {
		Map<String, String> params = new HashMap<>();
		params.put("name", lineName);
		params.put("color", color);
		return params;
	}

	private ExtractableResponse<Response> callCreateLine(Map<String, String> params) {
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> callGetLines() {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/lines")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> callGetLine(Long lineId) {
		return RestAssured.given().log().all()
			.pathParam("id", lineId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/lines/{id}")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> callUpdateLine(long lineId, Map<String, String> params) {
		return RestAssured.given().log().all()
			.pathParam("id", lineId)
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put("/lines/{id}")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> callDeleteLine(long lineId) {
		return RestAssured.given().log().all()
			.pathParam("id", lineId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/lines/{id}")
			.then().log().all()
			.extract();
	}
}
