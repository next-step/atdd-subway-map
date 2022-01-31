package nextstep.subway.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;

public class SectionSteps {
	public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long id, SectionRequest sectionRequest) {
		return RestAssured.given().log().all()
				.body(sectionRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("lines/{id}/sections", id)
				.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long id, Long stationId) {
		return RestAssured.given().log().all()
			.queryParam("stationId", stationId)
			.when()
			.delete("lines/{id}/sections", id)
			.then().log().all()
			.extract();
	}

	// public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long id) {
	// }
}
