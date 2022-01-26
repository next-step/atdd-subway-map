package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestValidation;
import nextstep.subway.utils.RequestBodyBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static nextstep.subway.acceptance.line.LineAcceptanceTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTestValidation extends AcceptanceTestValidation {
	private Map<String, String> 노선_요청_본문_생성(String 노선이름, String 노선색상, String 상행역_종점_아이디, String 하행역_종점_아이디, int 종점간_거리) {
		return RequestBodyBuilder.builder()
				.put(노선이름_필드, 노선이름)
				.put(노선색상_필드, 노선색상)
				.put(상행역_종점_아이디_필드, 상행역_종점_아이디)
				.put(하행역_종점_아이디_필드, 하행역_종점_아이디)
				.put(종점역_간_거리_필드, 종점간_거리)
				.build();
	}

	public void 노선_조회_요청_목록_검증(ExtractableResponse<Response> 목록_조회_응답, String... 노선이름) {
		조회_요청_목록_검증(목록_조회_응답, 노선이름_필드, Arrays.stream(노선이름).collect(Collectors.toList()));
	}

	public void 노선_조회_요청_단건_검증(ExtractableResponse<Response> 단건조회_응답, String 노선이름) {
		조회_요청_단건_검증(단건조회_응답, 노선이름_필드, 노선이름);
	}

	public void 노선_수정_요청_검증(ExtractableResponse<Response> 수정_응답, LineAcceptanceTestParameter 수정된_노선) {
		수정_요청_검증(수정_응답, 노선이름_필드, 수정된_노선.노선이름);
		수정_요청_검증(수정_응답, 노선색상_필드, 수정된_노선.노선색상);
	}

	public void 역_순서_검증(ExtractableResponse<Response> 요청_응답, String 상행_종점역, String 하행_종점역, String 하행역) {
		final List<String> list = 요청_응답.jsonPath().get(getStationNameJsonPath());
		assertThat(list).containsExactly(상행_종점역, 하행_종점역, 하행역);
	}

	private String getStationNameJsonPath() {
		return format("%s.%s.flatten()", 역_목록_필드, 역_이름_필드);
	}
}
