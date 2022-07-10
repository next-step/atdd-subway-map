package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
	 */
	@DisplayName("지하철노선 생성")
	@Test
	void createLine() {
		// when
		Map<String, String> params = new HashMap<>();
		params.put("name", "신분당선");
		params.put("color", "bg-red-600");
		params.put("upStationId", "1");
		params.put("downStationId", "2");

		RestAssured
				.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all();
		// then
		List<String> names = RestAssured
				.given().log().all()
				.when().get("/lines")
				.then().log().all()
				.extract().jsonPath().getList("name");

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
		Map<String, String> params1 = new HashMap<>();
		params1.put("name", "신분당선");
		params1.put("color", "bg-red-600");
		params1.put("upStationId", "1");
		params1.put("downStationId", "2");

		RestAssured
				.given().log().all()
				.body(params1)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all();

		Map<String, String> params2 = new HashMap<>();
		params2.put("name", "분당선");
		params2.put("color", "bg-green-600");
		params2.put("upStationId", "1");
		params2.put("downStationId", "3");

		RestAssured
				.given().log().all()
				.body(params2)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all();
		// when
		List<Long> id = RestAssured
				.given().log().all()
				.when().get("/lines")
				.then().log().all()
				.extract().jsonPath().getList("id");
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
		Map<String, String> params1 = new HashMap<>();
		params1.put("name", "신분당선");
		params1.put("color", "bg-red-600");
		params1.put("upStationId", "1");
		params1.put("downStationId", "2");

		RestAssured
				.given().log().all()
				.body(params1)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all();
		// when
		String name = RestAssured
				.given()
				.when().get("/lines/{}", 1)
				.then()
				.extract().jsonPath().get("name");
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
		Map<String, String> params = new HashMap<>();
		params.put("name", "신분당선");
		params.put("color", "bg-red-600");
		params.put("upStationId", "1");
		params.put("downStationId", "2");

		RestAssured
				.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all();
		// when
		Map<String, String> params1 = new HashMap<>();
		params1.put("name", "다른분당선");
		params1.put("color", "bg-red-600");

		RestAssured
				.given().log().all()
				.body(params1)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().put("/lines/{}", 1)
				.then().log().all();
		// then
		String name = RestAssured
				.given().log().all()
				.when().get("/lines/{}", 1)
				.then().log().all()
				.extract().jsonPath().get("name");
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
		Map<String, String> params = new HashMap<>();
		params.put("name", "신분당선");
		params.put("color", "bg-red-600");
		params.put("upStationId", "1");
		params.put("downStationId", "2");

		RestAssured
				.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all();
		// when
		RestAssured
				.given().log().all()
				.when().delete("/lines/{}", 1)
				.then().log().all();

		// then
		List<Long> id = RestAssured
				.given().log().all()
				.when().get("/lines")
				.then().log().all()
				.extract().jsonPath().getList("id");
		assertThat(id).isEmpty();
	}
}
