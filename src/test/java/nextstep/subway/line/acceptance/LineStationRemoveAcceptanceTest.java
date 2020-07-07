package nextstep.subway.line.acceptance;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_상세정보_조회_요청;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.지하철_노선에_지하철역_등록_되어있음;
import static nextstep.subway.line.acceptance.step.LineStationRemoveAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 등록 제외 기능")
public class LineStationRemoveAcceptanceTest extends AcceptanceTest {

    private ExtractableResponse<Response> stationResponse1;
    private ExtractableResponse<Response> stationResponse2;
    private ExtractableResponse<Response> stationResponse3;
    private ExtractableResponse<Response> lineResponse;

    @BeforeEach
    void setBackground() {
        stationResponse1 = 지하철역_등록되어_있음("강남역");
        stationResponse2 = 지하철역_등록되어_있음("양재역");
        stationResponse3 = 지하철역_등록되어_있음("판교역");

        lineResponse = 지하철_노선_등록되어_있음("신분당선", "YELLOW", LocalTime.of(6, 30), LocalTime.of(22, 30), 5);

        지하철_노선에_지하철역_등록_되어있음(lineResponse, stationResponse1, "");
        지하철_노선에_지하철역_등록_되어있음(lineResponse, stationResponse2, "1");
        지하철_노선에_지하철역_등록_되어있음(lineResponse, stationResponse3, "2");
    }

    @DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
    @Test
    void removeLineStationAtLast() {
        //when
        ExtractableResponse<Response> deleteResponse = 지하철_노선의_지하철역_제외_요청(lineResponse, stationResponse3);

        //then
        지하철_노선에_지하철역_제외됨(deleteResponse);

        //when
        ExtractableResponse<Response> lineDetailResponse = 지하철_노선_상세정보_조회_요청(lineResponse);
        지하철_노선에_지하철역_제외_확인됨(lineDetailResponse, stationResponse3, 2);
        지하철_노선에_지하철역_순서_정렬됨(lineDetailResponse, Lists.newArrayList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
    @Test
    void removeLineStationAtMiddle() {
        //when
        ExtractableResponse<Response> deleteResponse = 지하철_노선의_지하철역_제외_요청(lineResponse, stationResponse2);

        //then
        지하철_노선에_지하철역_제외됨(deleteResponse);

        //when
        ExtractableResponse<Response> lineDetailResponse = 지하철_노선_상세정보_조회_요청(lineResponse);
        지하철_노선에_지하철역_제외_확인됨(lineDetailResponse, stationResponse2, 2);
        지하철_노선에_지하철역_순서_정렬됨(lineDetailResponse, Lists.newArrayList(stationResponse1, stationResponse3));
    }

}
