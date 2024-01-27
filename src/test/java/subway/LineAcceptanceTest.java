package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "/db/test.sql")
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
	private static final String TEST_LINE_NAME_1 = "신분당선";
	private static final String TEST_LINE_NAME_2 = "분당선";
	private static final String TEST_LINE_COLOR_1 = "bg-red";
	private static final String TEST_LINE_COLOR_2 = "bg-blue";

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLineTest() {
		// when
		ExtractableResponse<Response> response = createLine(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 1L, 2L, 10);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(getLines().jsonPath().getList("name")).contains(TEST_LINE_NAME_1);
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선 목록을 조회한다.")
	@DirtiesContext
	@Test
	void getLinesTest() {
		// given
		createLine(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 1L, 2L, 10);
		createLine(TEST_LINE_NAME_2, TEST_LINE_COLOR_2, 2L, 4L, 15);

		// when
		ExtractableResponse<Response> response = getLines();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("name")).contains(TEST_LINE_NAME_1, TEST_LINE_NAME_2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("지하철 노선을 조회한다.")
	@DirtiesContext
	@Test
	void getLineTest() {
		// given
		createLine(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 1L, 2L, 10);

		// when
		ExtractableResponse<Response> response = getLine(1L);

		// then
		assertThat(response.jsonPath().getString("name")).isEqualTo(TEST_LINE_NAME_1);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLineTest() {
		// given
		createLine(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 1L, 2L, 10);

		// when
		ExtractableResponse<Response> response =
				RestAssured.given()
						.body(Map.of(
								"name", TEST_LINE_NAME_2
								,"color", TEST_LINE_COLOR_2))
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
							.put("/lines")
						.then()
							.extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(getLine(1L).jsonPath().getString("name")).isEqualTo(TEST_LINE_NAME_2);
		assertThat(getLine(1L).jsonPath().getString("color")).isEqualTo(TEST_LINE_COLOR_2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철 노선을 삭제한다.")
	@Test
	void deleteLineTest() {
		// given
		createLine(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 1L, 2L, 10);

		// when
		RestAssured.given()
				.when()
					.delete("/lines/{id}", 1L)
				.then()
					.statusCode(HttpStatus.NO_CONTENT.value());

		// then
		assertThat(getLines().jsonPath().getString("name")).doesNotContain(TEST_LINE_NAME_1);
	}

	private ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, int distance) {
		return RestAssured.given()
					.body(Map.of(
						"name", name
						,"color", color
						,"upStationId", upStationId
						,"downStationId", downStationId
						,"distance", distance))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
		            .when()
						.post("/lines")
		            .then()
		                .extract();
	}

	private ExtractableResponse<Response> getLines() {
		return RestAssured.given()
		            .when()
						.get("/lines")
		            .then()
		                .extract();
	}

	private ExtractableResponse<Response> getLine(Long id) {
		return RestAssured.given()
				.when()
				.get("/lines/{id}", id)
				.then()
				.extract();
	}
}
