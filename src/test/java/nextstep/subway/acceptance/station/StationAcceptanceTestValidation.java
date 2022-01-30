package nextstep.subway.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestValidation;

import static java.util.Arrays.asList;
import static nextstep.subway.acceptance.station.StationAcceptanceTestFixture.역이름_필드;

public class StationAcceptanceTestValidation extends AcceptanceTestValidation {

	public void 역_조회_요청_목록_검증(ExtractableResponse<Response> 목록_조회_응답, String 역_이름, String 새로운_역_이름) {
		조회_요청_목록_검증(목록_조회_응답, 역이름_필드, asList(역_이름, 새로운_역_이름));
	}
}
