package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

	private static final String LINE_COLOR_RED = "bg-red-600";
	private static final String LINE_COLOR_BLUE = "bg-blue-600";

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
		Map<String, Object> createLineRequest =
			지하철노선_생성_파라미터생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		지하철라인_생성(createLineRequest);

		//then
		List<String> names = 지하철라인_전체_조회();
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
		Map<String, Object> createLineRequest1 =
			지하철노선_생성_파라미터생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		Map<String, Object> createLineRequest2 =
			지하철노선_생성_파라미터생성(SAMSUNG_STATION, LINE_COLOR_RED, 2, 3, 8);

		지하철라인_생성(createLineRequest1);
		지하철라인_생성(createLineRequest2);

		//when
		List<String> names = 지하철라인_전체_조회();

		//then
		assertThat(names).hasSize(2)
			.containsExactly(SIN_BOONDANG_LINE, SAMSUNG_STATION);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@Test
	void 지하철노선_조회() {
		//given
		Map<String, Object> createLineRequest =
			지하철노선_생성_파라미터생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		long stationId = 지하철라인_생성(createLineRequest);

		//when
		Map<String, Object> response = 지하철라인_조회_BY_ID(stationId, HttpStatus.OK);

		//then
		assertEquals(response.get("name"), SIN_BOONDANG_LINE);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@Test
	void 지하철노선_수정() {
		//given
		Map<String, Object> createdLineRequest =
			지하철노선_생성_파라미터생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		long stationId = 지하철라인_생성(createdLineRequest);

		//when
		Map<String, Object> updatedLineRequest = generateUpdateRequestDTO(SAMSUNG_STATION, LINE_COLOR_BLUE);
		RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(updatedLineRequest)
			.when().put("/lines/" + stationId)
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());

		//then
		Map<String, Object> response = 지하철라인_조회_BY_ID(stationId, HttpStatus.OK);
		assertEquals(SAMSUNG_STATION, response.get("name"));
		assertEquals(LINE_COLOR_BLUE, response.get("color"));

	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@Test
	void 지하철노선_삭제() {
		//given
		Map<String, Object> createdLineRequest =
			지하철노선_생성_파라미터생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		long stationId = 지하철라인_생성(createdLineRequest);

		//when
		RestAssured.given().log().all()
			.when().delete("/lines/" + stationId)
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());

		//then
		Map<String, Object> response = 지하철라인_조회_BY_ID(stationId, HttpStatus.OK);
		assertThat(response.get("name")).isNull();

	}

	private Map<String, Object> 지하철노선_생성_파라미터생성(String stationName, String stationColor, long upstationId,
		long downStationId,
		long distance) {
		Map<String, Object> requestParameter = new HashMap<>();
		requestParameter.put("name", stationName);
		requestParameter.put("color", stationColor);
		requestParameter.put("upStationId", upstationId);
		requestParameter.put("downStationId", downStationId);
		requestParameter.put("distance", distance);
		return requestParameter;
	}

	private Map<String, Object> generateUpdateRequestDTO(String stationName, String stationColor) {
		Map<String, Object> requestParameter = new HashMap<>();
		requestParameter.put("name", stationName);
		requestParameter.put("color", stationColor);
		return requestParameter;
	}

	private Long 지하철라인_생성(Map<String, Object> requestParameter) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(requestParameter)
			.when().post("/lines")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.jsonPath().getLong("id");
	}

	private List<String> 지하철라인_전체_조회() {
		return RestAssured.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath().getList("name", String.class);
	}

	private Map<String, Object> 지하철라인_조회_BY_ID(Long id, HttpStatus httpStatus) {
		return RestAssured.given().log().all()
			.when().get("/lines/" + id)
			.then().log().all()
			.statusCode(httpStatus.value())
			.extract().jsonPath().get();
	}
}
