package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:/truncate.sql")
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;

		createStation(Map.of("name", "지하철역"));
		createStation(Map.of("name", "새로운지하철역"));
		createStation(Map.of("name", "또다른지하철역"));
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
	 */
	@DisplayName("지하철노선 생성")
	@Test
	void createLine() {
		// when
		createLineFrom("신분당선", "bg-red-600", 1L, 2L);
		// then
		List<String> names = responseOfShowLines().jsonPath().getList("name");

		assertThat(names.get(0)).isEqualTo("신분당선");
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철노선 목록 조회")
	@Test
	void showLines() {
		// given
		createLineFrom("신분당선", "bg-red-600", 1L, 2L);
		createLineFrom("분당선", "bg-green-600", 1L, 3L);

		// when
		List<Long> id = responseOfShowLines().jsonPath().getList("id");

		// then
		assertThat(id).hasSize(2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("지하철노선 조회")
	@Test
	void showLine() {
		// given
		Number tmpId = createLineFrom("신분당선", "bg-red-600", 1L, 2L)
				.jsonPath().get("id");
		Long id = tmpId.longValue();

		// when
		String name = responseOfShowLine(id).jsonPath().get("name");

		// then
		assertThat(name).isEqualTo("신분당선");
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다.
	 */
	@DisplayName("지하철노선 수정")
	@Test
	void updateLine() {
		// given
		Number tmpId = createLineFrom("신분당선", "bg-red-600", 1L, 2L)
				.jsonPath().get("id");
		Long id = tmpId.longValue();

		// when
		Map<String, String> params1 = new HashMap<>();
		params1.put("name", "다른분당선");
		params1.put("color", "bg-red-600");

		RestAssured
				.given().log().all()
				.body(params1)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().put("/lines/{lineId}", id)
				.then().log().all();

		// then
		String name = responseOfShowLine(id).jsonPath().get("name");
		assertThat(name).isEqualTo("다른분당선");
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다.
	 */
	@DisplayName("지하철노선 삭제")
	@Test
	void deleteLine() {
		// given
		Number tmpId = createLineFrom("신분당선", "bg-red-600", 1L, 2L)
				.jsonPath().get("id");
		Long id = tmpId.longValue();

		// when
		RestAssured
				.given().log().all()
				.when().delete("/lines/{lineId}", id)
				.then().log().all();

		// then
		List<Long> ids = responseOfShowLines().jsonPath().getList("id");
		assertThat(ids).isEmpty();
	}

	private ExtractableResponse<Response> createLineFrom(String name, String color, Long upStationId, Long downStationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);

		return createLineFrom(params);
	}

	private ExtractableResponse<Response> createLineFrom(Map<String, Object> params) {
		 return RestAssured
				.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all()
				.extract();
	}

	private ExtractableResponse<Response> responseOfShowLine(Long id) {
		return RestAssured
				.given().log().all()
				.when().get("/lines/{lineId}", id)
				.then().log().all()
				.extract();
	}

	private ExtractableResponse<Response> responseOfShowLines() {
		return RestAssured
				.given().log().all()
				.when().get("/lines")
				.then().log().all()
				.extract();
	}

	private ExtractableResponse<Response> createStation(Map<String, String> station) {
		return RestAssured.given().log().all()
				.body(station)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/stations")
				.then().log().all()
				.extract();
	}
}
