package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.station.acceptance.step.LineStationAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에서 역 제외 기능")
public class LineStationRemoveAcceptanceTest extends AcceptanceTest {

    private Long 이호선;
    private Long 강남역;
    private Long 역삼역;
    private Long 면목역;
    private Long 선릉역;
    private Long 안국역;

    @DisplayName("지하철 역이 등록되어 있고 노선이 등록되어 있고 노선에 지하철역이 등록되어 있다.")
    @BeforeEach
    void background() {
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("면목역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("선릉역");
        ExtractableResponse<Response> createdStationResponse5 = 지하철역_등록되어_있음("안국역");
        이호선 = createdLineResponse.as(LineResponse.class).getId();
        강남역 = createdStationResponse1.as(StationResponse.class).getId();
        역삼역 = createdStationResponse2.as(StationResponse.class).getId();
        면목역 = createdStationResponse3.as(StationResponse.class).getId();
        선릉역 = createdStationResponse4.as(StationResponse.class).getId();
        안국역 = createdStationResponse5.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록_요청(이호선, null, 강남역, 4L, 2L);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, 4L, 2L);
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 면목역, 4L, 2L);
        지하철_노선에_지하철역_등록_요청(이호선, 면목역, 선릉역, 4L, 2L);
    }

    @DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
    @Test
    void removeFinalStationOnLine() {

        // when
        final ExtractableResponse<Response> deleteResponse = 지하철_노선_상_지하철역_제외_요청(이호선, 선릉역);
        지하철_노선에_지하철역_제외됨(deleteResponse);

        // when
        final ExtractableResponse<Response> lineInfoResponse = 지하철_노선_상세정보_조회_요청(이호선);
        지하철_노선에_지하철역_제외_확인됨(lineInfoResponse, 선릉역);
        지하철_노선_등록_순서_검사(lineInfoResponse, 강남역, 역삼역, 면목역);
    }

    @DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
    @Test
    void removeStationInTheMiddleOfLine() {

        // when
        final ExtractableResponse<Response> deleteResponse = 지하철_노선_상_지하철역_제외_요청(이호선, 면목역);
        // then
        지하철_노선에_지하철역_제외됨(deleteResponse);

        // when
        final ExtractableResponse<Response> lineInfoResponse = 지하철_노선_상세정보_조회_요청(이호선);
        // then
        지하철_노선에_지하철역_제외_확인됨(lineInfoResponse, 면목역);
        지하철_노선_등록_순서_검사(lineInfoResponse, 강남역, 역삼역, 선릉역);
    }

    @DisplayName("지하철 노선에서 등록되지 않는 역을 제외한다.")
    @Test
    void removeUnregisteredStationOnLine() {
        
        // when
        final ExtractableResponse<Response> deleteResponse = 지하철_노선_상_지하철역_제외_요청(이호선, 안국역);
        지하철_노선에_지하철역_제외_실패됨(deleteResponse);
    }
}
