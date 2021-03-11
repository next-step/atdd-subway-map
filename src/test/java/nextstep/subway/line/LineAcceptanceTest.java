package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.LineSteps.Line.분당선;
import static nextstep.subway.line.LineSteps.Line.일호선;
import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.line.SectionSteps.지하철노선_구간_등록요청;
import static nextstep.subway.station.StationSteps.Station.*;
import static nextstep.subway.station.StationSteps.지하철역_생성요청;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private Map<String, String> lineParams1 = new HashMap<>();
    private Map<String, String> lineParams2 = new HashMap<>();
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

        lineParams1.put("name", 일호선.name);
        lineParams1.put("color", 일호선.color);
        lineParams1.put("upStationId", String.valueOf(stationId1));
        lineParams1.put("downStationId", String.valueOf(stationId2));
        lineParams1.put("distance", String.valueOf(distance));

        lineParams2.put("name", 분당선.name);
        lineParams2.put("color", 분당선.color);
        lineParams2.put("upStationId", String.valueOf(stationId1));
        lineParams2.put("downStationId", String.valueOf(stationId2));
        lineParams2.put("distance", String.valueOf(distance));
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given & when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(lineParams1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 노선을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_노선_생성요청(lineParams1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(lineParams1);

        // then
        지하철_노선_생성실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_생성요청(lineParams1);
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_생성요청(lineParams2);

        // when
        ExtractableResponse<Response> response = 지하철_노선목록_조회요청();

        // then
        지하철_노선목록_응답됨(response);
        지하철_노선목록_포함됨(response, Arrays.asList(createdResponse1, createdResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(lineParams1);
        Long lineId = createdResponse.as(LineResponse.class).getId();

        sectionParams.put("upStationId", String.valueOf(stationId2));
        sectionParams.put("downStationId", String.valueOf(stationId3));
        sectionParams.put("distance", String.valueOf(distance));

        지하철노선_구간_등록요청(lineId, sectionParams);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회요청(lineId);

        // then
        지하철_노선_응답됨(response);
        지하철_노선_포함됨(response, createdResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(lineParams1);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선_수정요청(lineId, lineParams2);

        // then
        지하철_노선_수정됨(response);
        지하철_노선_확인됨(response, lineParams2.get("name"), lineParams2.get("color"));
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(lineParams1);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선_제거요청(lineId);

        // then
        지하철_노선_삭제됨(response);
    }
}
