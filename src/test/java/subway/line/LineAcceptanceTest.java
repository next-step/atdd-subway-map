package subway.line;

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
import subway.helper.SubwayStationHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.helper.SubwayLineHelper.*;
import static subway.helper.SubwayLineHelper.지하철_노선_생성_요청;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    @DisplayName("4개의 지하철 역을 생성합니다.")
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        Stream.of("지하철 역", "새로운 지하철 역", "또 다른 지하철 역", "또 다른 새로운 지하철 역")
                .forEach(SubwayStationHelper::지하철_역_생성_요청);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        // when
        지하철_노선_생성_요청(신분당선);

        // then
        생성된_지하철_노선_조회됨("신분당선");
    }

    /**
     * Given 2개의 지하철 노션을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showSubwayLines() {
        // given
        지하철_노선_생성_요청(신분당선);
        지하철_노선_생성_요청(분당선);

        // when
        List<String> 생성된_지하철_노선_목록_이름 = 지하철_노선_목록_이름_조회();

        // then
        Assertions.assertThat(생성된_지하철_노선_목록_이름).contains("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답 받을 수 있다.
     */
    @DisplayName("지하철 노선 정보를 조회한다.")
    @Test
    void showSubwayLine() {
        // given
        ExtractableResponse<Response> 지하철_노선_생성_결과 = 지하철_노선_생성_요청(신분당선);
        String 생성된_지하철_노선_URL = 생성된_지하철_노선_URL(지하철_노선_생성_결과);

        // when
        ExtractableResponse<Response> 지하철_노선_정보_조회_요청_결과 = 지하철_노선_정보_조회_요청(생성된_지하철_노선_URL);

        // then
        지하철_노선_정보_조회됨(지하철_노선_정보_조회_요청_결과, 신분당선);
    }

    /**
     * Given 지하철 노션을 생성하고
     * When 생성한 지하철 노션 정보를 수정하면
     * Then 해당 지하철 노션 정보는 수정된다.
     */
    @DisplayName("지하철 노션 정보를 수정한다.")
    @Test
    void updateSubwayLine() {
        // given
        ExtractableResponse<Response> 지하철_노선_생성_결과 = 지하철_노선_생성_요청(신분당선);
        String 생성된_지하철_노선_URL = 생성된_지하철_노선_URL(지하철_노선_생성_결과);

        // when
        Map<String, Object> 지하철_노선_수정_값 = Map.of("name", "다른분당선", "color", "bg-red-600");

        ExtractableResponse<Response> 지하철_노선_정보_수정_결과 =
                지하철_노선_정보_수정_요청(생성된_지하철_노선_URL, 지하철_노선_수정_값);

        // then
        지하철_노선_정보_수정됨(지하철_노선_정보_수정_결과);
    }

    /**
     * Given 지하철 노션을 생성하고
     * When 생성한 지하철 노션을 삭제하면
     * Then 해당 지하철 노션 정보는 삭제된다.
     */
    @DisplayName("지하철 노션을 삭제한다.")
    @Test
    void deleteSubwayLine() {
        // given
        ExtractableResponse<Response> 지하철_노선_생성_결과 = 지하철_노선_생성_요청(신분당선);
        String 생성된_지하철_노선_URL = 생성된_지하철_노선_URL(지하철_노선_생성_결과);

        // when
        ExtractableResponse<Response> 지하철_노선_삭제_결과 = 지하철_노선_삭제_요청(생성된_지하철_노선_URL);

        // then
        지하철_노선_삭제됨(지하철_노선_삭제_결과);
    }

    private String 생성된_지하철_노선_URL(ExtractableResponse<Response> createSubwayLineApiResponse) {
        String createSubwayLineApiResponseUrl = createSubwayLineApiResponse
                .response().getHeaders().getValue("Location");

        return createSubwayLineApiResponseUrl;
    }

    private List 지하철_노선_목록_이름_조회() {
        List<String> subwayLineNames = 지하철_노선_목록_조회_요청()
                .jsonPath().getList("name", String.class);

        return subwayLineNames;
    }

    private void 생성된_지하철_노선_조회됨(String subwayLineName) {
        List<String> subwayLineNames = 지하철_노선_목록_조회_요청()
                .jsonPath().getList("name", String.class);

        // then
        Assertions.assertThat(subwayLineNames).contains(subwayLineName);
    }

    private void 지하철_노선_정보_조회됨(ExtractableResponse<Response> showSubwayLineApiResponse
            , Map<String, Object> subwayLine) {
        long actualId = showSubwayLineApiResponse.jsonPath().getLong("id");
        String actualName = showSubwayLineApiResponse.jsonPath().getString("name");
        String actualColor = showSubwayLineApiResponse.jsonPath().getString("color");

        Assertions.assertThat(actualId).isEqualTo(subwayLine.get("upStationId"));
        Assertions.assertThat(actualName).isEqualTo(subwayLine.get("name"));
        Assertions.assertThat(actualColor).isEqualTo(subwayLine.get("color"));
    }

    private void 지하철_노선_정보_수정됨(ExtractableResponse<Response> updateSubwayLineApiResponse) {
        assertThat(updateSubwayLineApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> deleteSubwayLineApiResponse) {
        assertThat(deleteSubwayLineApiResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}