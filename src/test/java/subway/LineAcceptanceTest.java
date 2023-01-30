package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() {
		//when
		Map<String, String> station1 = new HashMap<>();
		station1.put("name", "지하철역");
		createStationResponse(station1);

		Map<String, String> station2 = new HashMap<>();
		station2.put("name", "새로운 지하철역");
		createStationResponse(station2);

		Map<String, Object> lineParams = new HashMap<>();
		lineParams.put("name", "신분당선");
		lineParams.put("color", "bg-red-600");
		lineParams.put("upStationId", 1);
		lineParams.put("downStationId", 2);
		lineParams.put("distance", 10);

		ExtractableResponse<Response> createResponse = createLineResponse(lineParams);

		// then
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		ExtractableResponse<Response> response = getLineResponse();
		List<String> lineNames = response.jsonPath().getList("name", String.class);

		assertAll(
				() -> assertThat(lineNames).containsAnyOf("신분당선")
		);
	}

	private ExtractableResponse<Response> getLineResponse() {
		return RestAssured.given().log().all()
				.when().get("/lines")
				.then().log().all().extract();
	}

	private ExtractableResponse<Response> createLineResponse(Map<String, Object> params) {
		return RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(params)
				.when().post("/lines")
				.then().log().all().extract();
	}

	private static ExtractableResponse<Response> createStationResponse(Map<String, String> param) {
		return RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(param)
				.when().post("/stations")
				.then().log().all().extract();
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	//TODO: 지하철노선 목록 조회

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	//TODO: 지하철노선 조회

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	//TODO: 지하철노선 수정

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	//TODO: 지하철노선 삭제

}
