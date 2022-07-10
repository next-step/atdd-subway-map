package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 노선 인수테스트")
class StationLineAcceptanceTest {

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회시 생성한 노선을 찾을수 있다.
	 */
	@DisplayName("지하철 노선 생성 테스트")
	@Test
	void createStationLintTest() {

		//given
		long upStationId = 지하철_생성_요청(Map.of("name", "판교역")).jsonPath().getLong("id");
		long downStationId = 지하철_생성_요청(Map.of("name", "정자역")).jsonPath().getLong("id");

		Map<String, Object> param =
			Map.of("name", "신분당선", "color", "bg-red-600", "upStationId", upStationId, "downStationId", downStationId, "distance", 10);

		//when
		ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(param);
		ExtractableResponse<Response> getLineResponse = 지하철_노선_목록_조회();

		//then
		assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(getLineResponse.jsonPath().getList("$..id")).containsAll(List.of(upStationId, downStationId));
	}

	private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> param) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.post("/lines")
			.then().log().all().extract();
	}

	private ExtractableResponse<Response> 지하철_노선_목록_조회() {
		return RestAssured.given().log().all()
			.when()
			.get("/lines")
			.then().log().all().extract();
	}

}
