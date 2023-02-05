package subway.api;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.section.SectionCreateRequest;

public class SectionTestApi {

	public ExtractableResponse<Response> createSection(Long lineId, Long newDownStationId, Long registeredUpStationId, int distance) {
		SectionCreateRequest sectionRequest = new SectionCreateRequest(newDownStationId, registeredUpStationId, distance);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(sectionRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines/{id}/sections", lineId)
			.then().log().all()
			.extract();

		return response;
	}

	public ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.param("stationId", stationId)
			.when().delete("/lines/{id}/sections", lineId)
			.then().log().all()
			.extract();

		return response;
	}
}
