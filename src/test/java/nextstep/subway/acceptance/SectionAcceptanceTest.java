package nextstep.subway.acceptance;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.subway.acceptance.SubwayLineCommon.지하철_노선_등록_요청;
import static nextstep.subway.acceptance.SubwayLineCommon.지하철_노선_조회_요청;
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

@DisplayName("지하철 구간 관리 관련 기능")
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

        // then - 지하철 구간을 정상적으로 등록했다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then - 지하철 노선 목록 조회 시 생성한 구간을 찾을 수 있다
        response = 지하철_노선_조회_요청(1L);
        assertThat(response.jsonPath().getList("stations.name")).containsExactly("신논현역", "언주역", "선정릉역");
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
        지하철_구간_등록_실패됨(response);
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
        지하철_구간_등록_실패됨(response);
    }

    /**
     * Given 지하철 구간이 등록되고
     * When 지하철 역을 삭재하면
     * Then 하행 종점역인 경우, 정상적으로 삭제된다
     */
    @DisplayName("삭제 요청된 역이 지하철 노선의 하행 종점역인 경우, 정상 삭제처리 된다.")
    @Test
    void deleteSection() {
        // given - 지하철 구간을 등록한다
        신규역 = 지하철역_생성_요청("선정릉역").jsonPath().getLong("id");
        지하철_구간_등록_요청(신분당선, 하행역, 신규역, 8);

        // when - 지하철 역을 삭제요청 한다
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(신분당선, 신규역);

        // then - 삭제 요청한 지하철 역이 하행 종점역이므로 정상적으로 삭제된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 구간이 등록되고
     * When 지하철 역을 삭재하면
     * Then 하행 종점역이 아닌 경우, 잘못된 요청 처리가 된다
     */
    @DisplayName("삭제 요청된 역이 지하철 노선의 하행 종점역이 아닌 경우, 오류가 발생한다.")
    @Test
    void deleteSectionException() {
        // given - 지하철 구간을 등록한다
        신규역 = 지하철역_생성_요청("선정릉역").jsonPath().getLong("id");
        지하철_구간_등록_요청(신분당선, 하행역, 신규역, 8);

        // when - 지하철 역을 삭제요청 한다
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(신분당선, 상행역);

        // then - 삭제 요청한 지하철 역이 하행 종점역이 아니므로 오류 처리된다
        지하철_구간_삭제_실패됨(response);
    }

    /**
     * When 지하철 역을 삭제하면
     * Then 상행 종점역과 하행 종점역만 있는 경우, 잘못된 요청 처리가 된다
     */
    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우 역을 삭제할 수 없다.")
    @Test
    void notDeleteSectionWithOneSection() {
        // when - 지하철 역 삭제 요청을 한다
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(신분당선, 하행역);
        
        // then - 한 개의 구간만 잇는 경우, 잘못된 요청 오류가 발생한다
        지하철_구간_삭제_실패됨(response);
    }
    
    private ExtractableResponse<Response> 지하철_구간_삭제_요청(Long 신분당선, Long 신규역) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations?stationId={stationId}", 신분당선, 신규역)
                .then().log().all()
                .extract();
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

    private void 지하철_구간_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_구간_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
