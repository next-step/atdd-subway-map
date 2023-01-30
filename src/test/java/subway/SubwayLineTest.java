package subway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.LineRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineTest {

	@BeforeEach
	void createStations() {
		createStation("신분당선");
		createStation("새로운지하철역");
		createStation("또다른지하철역");
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선 생성")
	@Test
	void createLine() {
		// when
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
		createLine(lineRequest);

		// then
		List<String> lineNames = showLines().jsonPath().getList("name", String.class);
		assertThat(lineNames).containsAnyOf("신분당선");
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 * */
	@DisplayName("지하철 노선 목록 조회")
	@Test
	void showLineList() {
		// given
		LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
		LineRequest lineRequest2 = new LineRequest("분당선", "bg-green-600", 1L, 3L, 10);

		createLine(lineRequest1);
		createLine(lineRequest2);

		// when
		// then
		List<String> lineNames = showLines().jsonPath().getList("name", String.class);
		assertThat(lineNames.size()).isEqualTo(2);
		assertThat(lineNames).containsAnyOf("신분당선", "분당선");
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 * */
	@DisplayName("지하철 노선 조회")
	@Test
	void showLine() {
		// given
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

		long id = createLine(lineRequest).jsonPath().getLong("id");

		// when
		// then
		Assertions.assertAll(
			() -> assertThat(showLineById(id).jsonPath().getLong("id")).isEqualTo(id),
			() -> assertThat(showLineById(id).jsonPath().getString("name")).isEqualTo("신분당선"),
			() -> assertThat(showLineById(id).jsonPath().getString("color")).isEqualTo("bg-red-600")
		);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 * */
	@DisplayName("지하철 노선 수정")
	@Test
	void updateLine() {
		// given
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

		long id = createLine(lineRequest).jsonPath().getLong("id");

		// when
		updateLine(id, "다른분당선", "bg-red-600");

		// then
		Assertions.assertAll(
			() -> assertThat(showLineById(id).jsonPath().getLong("id")).isEqualTo(id),
			() -> assertThat(showLineById(id).jsonPath().getString("name")).isEqualTo("다른분당선"),
			() -> assertThat(showLineById(id).jsonPath().getString("color")).isEqualTo("bg-red-600")
		);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 * */
	@DisplayName("지하철 노선 삭제")
	@Test
	void deleteLine() {
		// given
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

		long id = createLine(lineRequest).jsonPath().getLong("id");

		// when
		deleteLine(id);

		// then
		assertThat(showLineById(id).jsonPath().getLong("id")).isNotEqualTo(id);
	}

	private ExtractableResponse<Response> createLine(LineRequest lineRequest) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return response;
	}

	private ExtractableResponse<Response> showLines() {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		return response;
	}

	private ExtractableResponse<Response> showLineById(Long id) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/lines/{id}", id)
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		return response;
	}

	private ExtractableResponse<Response> updateLine(Long id, String name, String color) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put("/lines/{id}", id)
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		return response;
	}

	private ExtractableResponse<Response> deleteLine(Long id) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().delete("/lines/{id}", id)
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		return response;
	}

	private ExtractableResponse<Response> createStation(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return response;
	}
}
