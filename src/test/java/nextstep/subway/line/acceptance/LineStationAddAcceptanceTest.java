package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.common.step.CommonAcceptanceStep.API_응답코드_검사;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선을_조회한다;
import static nextstep.subway.line.acceptance.step.LineStationAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {

    private ExtractableResponse<Response> 지하철2호선;
    private ExtractableResponse<Response> 강남역;
    private ExtractableResponse<Response> 역삼역;
    private ExtractableResponse<Response> 선릉역;

    @BeforeEach
    public void background() {
        // given
        this.지하철2호선 = 지하철_노선_등록되어_있음("2호선", "GREEN");
        this.강남역 = 지하철역_등록되어_있음("강남역");
        this.역삼역 = 지하철역_등록되어_있음("역삼역");
        this.선릉역 = 지하철역_등록되어_있음("선릉역");
    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = 지하철2호선.as(LineResponse.class).getId();
        Long stationId = 강남역.as(StationResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선에_지하철역을_등록한다(lineId, stationId, null);

        // then
        API_응답코드_검사(response.statusCode(), HttpStatus.CREATED);
    }

    @DisplayName("이미 등록되어 있던 역을 등록한다.")
    @Test
    void addDuplicateLineStation() {
        // when
        Long lineId = 지하철2호선.as(LineResponse.class).getId();
        StationResponse stationResponse = 강남역.as(StationResponse.class);
        지하철_노선에_지하철역을_등록한다(lineId, stationResponse.getId(), null);
        ExtractableResponse<Response> duplicateLineStationResponse = 지하철_노선에_지하철역을_등록한다(lineId, stationResponse.getId(), null);

        // then
        API_응답코드_검사(duplicateLineStationResponse.statusCode(), HttpStatus.CONFLICT);
    }


    @DisplayName("존재하지 않는 역을 노선에 등록한다.")
    @Test
    void addInvalidLineStation() {
        // when
        Long lineId = 지하철2호선.as(LineResponse.class).getId();
        long invalidStationId = 11111L;
        ExtractableResponse<Response> invalidStationResponse = 지하철_노선에_지하철역을_등록한다(lineId, invalidStationId, null);

        // then
        API_응답코드_검사(invalidStationResponse.statusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = 지하철2호선.as(LineResponse.class).getId();
        Long stationId = 강남역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId, null);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선을_조회한다(lineId);

        // then
        API_응답코드_검사(response.statusCode(), HttpStatus.OK);
        LineResponse lineResponse = response.as(LineResponse.class);
        지하철_노선을_조회하는_요청이_성공(lineResponse);
        지하철_노선의_역_갯수가_n개이다(lineResponse, 1);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = 지하철2호선.as(LineResponse.class).getId();
        Long stationId1 = 강남역.as(StationResponse.class).getId();
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역을_등록한다(lineId, stationId1, null);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = 역삼역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId2, stationId1);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = 선릉역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId3, stationId2);

        // then
        API_응답코드_검사(lineStationResponse.statusCode(), HttpStatus.CREATED);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선을_조회한다(lineId);

        // then
        LineResponse lineResponse = response.as(LineResponse.class);
        지하철_노선을_조회하는_요청이_성공(lineResponse);
        지하철_노선의_역_갯수가_n개이다(lineResponse, 3);
        지하철_노선이_정렬되어있다(lineResponse, Lists.newArrayList(1L, 2L, 3L));
    }



    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = 지하철2호선.as(LineResponse.class).getId();
        Long stationId1 = 강남역.as(StationResponse.class).getId();
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역을_등록한다(lineId, stationId1, null);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = 역삼역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId2, stationId1);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = 선릉역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId3, stationId1);

        // then
        API_응답코드_검사(lineStationResponse.statusCode(), HttpStatus.CREATED);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선을_조회한다(lineId);

        LineResponse lineResponse = response.as(LineResponse.class);
        지하철_노선을_조회하는_요청이_성공(lineResponse);
        지하철_노선의_역_갯수가_n개이다(lineResponse, 3);
        지하철_노선이_정렬되어있다(lineResponse, Lists.newArrayList(1L, 3L, 2L));
    }

    @DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
    @Test
    public void excludeLineStationAtLast() {
        // given
        // 지하철 노선에 지하철역이 등록되어 있다
        Long lineId = 지하철2호선.as(LineResponse.class).getId();

        // 지하철_노선에_지하철역_등록_요청
        Long stationId1 = 강남역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId1, null);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = 역삼역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId2, stationId1);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = 선릉역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId3, stationId2);

        // when
        // 지하철 노선의 마지막에 지하철역 제외 요청
        ExtractableResponse<Response> deleteLineStationResponse = 지하철_노선에_지하철역을_제외한다(lineId, stationId3);

        // then
        // 지하철 노선에 지하철역 제외됨
        API_응답코드_검사(deleteLineStationResponse.statusCode(), HttpStatus.OK);

        // when
        // 지하철 노선 상세정보 조회 요청
        ExtractableResponse<Response> getLineResponse = 지하철_노선을_조회한다(lineId);

        //then
        // 지하철 노선에 지하철역 제외 확인됨
        API_응답코드_검사(getLineResponse.statusCode(), HttpStatus.OK);
        LineResponse existLine = getLineResponse.as(LineResponse.class);

        지하철_노선의_역_갯수가_n개이다(existLine, 2);
        지하철_노선에_역이_포함되지_않는다(existLine, stationId3);

        // 지하철 노선에 지하철역 순서 정렬됨
        지하철_노선이_정렬되어있다(existLine, Lists.newArrayList(1L, 2L));
    }

    @DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
    @Test
    public void excludeLineStationInTheMiddle() {
        // given
        // 지하철 노선에 지하철역이 등록되어 있다
        Long lineId = 지하철2호선.as(LineResponse.class).getId();

        // 지하철_노선에_지하철역_등록_요청
        Long stationId1 = 강남역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId1, null);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = 역삼역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId2, stationId1);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = 선릉역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId3, stationId2);

        // when
        // 지하철 노선의 중간 지하철역 제외 요청
        ExtractableResponse<Response> deleteLineStationResponse = 지하철_노선에_지하철역을_제외한다(lineId, stationId2);

        // then
        // 지하철 노선에 지하철역 제외됨
        API_응답코드_검사(deleteLineStationResponse.statusCode(), HttpStatus.OK);

        // when
        // 지하철 노선 상세정보 조회 요청
        ExtractableResponse<Response> getLineResponse = 지하철_노선을_조회한다(lineId);

        //then
        // 지하철 노선에 지하철역 제외 확인됨
        LineResponse existLine = getLineResponse.as(LineResponse.class);

        지하철_노선의_역_갯수가_n개이다(existLine, 2);
        지하철_노선에_역이_포함되지_않는다(existLine, stationId3);

        // 지하철 노선에 지하철역 순서 정렬됨
        지하철_노선이_정렬되어있다(existLine, Lists.newArrayList(1L, 3L));
    }

    @DisplayName("지하철 노선에서 등록되지 않는 역을 제외한다.")
    @Test
    public void excludeNoTExistLineStation() {
        // given
        // 지하철 노선에 지하철역이 등록되어 있다
        Long lineId = 지하철2호선.as(LineResponse.class).getId();

        // 지하철_노선에_지하철역_등록_요청
        Long stationId1 = 강남역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId1, null);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = 역삼역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId2, stationId1);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = 선릉역.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId3, stationId2);

        // when
        // 지하철 노선에 등록되지 않은 역 제외 요청
        ExtractableResponse<Response> deleteLineStationResponse = 지하철_노선에_지하철역을_제외한다(lineId, 999L);

        // then
        // 지하철 노선에 지하철역 제외 실패됨
        API_응답코드_검사(deleteLineStationResponse.statusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
