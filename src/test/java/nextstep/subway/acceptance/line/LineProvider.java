package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.LineRestAssuredProvider.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.ProviderBase;

public class LineProvider extends ProviderBase {

	public static ExtractableResponse<Response> 지하철_노선_생성_성공(String name, String color, String upStationId,
		String downStationId, int distance) {
		ExtractableResponse<Response> response = 지하철_노선_등록(name, color, upStationId, downStationId,
			String.valueOf(distance));
		응답코드_검증(response, CREATED);
		return response;
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회_성공() {
		ExtractableResponse<Response> response = 지하철_노선_목록_조회();
		응답코드_검증(response, OK);
		return response;
	}

	public static ExtractableResponse<Response> 지하철_노선_조회_성공(String id) {
		ExtractableResponse<Response> response = 지하철_노선_조회(id);
		응답코드_검증(response, OK);
		return response;
	}

	public static void 지하철_노선_수정_성공(String id, String name, String color) {
		ExtractableResponse<Response> response = 지하철_노선_수정(id, name, color);
		응답코드_검증(response, OK);
	}

	public static void 지하철_노선_제거_성공(String id) {
		ExtractableResponse<Response> response = 지하철_노선_제거(id);
		응답코드_검증(response, NO_CONTENT);
	}

	public static String 지하철_노선_Id_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getString("id");
	}

	public static String 노선_이름_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getString("name");
	}

	public static List<String> 노선_이름_목록_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getList("name", String.class);
	}

	public static String 노선_COLOR_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getString("color");
	}

	public static String 하행_종점역_Id_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getString("$.stations[-1:0].id");
	}

}
