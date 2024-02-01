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
		// when
		StationApi.createStation(StationFixture.강남역_생성_요청());
		StationApi.createStation(StationFixture.압구정역_생성_요청());
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
}
