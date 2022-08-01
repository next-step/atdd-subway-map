package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

	private final String LINE_BUNDANG = "분당선";
	private final String LINE_SHINBUNDANG = "신분당선";
	private final String LINE_OTHER = "다른분당선";
	private final String COLOR_RED = "bg-red-600";
	private final String COLOR_GREEN = "bg-green-600";

	private Long STATION_ID_GANGNAM;
	private Long STATION_ID_HANTI;

	@LocalServerPort
	int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	private StationAcceptanceTest stationAcceptanceTest;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();

		stationAcceptanceTest = new StationAcceptanceTest();
		STATION_ID_GANGNAM = stationAcceptanceTest.callCreateStation("강남역").jsonPath().getLong("id");
		STATION_ID_HANTI = stationAcceptanceTest.callCreateStation("한티역").jsonPath().getLong("id");
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() {
		// when
		ExtractableResponse<Response> response = callCreateLine(makeCreateLineParams(LINE_BUNDANG, COLOR_RED, STATION_ID_GANGNAM, STATION_ID_HANTI, 10));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> lineNames = callGetLines().jsonPath().getList("name", String.class);
		assertThat(lineNames).containsAnyOf(LINE_BUNDANG);
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
		callCreateLine(makeCreateLineParams(LINE_BUNDANG, COLOR_RED, STATION_ID_GANGNAM, STATION_ID_HANTI, 10));
		callCreateLine(makeCreateLineParams(LINE_SHINBUNDANG, COLOR_GREEN, STATION_ID_GANGNAM, STATION_ID_HANTI, 10));

		// when
		ExtractableResponse<Response> response = callGetLines();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		List<String> lineNames = response.jsonPath().getList("name", String.class);
		assertThat(lineNames).hasSize(2);
		assertThat(lineNames).containsOnlyOnce(LINE_BUNDANG, LINE_SHINBUNDANG);
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
		long lineId = callCreateLine(makeCreateLineParams(LINE_BUNDANG, COLOR_RED, STATION_ID_GANGNAM, STATION_ID_HANTI, 10))
			.jsonPath()
			.getLong("id");

		// when
		ExtractableResponse<Response> response = callGetLine(lineId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		String responseLineName = response.jsonPath().get("name");
		assertThat(responseLineName).isEqualTo(LINE_BUNDANG);
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
		long lineId = callCreateLine(makeCreateLineParams(LINE_BUNDANG, COLOR_RED, STATION_ID_GANGNAM, STATION_ID_HANTI, 10))
			.jsonPath()
			.getLong("id");

		// when
		ExtractableResponse<Response> response = callUpdateLine(lineId, makeUpdateLineParams(LINE_OTHER, COLOR_GREEN));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		ExtractableResponse<Response> lineResponse = callGetLine(lineId);
		String responseLineName = lineResponse.jsonPath().get("name");
		String responseLineColor = lineResponse.jsonPath().get("color");

		assertThat(responseLineName).isEqualTo(LINE_OTHER);
		assertThat(responseLineColor).isEqualTo(COLOR_GREEN);
	}

	@DisplayName("지하철 노선을 삭제한다.")
	@Test
	void deleteLine() {
		long lineId = callCreateLine(makeCreateLineParams(LINE_BUNDANG, COLOR_RED, STATION_ID_GANGNAM, STATION_ID_HANTI, 10))
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

		assertThat(allLineNames).doesNotContain(LINE_BUNDANG);
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
