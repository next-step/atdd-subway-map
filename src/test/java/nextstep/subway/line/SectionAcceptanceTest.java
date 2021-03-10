package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.NewUpStationIsWrongException;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineSteps.Line.일호선;
import static nextstep.subway.line.LineSteps.지하철_노선_생성실패됨;
import static nextstep.subway.line.LineSteps.지하철_노선_생성요청;
import static nextstep.subway.line.SectionSteps.*;
import static nextstep.subway.line.SectionSteps.지하철노선_구간_등록됨;
import static nextstep.subway.station.StationSteps.Station.*;
import static nextstep.subway.station.StationSteps.지하철역_생성요청;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private int distance;

    private LineRequest defaultLineRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        stationId1 = 지하철역_생성요청(강남역.name).as(StationResponse.class).getId();
        stationId2 = 지하철역_생성요청(역삼역.name).as(StationResponse.class).getId();
        stationId3 = 지하철역_생성요청(선릉역.name).as(StationResponse.class).getId();
        distance = 10;

        defaultLineRequest = new LineRequest(일호선.name, 일호선.color, stationId1, stationId2, distance);
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void addSection() {
        // given
        Long lineId = 지하철_노선_생성됨(defaultLineRequest);

        // when
        SectionRequest request = new SectionRequest(stationId2, stationId3, distance);
        ExtractableResponse<Response> response = 지하철노선_구간_등록요청(lineId, request);

        // then
        지하철노선_구간_등록됨(response);
    }

    @DisplayName("새로운 구간의 상행역이 현재 등록되어 있는 하행 종점역이 아닌경우 실패한다.")
    @Test
    void failNewUpStationIsWrong() {
        // given
        Long lineId = 지하철_노선_생성됨(defaultLineRequest);

        // when
        SectionRequest request = new SectionRequest(stationId1, stationId3, distance);
        ExtractableResponse<Response> response = 지하철노선_구간_등록요청(lineId, request);

        // then
        지하철노선_구간_등록실패됨(response);
    }

    @DisplayName("새로운 구간의 하행역이 현재 등록되어있는 경우 실패한다.")
    @Test
    void failNewDownStationIsAlreadyRegistered() {
        // given
        Long lineId = 지하철_노선_생성됨(defaultLineRequest);

        // when & then
        SectionRequest request = new SectionRequest(stationId2, stationId1, distance);
        ExtractableResponse<Response> response = 지하철노선_구간_등록요청(lineId, request);

        // then
        지하철노선_구간_등록실패됨(response);
    }

    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        Long lineId = 지하철_노선_생성됨(defaultLineRequest);
        지하철노선_구간_등록요청(lineId, new SectionRequest(stationId2, stationId3, distance));

        // when
        ExtractableResponse<Response> response = 지하철노선_구간_제거요청(lineId, stationId3);

        // then
        지하철노선_구간_삭제됨(response);
    }

    @DisplayName("삭제하려는 역이 하행 종점역이 아닌경우 실패한다.")
    @Test
    void failDeleteStationIsNotLastStation() {
        // given
        Long lineId = 지하철_노선_생성됨(defaultLineRequest);
        SectionRequest request = new SectionRequest(stationId2, stationId3, distance);
        지하철노선_구간_등록요청(lineId, request);

        // when
        ExtractableResponse<Response> response = 지하철노선_구간_제거요청(lineId, stationId2);

        // then
        지하철노선_구간_등록실패됨(response);
    }

    @DisplayName("구간이 1개인 경우 삭제시도시 실패한다.")
    @Test
    void failOnlyOneSectionLeft() {
        // given
        Long lineId = 지하철_노선_생성됨(defaultLineRequest);

        // when
        ExtractableResponse<Response> response = 지하철노선_구간_제거요청(lineId, stationId2);

        // then
        지하철노선_구간_등록실패됨(response);
    }
}
