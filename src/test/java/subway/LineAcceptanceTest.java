package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.fixture.LineFixture;
import subway.fixture.StationApi;
import subway.fixture.StationFixture;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
	/** When 지하철 노선을 생성하면 Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void create_line() {
		// given
		StationApi.createStation(StationFixture.강남역_생성_요청());
		StationApi.createStation(StationFixture.압구정역_생성_요청());

		// when
		ExtractableResponse<Response> response = createLine(LineFixture.신분당선_생성());

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		ResponseBodyExtractionOptions responseBody = response.body();
		assertThat(responseBody.jsonPath().getString("name"))
				.isEqualTo(LineFixture.신분당선_생성().getName());
		assertThat(responseBody.jsonPath().getString("color"))
				.isEqualTo(LineFixture.신분당선_생성().getColor());
		assertThat(responseBody.jsonPath().getList("stations")).hasSize(2);
	}

	/** Given 2개의 지하철 노선을 생성하고 When 지하철 노선 목록을 조회하면 Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다. */
	@DisplayName("지하철 노선의 목록을 조회한다.")
	@Test
	void get_lines() {
		// given
		StationApi.createStation(StationFixture.강남역_생성_요청());
		StationApi.createStation(StationFixture.압구정역_생성_요청());
		createLine(LineFixture.신분당선_생성()).body();

		StationApi.createStation(StationFixture.강남역_생성_요청());
		StationApi.createStation(StationFixture.합정역_생성_요청());
		createLine(LineFixture.분당선_생성()).body();

		// when
		ExtractableResponse<Response> response = getLines();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		ResponseBodyExtractionOptions responseBody = response.body();
		assertThat(responseBody.jsonPath().getList("id").size()).isEqualTo(2);
		assertThat(responseBody.jsonPath().getList("name"))
				.contains(LineFixture.신분당선_생성().getName(), LineFixture.분당선_생성().getName());
		assertThat(responseBody.jsonPath().getList("color"))
				.contains(LineFixture.신분당선_생성().getColor(), LineFixture.분당선_생성().getColor());
		assertThat(responseBody.jsonPath().getList("stations").size()).isEqualTo(2);
	}

	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void get_line() {
		// given
		StationApi.createStation(StationFixture.강남역_생성_요청());
		StationApi.createStation(StationFixture.압구정역_생성_요청());
		Long lineId = createLine(LineFixture.신분당선_생성()).body().jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> response = getLine(lineId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		ResponseBodyExtractionOptions responseBody = response.body();
		assertThat(responseBody.jsonPath().getLong("id")).isEqualTo(lineId);
		assertThat(responseBody.jsonPath().getString("color"))
				.isEqualTo(LineFixture.신분당선_생성().getColor());
		assertThat(responseBody.jsonPath().getList("stations.id").size()).isEqualTo(2);
		assertThat(responseBody.jsonPath().getList("stations.id"))
				.contains(
						LineFixture.신분당선_생성().getUpStationId().intValue(),
						LineFixture.신분당선_생성().getDownStationId().intValue());
	}

	private ExtractableResponse<Response> createLine(LineRequest request) {
		return RestAssured.given()
				.log()
				.all()
				.body(request)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/lines")
				.then()
				.log()
				.all()
				.extract();
	}

	private ExtractableResponse<Response> getLines() {
		return RestAssured.given()
				.log()
				.all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.get("/lines")
				.then()
				.log()
				.all()
				.extract();
	}

	private ExtractableResponse<Response> getLine(Long lineId) {
		return RestAssured.given()
				.log()
				.all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.get("/lines/" + lineId)
				.then()
				.log()
				.all()
				.extract();
	}
}
