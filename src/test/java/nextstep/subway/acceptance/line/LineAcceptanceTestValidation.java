package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestValidation;
import nextstep.subway.utils.RequestBodyBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.line.LineAcceptanceTestFixture.*;

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
}
