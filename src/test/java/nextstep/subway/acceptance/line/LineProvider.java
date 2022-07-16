package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.LineRestAssuredProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineProvider {

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

	public static ExtractableResponse<Response> 지하철_노선_수정_성공(String id, String name, String color) {
		ExtractableResponse<Response> response = 지하철_노선_수정(id, name, color);
		응답코드_검증(response, OK);
		return response;
	}

	public static void 지하철_노선_제거_성공(String id) {
		ExtractableResponse<Response> response = 지하철_노선_제거(id);
		응답코드_검증(response, NO_CONTENT);
	}

	private static void 응답코드_검증(ExtractableResponse<Response> response, HttpStatus httpStatus) {
		assertThat(response.statusCode()).isEqualTo(httpStatus.value());
	}

	public static String 지하철_노선_Id_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getString("id");
	}

	public static List<String> 노선_이름_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getList("name", String.class);
	}

	public static List<String> 노선_COLOR_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getList("color", String.class);
	}

}
