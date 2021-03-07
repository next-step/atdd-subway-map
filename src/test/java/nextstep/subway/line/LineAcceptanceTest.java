package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.data.Lines;
import nextstep.subway.data.Stations;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationStep;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.LineStep.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private LineResponse 사호선;
    private LineResponse 신분당선;
    private StationResponse 금정역;
    private StationResponse 범계역;
    private StationResponse 강남역;
    private StationResponse 광교역;



    @BeforeEach
    void setup() {
        super.setUp();
        금정역 = StationStep.지하철역_생성_요청(Stations.금정역).as(StationResponse.class);
        범계역 = StationStep.지하철역_생성_요청(Stations.범계역).as(StationResponse.class);
        강남역 = StationStep.지하철역_생성_요청(Stations.강남역).as(StationResponse.class);
        광교역 = StationStep.지하철역_생성_요청(Stations.광교역).as(StationResponse.class);

        Lines.사호선.put("upStationId", 금정역.getId() + "");
        Lines.사호선.put("downStationId", 범계역.getId() + "");
        Lines.사호선.put("distance", 10 + "");

        Lines.신분당선.put("upStationId", 강남역.getId() + "");
        Lines.신분당선.put("downStationId", 광교역.getId() + "");
        Lines.신분당선.put("distance", 10 + "");

    }


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(Lines.사호선);

        // then
        응답_결과_확인(response, HttpStatus.CREATED);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        //given
        ExtractableResponse<Response> createdLineResponse1 = 지하철_노선_생성_요청(Lines.사호선);
        ExtractableResponse<Response> createdLineResponse2 = 지하철_노선_생성_요청(Lines.신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        응답_결과_확인(response, HttpStatus.OK);

        List<Long> createdLineIds = 지하철_노선_아이디_추출(createdLineResponse1, createdLineResponse2);
        List<Long> resultLineIds = 지하철_노선_객체_리스트_반환(response);

        지하철_노선_목록_포함됨(createdLineIds, resultLineIds);
    }


    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        //given
        LineResponse lineResponse = 지하철_노선_생성_요청(Lines.신분당선).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse.getId());

        // then
        응답_결과_확인(response, HttpStatus.OK);
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse response = 지하철_노선_생성_요청(Lines.신분당선).as(LineResponse.class);

        // when
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "구분당선");
        params.put("color", "bg-blue-600");
        Long createdId = response.getId();

        ExtractableResponse<Response> response1 = 지하철_노선_수정_요청(createdId, params);
        ExtractableResponse<Response> response2 = 지하철_노선_조회_요청(createdId);

        // then
        지하철_노선_수정됨(response1);
        지하철_노선_수정_확인(response2, params);
    }


    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_생성_요청(Lines.신분당선);
        Long createdId = 지하철_노선_아이디_추출(createdLineResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createdId);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        ExtractableResponse<Response> createdLineResponse1 = 지하철_노선_생성_요청(Lines.신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(Lines.신분당선);

        // then
        지하철_노선_생성_실패됨(response);
    }
}
