package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationAcceptanceStatic.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineAcceptanceStatic {
	public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> param) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.post("/lines")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
		return RestAssured.given().log().all()
			.when()
			.get("/lines")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_조회_요청(long lineId) {
		return RestAssured.given().log().all()
			.when()
			.get("/lines/{id}", lineId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_삭제_요청(long lineId) {
		return RestAssured.given().log().all()
			.when()
			.delete("/lines/{lineId}", lineId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_수정_요청(long lineId, Map<String, Object> param) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.put("/lines/{lineId}", lineId)
			.then().log().all().extract();
	}

	public static Long 지하철_노선_생성되어_있음(String lineName, String lineColor, Long upStationId, Long downStationId, int distance) {
		Map<String, Object> param =
			Map.of("name", lineName, "color", lineColor, "upStationId", upStationId, "downStationId", downStationId, "distance", distance);
		return 지하철_노선_생성_요청(param).jsonPath().getLong("id");
	}

	public static ExtractableResponse<Response> 구간_등록_요청(Long 신분당선_노선_번호, Map<String, Object> param) {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.post("/lines/{lineId}/sections", 신분당선_노선_번호)
			.then().log().all().extract();
		return response;
	}

	public static Long 신분당선_노선_생성되어_있음() {
		Long 정자역_번호 = 지하철역_생성되어_있음(Map.of("name", "정자역"));
		Long 판교역_번호 = 지하철역_생성되어_있음(Map.of("name", "판교역"));
		return 지하철_노선_생성되어_있음("신분당선", "red", 정자역_번호, 판교역_번호, 10);
	}

	public static ExtractableResponse<Response> 구간_삭제_요청(Long lineId, Long stationId) {
		return RestAssured.given().log().all()
			.when()
			.delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
			.then().log().all().extract();
	}

	public static void 하행종점_상행역_불일치_에러가_발생함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("errorMessage")).contains("하행 종점이 추가하려는 구간의 상행역과 일치하지 않습니다");
	}

	public static void 이미_추가된_노선_추가_에러가_발생함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("errorMessage")).contains("추가하려는 구간의 하행이 이미 노선에 추가되어 있습니다.");
	}

	public static void 마지막_구간이_아닌구가_제거_에러가_발생함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("errorMessage")).contains("마지막 구간이 아닙니다.");
	}

	public static void 한개_뿐인_구간삭제_에러가_발생함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("errorMessage")).contains("1개뿐인 구간은 삭제할 수 없습니다.");
	}

}
