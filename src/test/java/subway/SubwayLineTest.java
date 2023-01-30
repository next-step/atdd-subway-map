package subway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineTest {

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선 생성")
	@Test
	void createLine() {
		// when
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1, 2, 10);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> lineNames =
			RestAssured.given().log().all()
				.when().get("/lines")
				.then().log().all()
				.extract().jsonPath().getList("name", String.class);
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
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1, 2, 10);
		LineRequest lineRequest2 = new LineRequest("분당선", "bg-green-600", 1, 3, 10);

		ExtractableResponse<Response> createResponse1 = RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
		assertThat(createResponse1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		ExtractableResponse<Response> createResponse2 = RestAssured
			.given().log().all()
			.body(lineRequest2)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
		assertThat(createResponse2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// when
		List<String> lineNames = RestAssured
			.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.extract().jsonPath().getList("name", String.class);

		// then
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
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1, 2, 10);

		ExtractableResponse<Response> createResponse = RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		long id = createResponse.jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> showResponse = RestAssured
			.given().log().all()
			.when().get("/lines/{id}", id)
			.then().log().all()
			.extract();

		// then
		Assertions.assertAll(
			() -> assertThat(showResponse.jsonPath().getLong("id")).isEqualTo(id),
			() -> assertThat(showResponse.jsonPath().getString("name")).isEqualTo("신분당선"),
			() -> assertThat(showResponse.jsonPath().getString("color")).isEqualTo("bg-red-600")
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
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1, 2, 10);

		ExtractableResponse<Response> createResponse = RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		long id = createResponse.jsonPath().getLong("id");

		// when
		Map<String, String> params = new HashMap<>();
		params.put("name", "다른분당선");
		params.put("color", "bg-red-600");

		RestAssured
			.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put("/lines/{id}", id)
			.then().log().all()
			.assertThat().statusCode(200);

		// then
		ExtractableResponse<Response> showResponse = RestAssured
			.given().log().all()
			.when().get("/lines/{id}", id)
			.then().log().all()
			.extract();

		Assertions.assertAll(
			() -> assertThat(showResponse.jsonPath().getLong("id")).isEqualTo(id),
			() -> assertThat(showResponse.jsonPath().getString("name")).isEqualTo("다른분당선"),
			() -> assertThat(showResponse.jsonPath().getString("color")).isEqualTo("bg-red-600")
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
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1, 2, 10);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();

		long id = response.jsonPath().getLong("id");

		// when
		RestAssured
			.given().log().all()
			.when().delete("/lines/{id}", id)
			.then().log().all()
			.assertThat().statusCode(204);

		// then
		long showId = RestAssured
			.given().log().all()
			.when().get("/lines/{id}", id)
			.then().log().all()
			.extract().jsonPath().getLong("id");
		assertThat(showId).isNotEqualTo(id);
	}

	class LineRequest {
		public String name;
		public String color;
		public int upStationsId;
		public int downStationsId;
		public int distance;

		public LineRequest(String name, String color, int upStationsId, int downStationsId, int distance) {
			this.name = name;
			this.color = color;
			this.upStationsId = upStationsId;
			this.downStationsId = downStationsId;
			this.distance = distance;
		}
	}
}
