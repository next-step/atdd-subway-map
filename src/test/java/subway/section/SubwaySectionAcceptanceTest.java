package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.helper.SubwayLineHelper;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.helper.SubwaySectionHelper.*;
import static subway.helper.SubwayStationHelper.지하철_역_생성_요청;

@DisplayName("지하철 구간 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwaySectionAcceptanceTest {

    private final Map<String, Object> 구간_A역_C역 = Map.of(
            "downStationId", 4L,
            "upStationId", 1L,
            "distance", 7
    );

    private final Map<String, Object> 구간_C역_B역 = Map.of(
            "downStationId", 4L,
            "upStationId", 2L,
            "distance", 10
    );

    @DisplayName("4개의 지하철 역을 생성합니다.")
    @BeforeEach
    void setUp() {
        네개의_지하철_역_생성_요청();
        지하철_노선_생성_요청();
    }

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철 노선에 생성된 지하철 구간이 생성된다.
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createSubwaySection() {
        // when
        ExtractableResponse<Response> 지하철_구간_생성_결과 = 지하철_구간_생성_요청(구간_A역_C역);

        // then
        지하철_구간_생성됨(지하철_구간_생성_결과);
    }

    /**
     * Given 첫 지하철 구간을 생성한 후
     * Given 새로운 지하철 구간을 생성하고
     * When 새로 생성된 지하철 구간이 기존의 지하철 노선에 등록되면
     * Then 기존의 지하철 노선에 새로운 지하철 구간이 등록된다.
     */
    @DisplayName("지하철 구간을 등록한다")
    @Test
    void registerSubwaySection() {
        // given
        지하철_구간_생성_요청(구간_A역_C역);

        // given
        지하철_구간_생성_요청(구간_C역_B역);

        // when
        ExtractableResponse<Response> 지하철_구간_등록_결과 = 지하철_구간_등록_요청(구간_C역_B역);

        // then
        지하철_구간_등록_됨(지하철_구간_등록_결과);
    }

    /**
     * Given 기존 지하철 노선에 구간을 등록하고
     * When 지하철 구간을 삭제하면
     * Then 지하철 노선의 하행 종점역이 삭제된다.
     */
    @DisplayName("지하철 구간을 삭제한다")
    @Test
    void deleteSubwaySection() {
        // given
        지하철_구간_삭제_요청_준비();

        // when
        ExtractableResponse<Response> 지하철_구간_삭제_결과 = 지하철_구간_삭제_요청("1");

        // then
        지하철_구간_삭제_됨(지하철_구간_삭제_결과);
    }

    private void 네개의_지하철_역_생성_요청() {
        지하철_역_생성_요청("A역");
        지하철_역_생성_요청("C역");
        지하철_역_생성_요청("D역");
        지하철_역_생성_요청("B역");
    }

    private void 지하철_노선_생성_요청() {
        Map<String, Object> 지하철_노선 = Map.of("name", "지하철_노선"
                , "color", "bg-red-600"
                , "upStationId", 1L, "downStationId", 4L
                , "distance", 7);

        SubwayLineHelper.지하철_노선_생성_요청(지하철_노선);
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> createSectionApiResponse) {
        assertThat(createSectionApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_등록_됨(ExtractableResponse<Response> registerSectionApiResponse) {
        assertThat(registerSectionApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_삭제_됨(ExtractableResponse<Response> deleteSectionApiResponse) {
        assertThat(deleteSectionApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_삭제_요청_준비() {
        지하철_구간_생성_요청(구간_A역_C역);
        지하철_구간_생성_요청(구간_C역_B역);
        지하철_구간_등록_요청(구간_C역_B역);
    }
}