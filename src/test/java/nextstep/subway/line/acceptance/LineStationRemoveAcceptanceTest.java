package nextstep.subway.line.acceptance;

import static nextstep.subway.station.acceptance.step.LineStationRemoveAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.acceptance.step.LineStationRemoveAcceptanceStep;
import nextstep.subway.station.dto.StationResponse;

public class LineStationRemoveAcceptanceTest extends AcceptanceTest {

	private ExtractableResponse<Response> createdLineResponse;

	private Long lineId;
	private Long firstStationId;
	private Long secondStationId;
	private Long thirdStationid;
	private Long fourthStationId;

	@BeforeEach
	public void setUp() {
		super.setUp();

		createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
		ExtractableResponse<Response> firstStationResponse = 지하철역_등록되어_있음("강남역");
		ExtractableResponse<Response> secondStationResponse = 지하철역_등록되어_있음("역삼역");
		ExtractableResponse<Response> thirdStationResponse = 지하철역_등록되어_있음("선릉역");
		ExtractableResponse<Response> fourthStationResponse = 지하철역_등록되어_있음("삼성역");

		lineId = createdLineResponse.as(LineResponse.class).getId();
		firstStationId = firstStationResponse.as(StationResponse.class).getId();
		secondStationId = secondStationResponse.as(StationResponse.class).getId();
		thirdStationid = thirdStationResponse.as(StationResponse.class).getId();
		fourthStationId = fourthStationResponse.as(StationResponse.class).getId();

		노선에_지하철역_첫번째_등록(firstStationId, lineId);
		노선에_지하철역_추가로_등록(firstStationId, secondStationId, lineId);
		노선에_지하철역_추가로_등록(secondStationId, thirdStationid, lineId);
	}

	@DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
	@Test
	void 노선의_마지막_역을_삭제한다() {
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, thirdStationid);
		assertThat(request.statusCode()).isEqualTo(HttpStatus.OK.value());
		ExtractableResponse<Response> response = 노선정보_확인_요청(lineId);
		ResponseBodyExtractionOptions body = response.body();
		List<Long> parsedStationIds = JSON_응답을_파싱한다(body);
		assertThat(parsedStationIds.contains(thirdStationid)).isEqualTo(false);
		List<Long> stationIds = Arrays.asList(firstStationId, secondStationId);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_지하철역_순서_정렬됨(response, stationIds))
			.isEqualTo(true);
	}

	@DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
	@Test
	void 노선의_중간_역을_삭제한다() {
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, secondStationId);
		assertThat(request.statusCode()).isEqualTo(HttpStatus.OK.value());
		ExtractableResponse<Response> response = 노선정보_확인_요청(lineId);
		ResponseBodyExtractionOptions body = response.body();
		List<Long> parsedStationIds = JSON_응답을_파싱한다(body);
		assertThat(parsedStationIds.contains(secondStationId)).isEqualTo(false);
		List<Long> stationIds = Arrays.asList(firstStationId, thirdStationid);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_지하철역_순서_정렬됨(createdLineResponse, stationIds))
			.isEqualTo(true);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_삭제된_역이_이전번호로_존재하지_않음(response, secondStationId));
	}

	@DisplayName("지하철 노선에 등록된 첫 번째 지하철역을 제외한다.")
	@Test
	void 노선의_첫번째_역을_삭제한다() {
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, firstStationId);
		assertThat(request.statusCode()).isEqualTo(HttpStatus.OK.value());
		ExtractableResponse<Response> response = 노선정보_확인_요청(lineId);
		ResponseBodyExtractionOptions body = response.body();
		List<Long> parsedStationIds = JSON_응답을_파싱한다(body);
		assertThat(parsedStationIds.contains(firstStationId)).isEqualTo(false);
		List<Long> stationIds = Arrays.asList(secondStationId, thirdStationid);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_지하철역_순서_정렬됨(createdLineResponse, stationIds))
			.isEqualTo(true);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_삭제된_역이_이전번호로_존재하지_않음(response, firstStationId));
	}

	@DisplayName("지하철 노선에 등록되지 않은 역을 삭제하려고 할 경우 예외를 반환한다.")
	@Test
	void 노선에_없는_역을_삭제하려고_하면_오류를_반환한다() {
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, fourthStationId);
		assertThat(request.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
