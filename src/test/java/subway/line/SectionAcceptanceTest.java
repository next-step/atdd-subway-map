package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.SectionRequest;
import subway.station.dto.StationResponse;
import subway.util.assertUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.line.LineAcceptanceTest.지하철노선이_생성됨;
import static subway.line.LineAcceptanceTest.지하철노선이_조회됨;
import static subway.station.StationAcceptanceTest.지하철역이_생성됨;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SectionAcceptanceTest {

    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;

    @BeforeEach
    void init() {
        //given - 지하철노선과 지하철역이 등록되어있다.
        강남역 = 지하철역이_생성됨("강남역");
        역삼역 = 지하철역이_생성됨("역삼역");
        선릉역 = 지하철역이_생성됨("선릉역");

        이호선 = 지하철노선이_생성됨(new LineRequest("2호선", "blue", 강남역.getId(), 역삼역.getId(), 10L));
    }

    /**
     * 지하철구간 생성
     *  - When 특정 지하철 노선에 지하철 구간을 생성하면
     *  - Then 특정 지하철 노선에 요청한 지하철 구간이 생성된다
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void Should_지하철구간을_생성하면_Then_지하철구간이_생성된다() {
        // when
        var 구간_request = new SectionRequest(역삼역.getId(), 선릉역.getId(), 10L);
        ExtractableResponse<Response> 구간_response = 지하철구간을_생성한다(이호선.getId(), 구간_request);

        // then
        지하철구간이_정상적으로_생성(구간_response);
        LineResponse 이호선_조회 = 지하철노선이_조회됨(이호선.getId());
        assertThat(이호선_조회.getStationIds()).containsExactlyElementsOf(List.of(강남역.getId(), 역삼역.getId(), 선릉역.getId()));
    }

    /**
     * 지하철노선 삭제
     *  - Given 지하철 구간을 생성하고
     *  - When 생성한 지하철 구간을 삭제하면
     *  - Then 해당 지하철 구간은 삭제된다
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void Should_지하철구간을_삭제하면_Then_지하철구간이_삭제된다() {
        //given ==> 강남역 - 역삼역 - 선릉역
        지하철구간을_생성한다(이호선.getId(), new SectionRequest(역삼역.getId(), 선릉역.getId(), 10L));

        // when
        ExtractableResponse<Response> response = 지하철구간을_삭제한다(이호선.getId(), 선릉역.getId());

        // then
        assertUtils.assertHttpStatus(response.statusCode(), HttpStatus.NO_CONTENT.value());
        LineResponse 이호선_조회 = 지하철노선이_조회됨(이호선.getId());
        assertThat(이호선_조회.getStationIds()).doesNotContain(선릉역.getId());
    }


    private static ExtractableResponse<Response> 지하철구간을_생성한다(Long lineId, SectionRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(String.format("/lines/%d/sections", lineId))
                .then().log().all()
                .extract();
    }

    private static void 지하철구간이_정상적으로_생성(ExtractableResponse<Response> response) {
        assertUtils.assertHttpStatus(response.statusCode(), HttpStatus.CREATED.value());
    }

    private static ExtractableResponse<Response> 지하철구간을_삭제한다(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(String.format("/lines/%d/sections?stationId=%d", lineId, stationId))
                .then().log().all()
                .extract();
    }
}
