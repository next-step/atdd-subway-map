package nextstep.subway.acceptance.station;

import static nextstep.subway.acceptance.station.StationRestAssuredProvider.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.ProviderBase;

public class StationProvider extends ProviderBase {

	public static ExtractableResponse<Response> 지하철역_생성_성공(String 역이름) {
		ExtractableResponse<Response> response = 지하철역_생성(역이름);
		응답코드_검증(response, CREATED);
		return response;
	}

	public static ExtractableResponse<Response> 지하철역_목록_조회_성공() {
		ExtractableResponse<Response> response = 지하철역_목록_조회();
		응답코드_검증(response, OK);
		return response;
	}

	public static void 지하철역_제거_성공(String id) {
		ExtractableResponse<Response> response = 지하철역_제거(id);
		응답코드_검증(response, NO_CONTENT);
	}

	public static String 지하철역_Id_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getString("id");
	}
}
