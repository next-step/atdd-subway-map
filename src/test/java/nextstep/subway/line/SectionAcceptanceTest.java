package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static java.lang.String.valueOf;
import static nextstep.subway.line.LineSteps.지하철_노선_생성;
import static nextstep.subway.line.LineSteps.지하철_노선_조회_요청;
import static nextstep.subway.station.StationSteps.지하철_역_생성;
import static nextstep.subway.utils.HttpAssertions.응답_HTTP_CREATED;
import static nextstep.subway.utils.HttpAssertions.응답_HTTP_NO_CONTENT;
import static nextstep.subway.utils.HttpTestUtils.리소스_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 구간을 추가한다.")
    @Test
    void createSection() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));
        Long 양재시민의숲역_ID = 리소스_ID(지하철_역_생성("양재시민의숲역"));

        LineRequest createRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        Long 신분당선_ID = 리소스_ID(지하철_노선_생성(createRequest));

        // when
        SectionRequest sectionRequest = new SectionRequest(양재역_ID, 양재시민의숲역_ID, 100);
        ExtractableResponse<Response> response = 구간_추가_요청(신분당선_ID, sectionRequest);

        // then
        응답_HTTP_CREATED(response);
    }

    @DisplayName("지하철 노선에 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));
        Long 양재시민의숲역_ID = 리소스_ID(지하철_역_생성("양재시민의숲역"));

        LineRequest createRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        Long 신분당선_ID = 리소스_ID(지하철_노선_생성(createRequest));
        SectionRequest sectionRequest = new SectionRequest(양재역_ID, 양재시민의숲역_ID, 100);
        구간_추가_요청(신분당선_ID, sectionRequest);

        // when
        ExtractableResponse<Response> deleteSectionResponse = 구간_삭제_요청(양재시민의숲역_ID, 신분당선_ID);

        // then
        포함된_역_갯수는(신분당선_ID, 2);
        응답_HTTP_NO_CONTENT(deleteSectionResponse);
    }

    private void 포함된_역_갯수는(Long 신분당선_ID, int size) {
        ExtractableResponse<Response> response2 = 지하철_노선_조회_요청(신분당선_ID);

        List<StationResponse> stationResponses = response2.jsonPath().getList("stations", StationResponse.class);
        assertThat(stationResponses.size()).isEqualTo(size);
    }

    private ExtractableResponse<Response> 구간_삭제_요청(Long 양재시민의숲역_ID, Long 신분당선_ID) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", 신분당선_ID, 양재시민의숲역_ID)
                .then().log().all().extract();
        return response;
    }

    private ExtractableResponse<Response> 구간_추가_요청(Long lineId, SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }


}
