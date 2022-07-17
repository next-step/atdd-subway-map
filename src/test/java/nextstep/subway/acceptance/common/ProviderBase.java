package nextstep.subway.acceptance.common;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.section.SectionErrorCode;

public abstract class ProviderBase {

	protected static void 응답코드_검증(ExtractableResponse<Response> response, HttpStatus httpStatus) {
		assertThat(response.statusCode()).isEqualTo(httpStatus.value());
	}

	public static SectionErrorCode 에러코드_추출(ExtractableResponse<Response> response) {
		return response.jsonPath().getObject("code", SectionErrorCode.class);
	}
}
