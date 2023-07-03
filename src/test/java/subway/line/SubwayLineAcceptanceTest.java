package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.helper.SubwayHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    private List<String> stationDataSet = Arrays.asList("지하철 역", "새로운 지하철 역", "또 다른 지하철 역"
            , "또 다른 새로운 지하철 역");

    @DisplayName("4개의 지하철 역을 생성합니다.")
    @BeforeEach
    void setUp() {
        for (String stationName : stationDataSet) {
            SubwayHelper.지하철_노션에_지하철_역_요청(stationName);
        }
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        // when
        ExtractableResponse<Response> createSubwayLineApiResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(SubwayHelper.SUBWAY_LIEN_PARAMETERS_1)
                .when().log().all()
                    .post(SubwayHelper.SUBWAY_LINE_API_URL)
                .then().log().all()
                    .extract();

        // then
        String actualSubwayLineName = RestAssured
                .given().log().all()
                .when().log().all()
                    .get(SubwayHelper.SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract()
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
        parametersList.add(SubwayHelper.SUBWAY_LIEN_PARAMETERS_1);
        parametersList.add(SubwayHelper.SUBWAY_LIEN_PARAMETERS_2);

        for (Map<String, Object> parameters : parametersList) {
            SubwayHelper.지하철_노션_요청(parameters);
        }

        // when
        List<String> subwayLineNames = RestAssured
                .given().log().all()
                .when().log().all()
                    .get(SubwayHelper.SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract()
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
        ExtractableResponse<Response> createSubwayLineApiResponse = SubwayHelper
                .지하철_노션_요청(SubwayHelper.SUBWAY_LIEN_PARAMETERS_1);
        String createSubwayLineApiResponseId = createSubwayLineApiResponse.jsonPath()
                .getString("id");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().log().all()
                    .get(SubwayHelper.SUBWAY_LINE_API_URL + "/" + createSubwayLineApiResponseId)
                .then().log().all()
                .extract();

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
        ExtractableResponse<Response> createSubwayLineApiResponse = SubwayHelper
                .지하철_노션_요청(SubwayHelper.SUBWAY_LIEN_PARAMETERS_1);
        String createSubwayLineApiResponseId = createSubwayLineApiResponse.jsonPath().getString("id");

        // when
        Map<String, Object> updateLineParameters = Map.of("name", "다른분당선", "color", "bg-red-600");

        ExtractableResponse<Response> updateSubwayLineApiResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(updateLineParameters)
                .when().log().all()
                    .put(SubwayHelper.SUBWAY_LINE_API_URL + "/" + createSubwayLineApiResponseId)
                .then().log().all()
                .extract();

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
        ExtractableResponse<Response> createSubwayLineApiResponse = SubwayHelper
                .지하철_노션_요청(SubwayHelper.SUBWAY_LIEN_PARAMETERS_1);
        String createSubwayLineApiResponseId = createSubwayLineApiResponse.jsonPath().getString("id");

        // when
        ExtractableResponse<Response> deleteSubwayLineApiResponse = RestAssured
                .given().log().all()
                .when().log().all()
                    .delete(SubwayHelper.SUBWAY_LINE_API_URL + "/" + createSubwayLineApiResponseId)
                .then().log().all()
                .extract();

        // then
        assertThat(deleteSubwayLineApiResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}