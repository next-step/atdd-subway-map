package nextstep.subway.line.acceptance;

import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

public class LineStationRemoveAcceptanceTest extends AcceptanceTest {

	private ExtractableResponse<Response> createdLineResponse;

	private ExtractableResponse<Response> firstStationResponse;
	private ExtractableResponse<Response> secondStationResponse;
	private ExtractableResponse<Response> thirdStationResponse;

	private ExtractableResponse<Response> firstLineStationResponse;
	private ExtractableResponse<Response> secondLineStationResponse;
	private ExtractableResponse<Response> thirdLineStationResponse;

	private Long lineId;
	private Long firstStationId;
	private Long secondStationId;
	private Long thirdStationid;

	@BeforeEach
	public void setUp() {
		super.setUp();

		createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
		firstStationResponse = 지하철역_등록되어_있음("강남역");
		secondStationResponse = 지하철역_등록되어_있음("역삼역");
		thirdStationResponse = 지하철역_등록되어_있음("선릉역");

		lineId = createdLineResponse.as(LineResponse.class).getId();
		firstStationId = firstStationResponse.as(StationResponse.class).getId();
		secondStationId = secondStationResponse.as(StationResponse.class).getId();
		thirdStationid = thirdStationResponse.as(StationResponse.class).getId();

		firstLineStationResponse = 노선에_지하철역_첫번째_등록(firstStationId, lineId);
		secondLineStationResponse = 노선에_지하철역_추가로_등록(firstStationId, secondStationId, lineId);
		thirdLineStationResponse = 노선에_지하철역_추가로_등록(secondStationId, thirdStationid, lineId);
	}

	@DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
	@Test
	void 노선의_마지막_역을_삭제한다() {
		// when
		// 지하철 노선의 마지막에 지하철역 제외 요청

		// then
		// 지하철 노선에 지하철역 제외됨
		// TODO: 컨트롤러 단에서는 200 반환이 되는 것으로만 믿으면 충분한지 궁금하다.

		// when
		// 지하철 노선 상세정보 조회 요청

		// then
		// 지하철 노선에 지하철역 제외 확인됨

		// and
		// 지하철 노선에 지하철역 순서 정렬됨
	}

	@DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
	@Test
	void 노선의_중간_역을_삭제한다() {
		//
	}

	@DisplayName("지하철 노선에 등록된 첫 번째 지하철역을 제외한다.")
	@Test
	void 노선의_첫번째_역을_삭제한다() {
		//
	}

	@DisplayName("지하철 노선에 등록되지 않은 역을 삭제하려고 할 경우 예외를 반환한다.")
	@Test
	void 노선에_없는_역을_삭제하려고_하면_오류를_반환한다() {
		//
	}
}
