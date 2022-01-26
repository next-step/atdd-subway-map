package nextstep.subway.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestRequest;
import nextstep.subway.utils.RequestBodyBuilder;

import static nextstep.subway.acceptance.section.SectionAcceptanceTestFixture.*;

public class SectionAcceptanceTestRequest extends AcceptanceTestRequest {

	protected SectionAcceptanceTestRequest() {
		super(요청_주소, 구간_아이디_필드);
	}

	public long 구간_생성_요청_아이디_반환(long 노선_아이디, SectionAcceptanceTestParameter 구간, long 구간_상행역_아이디, long 구간_하행역_아이디) {
		return 아이디_추출(구간_생성_요청(노선_아이디, 구간, 구간_상행역_아이디, 구간_하행역_아이디));
	}

	public ExtractableResponse<Response> 구간_생성_요청(long 노선_아이디, SectionAcceptanceTestParameter 구간, long 구간_상행역_아이디, long 구간_하행역_아이디) {
		return 생성_요청(요청_주소_생성(노선_아이디),
				RequestBodyBuilder.builder()
						.put(상행역_아이디_필드, 구간_상행역_아이디)
						.put(하행역_아이디_필드, 구간_하행역_아이디)
						.put(구간_거리_필드, 구간.구간_거리)
						.build()
		);
	}

	private String 요청_주소_생성(long 노선_아이디) {
		return String.format(요청_주소, 노선_아이디);
	}

	public ExtractableResponse<Response> 구간_삭제_요청(long 노선_아이디, long 구간_하행역_아이디) {
		return 삭제_요청(구간_삭제_요청_주소_생성(노선_아이디, 구간_하행역_아이디));
	}

	private String 구간_삭제_요청_주소_생성(long 노선_아이디, long 구간_하행역_아이디) {
		return 요청_주소_생성(노선_아이디) + "?stationId=" + 구간_하행역_아이디;
	}
}
