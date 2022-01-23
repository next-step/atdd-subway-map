package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.apache.http.HttpHeaders.LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

public class AcceptanceTestValidation {

	public void 생성_요청_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header(LOCATION)).isNotEmpty();
	}

	public void 삭제_요청_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
	}

	public void 조회_요청_목록_검증(ExtractableResponse<Response> response, String fieldName, List<String> expectedValues) {
		assertThat(response.statusCode()).isEqualTo(OK.value());
		assertThat(response.jsonPath().getList(fieldName)).containsExactlyInAnyOrderElementsOf(expectedValues);
	}

	public void 조회_요청_단건_검증(ExtractableResponse<Response> response, String fieldName, String expectedValues) {
		assertThat(response.statusCode()).isEqualTo(OK.value());
		assertThat(response.jsonPath().getString(fieldName)).isEqualTo(expectedValues);
	}

	public void 수정_요청_검증(ExtractableResponse<Response> response, String fieldName, String expectedValue) {
		조회_요청_단건_검증(response, fieldName, expectedValue);
	}

	public void 중복_생성_요청_실패_검증(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
	}
}
