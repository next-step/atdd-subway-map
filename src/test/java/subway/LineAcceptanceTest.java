package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.create.LineCreator;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {
	@LocalServerPort
	private int port;

	@BeforeEach
	public void beforeEach() {
		RestAssured.port = port;
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void saveLine() {
		// when
		ExtractableResponse<Response> savedLine = LineCreator.of()
			.build()
			.actionReturnExtractableResponse();
		LineResponse actualResponse = savedLine.jsonPath().getObject("", LineResponse.class);

		String uri = String.format("/%s/%s", "lines", actualResponse.getId());
		LineResponse expectedResponse = RestAssured
			.given().log().all()
			.when().get(uri)
			.then().log().all()
			.extract().jsonPath().getObject("", LineResponse.class);

		// then
		assertThat(savedLine.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선 2개를 생성하고, 생성된 목록을 조회한다.")
	@Test
	void createLineAndRetrieveLines() {
		// given
		LineCreator lineCreator1 = LineCreator.of().build();
		LineCreator lineCreator2 = LineCreator.of().upStationName("남강역").downStationName("재양역").build();
		LineResponse line1 = lineCreator1.actionReturnLineResponse();
		LineResponse line2 = lineCreator2.actionReturnLineResponse();

		// when
		ExtractableResponse<Response> extractableResponse = RestAssured
			.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.extract();

		List<LineResponse> actualResponse = extractableResponse.jsonPath().getList("", LineResponse.class);

		// then
		assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(actualResponse.get(0)).usingRecursiveComparison().isEqualTo(line1);
		assertThat(actualResponse.get(1)).usingRecursiveComparison().isEqualTo(line2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("생성된 노선을 조회한다.")
	@Test
	void createLineAndRetrieveLine() {
		// given
		LineResponse expectedResponse = LineCreator.of()
			.build()
			.actionReturnLineResponse();

		// when
		String uri = String.format("/%s/%s", "lines", expectedResponse.getId());
		ExtractableResponse<Response> extractableResponse = RestAssured
			.given().log().all()
			.when().get(uri)
			.then().log().all()
			.extract();

		LineResponse actualResponse = extractableResponse.jsonPath().getObject("", LineResponse.class);

		// then
		assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 생성하고, 생성된 노선을 수정한다.")
	@Test
	void updateLine() {
		// given
		String expectedName = "변경된 이름";
		String expectedColor = "변경된 색깔";
		LineResponse lineResponse = LineCreator.of()
			.build()
			.actionReturnLineResponse();

		// when
		LineUpdateRequest request = new LineUpdateRequest(expectedName, expectedColor);

		String uri = String.format("/%s/%s", "lines", lineResponse.getId());
		ExtractableResponse<Response> extractableResponse =
			RestAssured
				.given().log().all()
				.body(request)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().put(uri)
				.then().log().all()
				.extract();

		LineResponse actualResponse = RestAssured
			.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get(uri)
			.then().log().all()
			.extract().jsonPath().getObject("", LineResponse.class);

		// then
		assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(actualResponse.getName()).isEqualTo(expectedName);
		assertThat(actualResponse.getColor()).isEqualTo(expectedColor);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철 노선을 생성하고, 삭제한다.")
	@Test
	void deleteLine() {
		// given
		LineResponse lineResponse = LineCreator.of()
			.build()
			.actionReturnLineResponse();

		// when
		String uri = String.format("/%s/%s", "lines", lineResponse.getId());
		ExtractableResponse<Response> extractableResponse =
			RestAssured
				.given().log().all()
				.when().delete(uri)
				.then().log().all()
				.extract();

		// then
		assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
