package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.LineSteps.Line.일호선;
import static nextstep.subway.line.SectionSteps.*;
import static nextstep.subway.station.StationSteps.Station.*;
import static nextstep.subway.station.StationSteps.지하철역_생성요청;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Map<String, String> lineParams = new HashMap<>();
    private Map<String, String> sectionParams = new HashMap<>();
    private Map<String, String> stationParams1 = new HashMap<>();
    private Map<String, String> stationParams2 = new HashMap<>();
    private Map<String, String> stationParams3 = new HashMap<>();

    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private int distance;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        stationParams1.put("name", 강남역.name);
        stationParams2.put("name", 역삼역.name);
        stationParams3.put("name", 선릉역.name);

        stationId1 = 지하철역_생성요청(stationParams1).as(StationResponse.class).getId();
        stationId2 = 지하철역_생성요청(stationParams2).as(StationResponse.class).getId();
        stationId3 = 지하철역_생성요청(stationParams3).as(StationResponse.class).getId();
        distance = 10;

        lineParams.put("name", 일호선.name);
        lineParams.put("color", 일호선.color);
        lineParams.put("upStationId", String.valueOf(stationId1));
        lineParams.put("downStationId", String.valueOf(stationId2));
        lineParams.put("distance", String.valueOf(distance));
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void addSection() {
        // given
        Long lineId = 지하철_노선_생성됨(lineParams);

        // when
        sectionParams.put("upStationId", String.valueOf(stationId2));
        sectionParams.put("downStationId", String.valueOf(stationId3));
        sectionParams.put("distance", String.valueOf(distance));

        ExtractableResponse<Response> response = 지하철노선_구간_등록요청(lineId, sectionParams);

        // then
        지하철노선_구간_등록됨(response);
    }

    @DisplayName("새로운 구간의 상행역이 현재 등록되어 있는 하행 종점역이 아닌경우 실패한다.")
    @Test
    void failNewUpStationIsWrong() {
        // given
        Long lineId = 지하철_노선_생성됨(lineParams);

        // when
        sectionParams.put("upStationId", String.valueOf(stationId1));
        sectionParams.put("downStationId", String.valueOf(stationId3));
        sectionParams.put("distance", String.valueOf(distance));

        ExtractableResponse<Response> response = 지하철노선_구간_등록요청(lineId, sectionParams);

        // then
        지하철노선_구간_등록실패됨(response);
    }

    @DisplayName("새로운 구간의 하행역이 현재 등록되어있는 경우 실패한다.")
    @Test
    void failNewDownStationIsAlreadyRegistered() {
        // given
        Long lineId = 지하철_노선_생성됨(lineParams);

        // when & then
        sectionParams.put("upStationId", String.valueOf(stationId2));
        sectionParams.put("downStationId", String.valueOf(stationId1));
        sectionParams.put("distance", String.valueOf(distance));

        ExtractableResponse<Response> response = 지하철노선_구간_등록요청(lineId, sectionParams);

        // then
        지하철노선_구간_등록실패됨(response);
    }

    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        Long lineId = 지하철_노선_생성됨(lineParams);

        sectionParams.put("upStationId", String.valueOf(stationId2));
        sectionParams.put("downStationId", String.valueOf(stationId3));
        sectionParams.put("distance", String.valueOf(distance));

        지하철노선_구간_등록요청(lineId, sectionParams);

        // when
        ExtractableResponse<Response> response = 지하철노선_구간_제거요청(lineId, stationId3);

        // then
        지하철노선_구간_삭제됨(response);
    }

    @DisplayName("삭제하려는 역이 하행 종점역이 아닌경우 실패한다.")
    @Test
    void failDeleteStationIsNotLastStation() {
        // given
        Long lineId = 지하철_노선_생성됨(lineParams);

        sectionParams.put("upStationId", String.valueOf(stationId2));
        sectionParams.put("downStationId", String.valueOf(stationId3));
        sectionParams.put("distance", String.valueOf(distance));

        지하철노선_구간_등록요청(lineId, sectionParams);

        // when
        ExtractableResponse<Response> response = 지하철노선_구간_제거요청(lineId, stationId2);

        // then
        지하철노선_구간_삭제실패됨(response);
    }

    @DisplayName("구간이 1개인 경우 삭제시도시 실패한다.")
    @Test
    void failOnlyOneSectionLeft() {
        // given
        Long lineId = 지하철_노선_생성됨(lineParams);

        // when
        ExtractableResponse<Response> response = 지하철노선_구간_제거요청(lineId, stationId2);

        // then
        지하철노선_구간_삭제실패됨(response);
    }
}
