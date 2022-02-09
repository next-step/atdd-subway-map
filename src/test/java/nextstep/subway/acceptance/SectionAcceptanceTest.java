package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.StationUtils;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.utils.LineUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long 노선에_속한_상행역;
    private Long 노선에_속한_하행역;
    private Long 노선에_속하지_않은_새로운역;
    private Long 지하철_노선;


    private static final Integer 거리 = 1;
    private static final String 상행역 = "상행역";
    private static final String 하행역 = "하행역";
    private static final String 새로운역 = "새로운역";


    @BeforeEach
    public void setup() {
        노선에_속한_상행역 = 지하철_역_데이터_생성(상행역);
        노선에_속한_하행역 = 지하철_역_데이터_생성(하행역);
        노선에_속하지_않은_새로운역 = 지하철_역_데이터_생성(새로운역);

        지하철_노선 = Long.valueOf(
                지하철_노선_생성요청(
                        지하철_노선_데이터_생성("1호선",
                                "blue",
                                노선에_속한_상행역,
                                노선에_속한_하행역,
                                거리))
                        .jsonPath()
                        .get("id")
                        .toString());
    }


    /**
     * given 하행역으로 상행 종점이면서, 새로운 역으로 하행 종점인 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 완료
     */
    @DisplayName("지하철 역 구간 생성")
    @Test
    void createSection() {
        // given
        final Map<String, Object> firstSectionParam = 지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속한_하행역);
        지하철_구간_생성_요청(firstSectionParam);

        // when
        final Map<String, Object> secondSectionParam = 지하철_구간_데이터_생성(노선에_속한_하행역, 노선에_속하지_않은_새로운역);
        final ExtractableResponse<Response> secondResponse = 지하철_구간_생성_요청(secondSectionParam);

        // then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(secondResponse.header("Location")).isNotBlank();
    }

    /**
     * given 하행역으로 상행 종점이면서, 상행역으로 하행 종점인 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 에러 (새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.)
     */
    @DisplayName("잘못된 하행역으로 구간 추가 시 에러")
    @Test
    void sectionCreationExceptionWithInvalidDownStation() {
        // given
        final Map<String, Object> firstSectionParam = 지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속한_하행역);
        지하철_구간_생성_요청(firstSectionParam);

        // when
        final Map<String, Object> secondSectionParam = 지하철_구간_데이터_생성(노선에_속한_하행역, 노선에_속한_상행역);
        final ExtractableResponse<Response> secondResponse = 지하철_구간_생성_요청(secondSectionParam);

        // then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * given 상행역으로 상행 종점이면서, 새로운 역으로 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 에러 (새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.)
     */
    @DisplayName("잘못된 상행역으로 구간 추가 시 에러")
    @Test
    void sectionCreationExceptionWithInvalidUpStation() {
        // given
        final Map<String, Object> firstSectionParam = 지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속한_하행역);
        지하철_구간_생성_요청(firstSectionParam);

        // when
        final Map<String, Object> secondSectionParam = 지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속하지_않은_새로운역);
        final ExtractableResponse<Response> secondResponse = 지하철_구간_생성_요청(secondSectionParam);

        // then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("지하철 구간 제거")
    @Test
    void deleteSection() {
        // given
        // 구간 생성
        지하철_구간_생성_요청(지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속한_하행역));
        final ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(지하철_구간_데이터_생성(노선에_속한_하행역, 노선에_속하지_않은_새로운역));

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 구간이 1개인 경우 지하철 구간 삭제 실패")
    @Test
    void invalidDeleteSection() {
        // given
        final Map<String, Object> param = 지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속한_하행역);
        지하철_구간_생성_요청(param);

        // when
        final ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when()
                .delete("/lines/" + 지하철_노선 + "/sections?stationId=" + 노선에_속한_하행역)
                .then().log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("하행 종점역이 아닌 경우 지하철 구간 삭제 실패")
    @Test
    void invalidDeleteSection2() {
        // given
        지하철_구간_생성_요청(지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속한_하행역));
        지하철_구간_생성_요청(지하철_구간_데이터_생성(노선에_속한_하행역, 노선에_속하지_않은_새로운역));

        // when
        final ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when()
                .delete("/lines/" + 지하철_노선 + "/sections?stationId=" + 노선에_속한_하행역)
                .then().log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("등록된 구간으로 역 목록 조회 기능")
    @Test
    void name() {
        // given
        지하철_구간_생성_요청(지하철_구간_데이터_생성(노선에_속한_상행역, 노선에_속한_하행역));
        지하철_구간_생성_요청(지하철_구간_데이터_생성(노선에_속한_하행역, 노선에_속하지_않은_새로운역));

        // when
        final ExtractableResponse<Response> getResponse = 지하철_노선_목록요청("/lines/" + 지하철_노선);

        // then
        final List<String> resultList = getResponse.jsonPath()
                .getList("stations.name")
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        assertThat(resultList).isEqualTo(Arrays.asList(상행역, 하행역, 새로운역));
    }


    private Long 지하철_역_데이터_생성(String name) {
        return Long.valueOf(
                StationUtils.지하철_역_생성_요청(StationUtils.지하철_역_데이터_생성(name))
                        .jsonPath()
                        .get("id")
                        .toString()
        );
    }

    private Map<String, Object> 지하철_구간_데이터_생성(Long upStationId, Long downStationId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 거리);
        return params;
    }

    private ExtractableResponse<Response> 지하철_구간_생성_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + 지하철_노선 + "/sections")
                .then().log().all()
                .extract();
    }
}
