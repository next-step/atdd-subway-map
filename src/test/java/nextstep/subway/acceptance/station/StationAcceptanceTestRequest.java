package nextstep.subway.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestRequest;
import nextstep.subway.utils.RequestBodyBuilder;

import java.util.Map;

import static nextstep.subway.acceptance.station.StationAcceptanceTestFixture.역_요청_주소;
import static nextstep.subway.acceptance.station.StationAcceptanceTestFixture.역이름_필드;

public class StationAcceptanceTestRequest extends AcceptanceTestRequest {

	static final String 역아이디_필드 = "id";

	public StationAcceptanceTestRequest() {
		super(역_요청_주소, 역아이디_필드);
	}

	public long 역_생성요청_아이디_반환(String 역_이름) {
		return 아이디_추출(역_생성요청(역_이름));
	}

	public ExtractableResponse<Response> 역_생성요청(String 역_이름) {
		return 생성_요청(역_요청_본문_생성(역_이름));
	}

	private Map<String, String> 역_요청_본문_생성(String 역_이름) {
		return RequestBodyBuilder.builder()
				.put(역이름_필드, 역_이름)
				.build();
	}
}
