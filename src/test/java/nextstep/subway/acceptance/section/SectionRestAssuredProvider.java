package nextstep.subway.acceptance.section;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class SectionRestAssuredProvider {

	public static ExtractableResponse<Response> 지하철_구간_추가(String lineId, String upStationId, String downStationId,
		int distance) {
		Map<String, Object> params = new HashMap<>();
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines/{lineId}/sections", lineId)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_구간_삭제(Long lineId, Long stationId) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("stationId", stationId)
			.when().delete("/lines/{id}/sections", lineId)
			.then().log().all()
			.extract();
	}
}
