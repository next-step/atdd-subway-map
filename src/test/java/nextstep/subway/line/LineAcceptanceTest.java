package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.line.LineSteps.Line.*;
import static nextstep.subway.line.SectionSteps.지하철노선_구간_등록요청;
import static nextstep.subway.station.StationSteps.Station.*;
import static nextstep.subway.station.StationSteps.지하철역_생성요청;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private LineRequest line1Request;
    private LineRequest line2Request;

    private Long station1;
    private Long station2;
    private Long station3;
    private int distance;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        station1 = 지하철역_생성요청(강남역.name).as(StationResponse.class).getId();
        station2 = 지하철역_생성요청(역삼역.name).as(StationResponse.class).getId();
        station3 = 지하철역_생성요청(선릉역.name).as(StationResponse.class).getId();
        distance = 10;

        line1Request = new LineRequest(일호선.name, 일호선.color, station1, station2, distance);
        line2Request = new LineRequest(분당선.name, 분당선.color, station1, station2, distance);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given & when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(line1Request);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 노선을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_노선_생성요청(line1Request);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(line1Request);

        // then
        지하철_노선_생성실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_생성요청(line1Request);
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_생성요청(line2Request);

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
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(line1Request);
        Long lineId = createdResponse.as(LineResponse.class).getId();
        지하철노선_구간_등록요청(lineId, new SectionRequest(station2, station3, distance));

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
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(line1Request);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        String name = 에버라인.name;
        String color = 에버라인.color;
        ExtractableResponse<Response> response = 지하철_노선_수정요청(lineId, name, color);

        // then
        지하철_노선_수정됨(response);
        지하철_노선_확인됨(response, name, color);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(line1Request);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선_제거요청(lineId);

        // then
        지하철_노선_삭제됨(response);
    }
}
