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
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;

@DisplayName("지하철 노선 테스트")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationLIneAcceptanceTest {

	private static final String SIN_BOONDANG_LINE = "신분당선";
	private static final String SAMSUNG_STATION = "삼성역";
	private static final String LINE_COLOR_RED = "bg-red-600";

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
	@Test
	void 지하철노선_생성() {
		//when
		Map<Object, Object> createLineRequest =
			generateCreatedRequestDTO(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		createLine(createLineRequest);

		//then
		List<String> names = getLines();
		assertThat(names).containsAnyOf(SIN_BOONDANG_LINE);

	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@Test
	void 지하철노선_목록_조회() {
		//given
		Map<Object, Object> createLineRequest =
			generateCreatedRequestDTO(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		createLine(createLineRequest);
		createLineRequest =
			generateCreatedRequestDTO(SAMSUNG_STATION, LINE_COLOR_RED, 2, 3, 8);
		createLine(createLineRequest);

		//when
		List<String> names = getLines();

		//then
		assertThat(names).hasSize(2)
			.containsAnyOf(SIN_BOONDANG_LINE)
			.containsAnyOf(SAMSUNG_STATION);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@Test
	void 지하철노선_조회() {
		//given
		Map<Object, Object> createLineRequest =
			generateCreatedRequestDTO(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		long stationId = createLine(createLineRequest);

		//when
		String stationName = getLine(stationId);

		//then
		assertThat(stationName).isEqualTo(SIN_BOONDANG_LINE);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@Test
	void 지하철노선_수정() {
		//given
		Map<Object, Object> createdLineRequest =
			generateCreatedRequestDTO(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		long stationId = createLine(createdLineRequest);

		//when
		Map<Object, Object> updatedLineRequest = generateUpdateRequestDTO(SAMSUNG_STATION, LINE_COLOR_RED);
		RestAssured.given().log().all()
			.body(updatedLineRequest)
			.when().put("/lines/" + stationId)
			.then().log().all()
			.statusCode(HttpStatus.OK.value());

		//then
		String name = getLine(stationId, "name");
		String color = getLine(stationId, "color");
		assertThat(name).isEqualTo(updatedLineRequest.get("name"));
		assertThat(color).isEqualTo(updatedLineRequest.get("color"));

	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@Test
	void 지하철노선_삭제() {
		//given
		Map<Object, Object> createdLineRequest =
			generateCreatedRequestDTO(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		long stationId = createLine(createdLineRequest);

		//when
		RestAssured.given().log().all()
			.when().delete("/lines/" + stationId)
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());

		//then
		assertThat(getLine(stationId)).isEmpty();

	}

	private Map<Object, Object> generateCreatedRequestDTO(String stationName, String stationColor, long upstationId,
		long downStationId,
		long distance) {
		Map<Object, Object> requestParameter = new HashMap<>();
		requestParameter.put("name", stationName);
		requestParameter.put("color", stationColor);
		requestParameter.put("upStationId", upstationId);
		requestParameter.put("downStationId", downStationId);
		requestParameter.put("distance", distance);
		return requestParameter;
	}

	private Map<Object, Object> generateUpdateRequestDTO(String stationName, String stationColor) {
		Map<Object, Object> requestParameter = new HashMap<>();
		requestParameter.put("name", stationName);
		requestParameter.put("color", stationColor);
		return requestParameter;
	}

	private Long createLine(Map<Object, Object> requestParameter) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(requestParameter)
			.when().post("/lines")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.jsonPath().getLong("id");
	}

	private List<String> getLines() {
		return RestAssured.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath().getList("name", String.class);
	}

	private String getLine(Long id) {
		return RestAssured.given().log().all()
			.when().get("/lines/" + id)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath().getString("name");
	}

	private String getLine(Long id, String path) {
		return RestAssured.given().log().all()
			.when().get("/lines/" + id)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath().getString(path);
	}
}
