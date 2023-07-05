package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.helper.SubwayLineHelper;
import subway.helper.SubwayStationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    @DisplayName("4개의 지하철 역을 생성합니다.")
    @BeforeEach
    void setUp() {
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
        SubwayLineHelper.지하철_노선_생성_요청(SubwayLineHelper.SUBWAY_LIEN_PARAMETERS_1);

        // then
        String actualSubwayLineName = SubwayLineHelper.지하철_노선_목록_조회_요청()
                .jsonPath().getString("name");

        Assertions.assertThat(actualSubwayLineName).contains("신분당선");
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
        List<Map<String, Object>> parametersList = new ArrayList<>();
        parametersList.add(SubwayLineHelper.SUBWAY_LIEN_PARAMETERS_1);
        parametersList.add(SubwayLineHelper.SUBWAY_LIEN_PARAMETERS_2);

        for (Map<String, Object> parameters : parametersList) {
            SubwayLineHelper.지하철_노선_생성_요청(parameters);
        }

        // when
        List<String> subwayLineNames = SubwayLineHelper.지하철_노선_목록_조회_요청()
                    .jsonPath().getList("name", String.class);

        // then
        Assertions.assertThat(subwayLineNames).contains("신분당선", "분당선");
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
        ExtractableResponse<Response> createSubwayLineApiResponse = SubwayLineHelper
                .지하철_노선_생성_요청(SubwayLineHelper.SUBWAY_LIEN_PARAMETERS_1);
        String createSubwayLineApiResponseUrl = createSubwayLineApiResponse
                .response().getHeaders().getValue("Location");

        // when
        ExtractableResponse<Response> response = SubwayLineHelper.지하철_노선_정보_조회_요청(createSubwayLineApiResponseUrl);

        long actualId = response.jsonPath().getLong("id");
        String actualName = response.jsonPath().getString("name");
        String actualColor = response.jsonPath().getString("color");

        // then
        Assertions.assertThat(actualId).isEqualTo(1L);
        Assertions.assertThat(actualName).contains("신분당선");
        Assertions.assertThat(actualColor).contains("bg-red-600");
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
        ExtractableResponse<Response> createSubwayLineApiResponse = SubwayLineHelper
                .지하철_노선_생성_요청(SubwayLineHelper.SUBWAY_LIEN_PARAMETERS_1);
        String createSubwayLineApiResponseUrl = createSubwayLineApiResponse
                .response().getHeaders().getValue("Location");

        // when
        Map<String, Object> updateLineRequest = Map.of("name", "다른분당선", "color", "bg-red-600");

        ExtractableResponse<Response> updateSubwayLineApiResponse = SubwayLineHelper
                .지하철_노선_정보_수정_요청(createSubwayLineApiResponseUrl, updateLineRequest);

        // then
        assertThat(updateSubwayLineApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노션을 생성하고
     * When 생성한 지하철 노션을 삭제하면
     * Then 해당 지하철 노션 장보는 삭제된다.
     */
    @DisplayName("지하철 노션을 삭제한다.")
    @Test
    void deleteSubwayLine() {
        // given
        ExtractableResponse<Response> createSubwayLineApiResponse = SubwayLineHelper
                .지하철_노선_생성_요청(SubwayLineHelper.SUBWAY_LIEN_PARAMETERS_1);
        String createSubwayLineApiResponseUrl = createSubwayLineApiResponse
                .response().getHeaders().getValue("Location");

        // when
        ExtractableResponse<Response> deleteSubwayLineApiResponse = SubwayLineHelper
                .지하철_노선_삭제_요청(createSubwayLineApiResponseUrl);

        // then
        assertThat(deleteSubwayLineApiResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}