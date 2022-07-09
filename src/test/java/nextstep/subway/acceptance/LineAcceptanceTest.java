package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
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

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

	@LocalServerPort
	private int port;

	private static final String LINE_NAME = "신분당선";
	private static final String UP_STATION_NAME = "강남역";
	private static final String DOWN_STATION_NAME = "양재역";
	private ExtractableResponse<Response> responseOfLine;
	private Long upStationId;
	private Long downStationId;
	private Long lineId;

	private final StationAcceptanceTest stationAcceptanceTest;

	public LineAcceptanceTest() {
		this.stationAcceptanceTest = new StationAcceptanceTest();
	}

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;

		// given 지하철 역 생성
		upStationId = stationAcceptanceTest.지하철역_생성_파싱_id(stationAcceptanceTest.지하철역_생성(UP_STATION_NAME));
		downStationId = stationAcceptanceTest.지하철역_생성_파싱_id(stationAcceptanceTest.지하철역_생성(DOWN_STATION_NAME));

		// given 지하철 노선 생성
		responseOfLine = 지하철노선_생성(LINE_NAME, "bg-red-600", upStationId, downStationId, 10L);
		lineId = responseOfLine.jsonPath().getLong("id");
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() {
		// then
		assertThat(responseOfLine.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> lineNames = 지하철노선_목록_조회_파싱_name(responseOfLine);
		assertThat(lineNames).contains(LINE_NAME);
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선 목록을 조회한다.")
	@Test
	void showLines() {
		// given
		Long newStationId = stationAcceptanceTest.지하철역_생성_파싱_id(stationAcceptanceTest.지하철역_생성("역삼역"));

		// given
		지하철노선_생성("2호선", "bg-green-600", downStationId, newStationId, 15L);

		// when
		ExtractableResponse<Response> response = 지하철노선_목록_조회();
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		List<String> lineNames = 지하철노선_목록_조회_파싱_name(response);
		assertThat(lineNames).hasSize(2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("지하철 노선의 정보를 조회한다.")
	@Test
	void showLine() {
		// when
		ExtractableResponse<Response> response = 지하철노선_조회(lineId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(지하철노선_조회_파싱_name(response)).isEqualTo(LINE_NAME);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void modifyLine() {
		// when
		String newLineName = "뉴신분당선";
		ExtractableResponse<Response> response = 지하철노선_수정(lineId, newLineName, "bg-orange-600");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(지하철노선_조회_파싱_name(지하철노선_조회(lineId))).isEqualTo(newLineName);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철 노선을 삭제한다.")
	@Test
	void deleteLine() {
		// when
		ExtractableResponse<Response> response = 지하철노선_삭제(lineId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(지하철노선_목록_조회_파싱_name(지하철노선_목록_조회())).doesNotContain(LINE_NAME);
	}

	private ExtractableResponse<Response> 지하철노선_생성(String name, String color, Long upStationId, Long downStationId,
		Long distance) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 지하철노선_목록_조회() {
		return RestAssured.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.extract();
	}

	private List<String> 지하철노선_목록_조회_파싱_name(ExtractableResponse<Response> response) {
		return response
			.jsonPath().getList("name", String.class);
	}

	private ExtractableResponse<Response> 지하철노선_조회(Long id) {
		return RestAssured.given().log().all()
			.when().get("/lines/" + id)
			.then().log().all()
			.extract();
	}

	private String 지하철노선_조회_파싱_name(ExtractableResponse<Response> response) {
		return response
			.jsonPath().getString("name");
	}

	private ExtractableResponse<Response> 지하철노선_수정(Long lineId, String name, String color) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put("/lines/" + lineId)
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 지하철노선_삭제(Long lineId) {
		return RestAssured.given().log().all()
			.when().delete("/lines/" + lineId)
			.then().log().all()
			.extract();
	}
}
