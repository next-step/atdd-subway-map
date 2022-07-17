package nextstep.subway.acceptance.section;

import static nextstep.subway.acceptance.section.SectionRestAssuredProvider.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.ProviderBase;

public class SectionProvider extends ProviderBase {

	public static ExtractableResponse<Response> 지하철_구간_추가_성공(String lineId, String upStationId, String downStationId,
		int distance) {
		ExtractableResponse<Response> response = 지하철_구간_추가(lineId, upStationId, downStationId, distance);
		응답코드_검증(response, CREATED);
		return response;
	}

	public static void 지하철_노선_제거_성공(Long lineId, Long stationId) {
		ExtractableResponse<Response> response = 지하철_구간_삭제(lineId, stationId);
		응답코드_검증(response, NO_CONTENT);
	}
}
