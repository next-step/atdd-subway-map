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

	private final StationAcceptanceTest stationAcceptanceTest;

	public LineAcceptanceTest() {
		this.stationAcceptanceTest = new StationAcceptanceTest();
	}

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
		Long upStationId = stationAcceptanceTest.지하철역_생성_파싱(UP_STATION_NAME);
		Long downStationId = stationAcceptanceTest.지하철역_생성_파싱(DOWN_STATION_NAME);

		// when
		ExtractableResponse<Response> response = 지하철노선_생성(LINE_NAME, "bg-red-600", upStationId, downStationId, 10L);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> lineNames = 지하철노선_목록_파싱();
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
		Long upStationId = stationAcceptanceTest.지하철역_생성_파싱(UP_STATION_NAME);
		Long downStationId = stationAcceptanceTest.지하철역_생성_파싱(DOWN_STATION_NAME);
		Long downStationIdOfLine2 = stationAcceptanceTest.지하철역_생성_파싱("역삼역");

		// given
		지하철노선_생성(LINE_NAME, "bg-red-600", upStationId, downStationId, 10L);
		지하철노선_생성("2호선", "bg-green-600", downStationId, downStationIdOfLine2, 15L);

		// when
		ExtractableResponse<Response> response = 지하철노선_목록_조회();
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		List<String> lineNames = 지하철노선_목록_파싱();
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

	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void modifyLine() {

	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철 노선을 삭제한다.")
	@Test
	void deleteLine() {

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

	private List<String> 지하철노선_목록_파싱() {
		return 지하철노선_목록_조회()
			.jsonPath().getList("name", String.class);
	}
}
