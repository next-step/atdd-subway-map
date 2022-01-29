package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.StationUtils.*;
import static nextstep.subway.utils.LineUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long 노선에_속한_상행역;
    private Long 노선에_속한_하행역;
    private Long 노선에_속하지_않은_새로운역;
    private static final Integer 거리 = 1;

    @BeforeEach
    public void setup() {
        노선에_속한_상행역 = Long.valueOf(
                지하철_역_생성_요청(지하철_역_데이터_생성("상행역"))
                        .jsonPath()
                        .get("id")
                        .toString()
        );
        노선에_속한_하행역 = Long.valueOf(
                지하철_역_생성_요청(지하철_역_데이터_생성("하행역"))
                        .jsonPath()
                        .get("id")
                        .toString()
        );
        노선에_속하지_않은_새로운역 = Long.valueOf(
                지하철_역_생성_요청(지하철_역_데이터_생성("새로운역"))
                        .jsonPath()
                        .get("id")
                        .toString()
        );

        지하철_노선_생성요청(지하철_노선_데이터_생성("1호선", "blue", 노선에_속한_상행역, 노선에_속한_하행역, 거리));
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
        final Map<String, Object> params = new HashMap<>();
        params.put("downStationId", 노선에_속한_하행역);
        params.put("upStationId", 노선에_속하지_않은_새로운역);
        params.put("distance", 거리);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        // todo 기능 구현
    }

    /**
     * given 하행역으로 상행 종점이면서, 상행역으로 하행 종점인 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 에러 (새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.)
     */
    @DisplayName("잘못된 하행역으로 구간 생성 시 에러")
    @Test
    void createSectionException1() {
    }

    /**
     * given 상행역으로 상행 종점이면서, 새로운 역으로 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 에러 (새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.)
     */
    @DisplayName("잘못된 상행역으로 구간 생성 시 에러")
    @Test
    void createSectionException2() {
    }

    /**
     * given 상행역으로 상행 종점이면서, 하행 역으로 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 에러 (새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.)
     * then 구간 데이터 생성 에러 (새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.)
     */
    @DisplayName("잘못된 상,하행역으로 구간 생성 시 에러")
    @Test
    void createSectionException3() {
    }

    /**
     * given 새로운 역으로 상행 종점이면서, 상행 역으로 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 에러 (새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.)
     * then 구간 데이터 생성 에러 (새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.)
     */
    @Test
    void createSectionException4() {
    }

    /**
     * given 새로운 역으로 상행 종점이면서, 하행 역으로 구간 데이터 생성
     * when 구간 데이터 생성 요청
     * then 구간 데이터 생성 에러 (새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.)
     * then 구간 데이터 생성 에러 (새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.)
     */
    @Test
    void createSectionException5() {
    }
}
