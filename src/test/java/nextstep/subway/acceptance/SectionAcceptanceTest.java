package nextstep.subway.acceptance;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.subway.acceptance.SubwayLineCommon.지하철_노선_등록_요청;
import static nextstep.subway.acceptance.SubwayStationCommon.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.utils.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 구관 관리 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private Long 상행역;
    private Long 하행역;
    private Long 신규역;
    private Long 신분당선;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
            databaseCleanUp.afterPropertiesSet();
        }

        databaseCleanUp.execute();

        상행역 = 지하철역_생성_요청("신논현역").jsonPath().getLong("id");
        하행역 = 지하철역_생성_요청("언주역").jsonPath().getLong("id");
        신분당선 = 지하철_노선_등록_요청("신분당선", "bg-red-600", 상행역, 하행역, 10).jsonPath().getLong("id");
    }

    /**
     * Given 지하철 역을 생성하고
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 목록 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createSection() {
        // given - 지하철 역을 생성한다
        신규역 = 지하철역_생성_요청("선정릉역").jsonPath().getLong("id");

        // when - 지하철 구간을 등록한다
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(신분당선, 하행역, 신규역, 8);

        // then - 지하철 노선 목록 조회 시 생성한 구간을 찾을 수 있다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 신규역을 생성하고
     * When 새로운 구간을 등록하면
     * Then 기등록된 구간의 하행 종점역과 다르므로 새로운 구간 등록에 실패한다
     */
    @DisplayName("새로운 구간을 등록하는 경우 상행역이 해당 노선에 기등록되어있는 하행 종점역과 다르다.")
    @Test
    void addNewUpStationNotMatchedPreviouslyDownStation() {
        // given -  지하철 구간을 등록한다
        신규역 = 지하철역_생성_요청("선정릉역").jsonPath().getLong("id");

        // when - 새로운 지하철 구간을 등록한다
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(신분당선, 상행역, 신규역, 8);

        
        // then - 기등록된 구간의 하행 종점역과 다르므로 새로운 구간 등록에 실패한다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 신규역을 생성하고
     * When 새로운 지하철 구간을 등록할 때
     * Then 신규 구간의 하행역은 해당 노선에 등록되어 있는 역일 수 없다
     */
    @DisplayName("신규 구간의 하행역은 해당 노선에 이미 등록된 역일 수 없다.")
    @Test
    void notContainDownStationInLine() {
        // given - 신규역을 생성한다
        신규역 = 지하철역_생성_요청("신논현역").jsonPath().getLong("id");

        // when - 신규 구간을 등록한다
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(신분당선, 하행역, 신규역, 8);

        // then - 신규 구간의 하행역은 해당 노선에 등록되어 있는 역인 경우, 잘못된 요청이다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_구간_등록_요청(Long 신분당선, Long 상행역, Long 하행역, int distance) {
        return RestAssured.given().log().all()
                .body(지하철_구간_등록_파라미터_생성(상행역, 하행역, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections", 신분당선)
                .then().log().all()
                .extract();
    }

    private Map<String, String> 지하철_구간_등록_파라미터_생성(Long 상행역, Long 하행역, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(상행역));
        params.put("downStationId", String.valueOf(하행역));
        params.put("distance", String.valueOf(distance));

        return params;
    }
}
