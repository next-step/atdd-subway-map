package nextstep.subway.acceptance.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionUtils {

	public static ExtractableResponse<Response> 지하철_구간을_등록한다(Long subwayLineId, Long upStationId, Long downStationId, Integer distance) {
		Map<String, Object> params = new HashMap<>();
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured
				.given()
					.log().all()
					.body(params)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.post("/lines/{subwayLineId}/sections", subwayLineId)
				.then()
					.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철_구간을_삭제한다(long subwayLineId, long stationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("subwayLineId", subwayLineId);
		params.put("sectionId", stationId);

		return RestAssured
				.given()
					.log().all()
					.body(params)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.delete("/lines/{subwayLineId}/sections?stationId={sectionId}", subwayLineId, stationId)
				.then()
					.log().all()
				.extract();
	}
}
