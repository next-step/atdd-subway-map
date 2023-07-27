package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;
    private Long A역;
    private Long C역;
    private Long 지하철_노선;

    @DisplayName("2개의 역과 1개의 노선을 생성합니다.")
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        A역 = 지하철_역_생성_요청("A역").jsonPath().getLong("id");
        C역 = 지하철_역_생성_요청("C역").jsonPath().getLong("id");
        지하철_노선 = 지하철_노선_생성_요청().jsonPath().getLong("id");
    }

    /**
     * Given 새로운 지하철 구간을 만들고
     * When 지하철 노선에 새로운 구간을 추가하면
     * Then 구간이 추가된다.
     */
    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addSection() {
        // given
        Long B역 = 지하철_역_생성_요청("B역").jsonPath().getLong("id");
        Map<String, Object> 구간_C역_B역 = Map.of(
                "upStationId", 2L,
                "downStationId", 3L,
                "distance", 3);

        // when
        ExtractableResponse<Response> 지하철_구간_추가_결과 = 지하철_구간_추가_요청_임시(지하철_노선, 구간_C역_B역);

        // then
        지하철_구간_등록_됨(지하철_구간_추가_결과);
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
        Long B역 = 지하철_역_생성_요청("B역").jsonPath().getLong("id");
        Map<String, Object> 구간_C역_B역 = Map.of(
                "upStationId", 2L,
                "downStationId", 3L,
                "distance", 3);
        지하철_구간_추가_요청_임시(지하철_노선, 구간_C역_B역);

        // when
        ExtractableResponse<Response> 지하철_구간_삭제_결과 = 지하철_구간_삭제_요청(1L, 3L);

        // then
        지하철_구간_삭제_됨(지하철_구간_삭제_결과);
    }

    /**
     * When 새로운 구간의 상행역이 노선에 등록되어 있는 하행 종점역이 아니면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("요청한 구간의 상행역이 노선의 하행 종점역이 아니면 예외가 발생한다.")
    @Test
    void newUpStationIsNotRegisteredLastDownStation() {
        // when
        Long B역 = 지하철_역_생성_요청("B역").jsonPath().getLong("id");
        Map<String, Object> 구간_A역_B역 = Map.of(
                "upStationId", 1L,
                "downStationId", 3L,
                "distance", 3);
        ExtractableResponse<Response> 지하철_구간_추가_결과 = 지하철_구간_추가_요청_임시(지하철_노선, 구간_A역_B역);

        // then
        Assertions.assertThat(지하철_구간_추가_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 새로운 구간의 하행역이 노선에 이미 등록되어 있으면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("요청한 구간의 하행역이 이미 등록되어 있는 역이면 예외가 발생한다.")
    @Test
    void newDownStationIsRegistered() {
        // when
        Map<String, Object> 구간_A역_C역 = Map.of(
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 3);
        ExtractableResponse<Response> 지하철_구간_추가_결과 = 지하철_구간_추가_요청_임시(지하철_노선, 구간_A역_C역);

        // then
        Assertions.assertThat(지하철_구간_추가_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청() {
        Map<String, Object> 지하철_노선_요청_값 = Map.of(
                "name", "지하철_노선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 7);

        return SubwayLineHelper.지하철_노선_생성_요청(지하철_노선_요청_값);
    }

    private void 지하철_구간_등록_됨(ExtractableResponse<Response> registerSectionApiResponse) {
        assertThat(registerSectionApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_삭제_됨(ExtractableResponse<Response> deleteSectionApiResponse) {
        SubwayLineHelper.지하철_노선_목록_조회_요청();
        assertThat(deleteSectionApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}