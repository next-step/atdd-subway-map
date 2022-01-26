package nextstep.subway.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestValidation;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTestValidation extends AcceptanceTestValidation {

	public void 구간_생성_요청_검증(ExtractableResponse<Response> 구간_생성_응답, SectionAcceptanceTestParameter 구간, long 하행역_종점_아이디, long 구간_하행역_아이디) {
		생성_요청_검증(구간_생성_응답);
		assertThat(구간_생성_응답.jsonPath().getLong(SectionAcceptanceTestFixture.상행역_아이디_필드)).isEqualTo(하행역_종점_아이디);
		assertThat(구간_생성_응답.jsonPath().getLong(SectionAcceptanceTestFixture.하행역_아이디_필드)).isEqualTo(구간_하행역_아이디);
		assertThat(구간_생성_응답.jsonPath().getInt(SectionAcceptanceTestFixture.구간_거리_필드)).isEqualTo(구간.구간_거리);
	}

}
