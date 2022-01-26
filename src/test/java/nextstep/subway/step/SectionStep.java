package nextstep.subway.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.ui.path.SubwayPath;
import org.springframework.http.MediaType;

public class SectionStep {

	public static ExtractableResponse<Response> saveSection(
					final Long upStationId,
					final Long downStationId,
					final int distance,
					final Long lineId
	) {
		SectionRequest request = request(upStationId, downStationId, distance);

		return RestAssured
						.given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
						.when().post(SubwayPath.LINES + SubwayPath.ID + SubwayPath.SECTIONS, lineId)
						.then().log().all()
						.extract();
	}

	public static ExtractableResponse<Response> deleteSection(final Long lineId, final Long sectionId) {

		return RestAssured
						.given().queryParam("stationId",sectionId).log().all()
						.when().delete(SubwayPath.LINES  + SubwayPath.ID + SubwayPath.SECTIONS, lineId)
						.then().log().all()
						.extract();
	}

	public static SectionRequest request(final Long upStationId, final Long downStationId, final int distance) {

		return new SectionRequest(upStationId, downStationId, distance);
	}
}
