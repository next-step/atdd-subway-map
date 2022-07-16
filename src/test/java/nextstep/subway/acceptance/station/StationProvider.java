package nextstep.subway.acceptance.station;

import static nextstep.subway.acceptance.station.StationRestAssuredProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationProvider {

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

	public static ExtractableResponse<Response> 지하철역_제거_성공(String id) {
		ExtractableResponse<Response> response = 지하철역_제거(id);
		응답코드_검증(response, NO_CONTENT);
		return response;
	}

	private static void 응답코드_검증(ExtractableResponse<Response> response, HttpStatus httpStatus) {
		assertThat(response.statusCode()).isEqualTo(httpStatus.value());
	}
}
