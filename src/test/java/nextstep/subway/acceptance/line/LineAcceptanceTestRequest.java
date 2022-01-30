package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestRequest;
import nextstep.subway.utils.RequestBodyBuilder;

import java.util.Map;

import static nextstep.subway.acceptance.line.LineAcceptanceTestFixture.*;

public class LineAcceptanceTestRequest extends AcceptanceTestRequest {

	public static final String 요청_주소 = "/lines";

	public LineAcceptanceTestRequest() {
		super(요청_주소, 노선_아이디_필드);
	}

	public ExtractableResponse<Response> 노선_생성_요청(String 노선이름, String 노선색상, long 상행역_종점_아이디, long 하행역_종점_아이디, int 종점역_간_거리) {
		return 생성_요청(RequestBodyBuilder.builder()
				.put(노선이름_필드, 노선이름)
				.put(노선색상_필드, 노선색상)
				.put(상행역_종점_아이디_필드, 상행역_종점_아이디)
				.put(하행역_종점_아이디_필드, 하행역_종점_아이디)
				.put(종점역_간_거리_필드, 종점역_간_거리)
				.build());
	}

	public ExtractableResponse<Response> 노선_생성_요청(LineAcceptanceTestParameter 노선) {
		return 생성_요청(RequestBodyBuilder.builder()
				.put(노선이름_필드, 노선.노선이름)
				.put(노선색상_필드, 노선.노선색상)
				.build());
	}

	public long 노선_생성_요청_아이디_반환(LineAcceptanceTestParameter 노선) {
		return 아이디_추출(노선_생성_요청(노선));
	}

	public Map<String, String> 요청_본문_생성(String 노선이름, String 노선색상) {
		return RequestBodyBuilder.builder()
				.put(노선이름_필드, 노선이름)
				.put(노선색상_필드, 노선색상)
				.build();
	}

	public ExtractableResponse<Response> 노선_수정_요청(ExtractableResponse<Response> 생성_응답, LineAcceptanceTestParameter 노선) {
		return 수정_요청(아이디_추출(생성_응답), 요청_본문_생성(노선.노선이름, 노선.노선색상));
	}

	public ExtractableResponse<Response> 노선_생성_요청(LineAcceptanceTestParameter 노선, long 상행_종점역_아이디, long 하행_종점역_아이디) {
		return 노선_생성_요청(노선.노선이름, 노선.노선색상, 상행_종점역_아이디, 하행_종점역_아이디, 노선.종점역_간_거리);
	}

	public long 노선_생성_요청_아이디_반환(LineAcceptanceTestParameter 노선, long 상행_종점역_아이디, long 하행_종점역_아이디) {
		return 아이디_추출(노선_생성_요청(노선.노선이름, 노선.노선색상, 상행_종점역_아이디, 하행_종점역_아이디, 노선.종점역_간_거리));
	}
}
