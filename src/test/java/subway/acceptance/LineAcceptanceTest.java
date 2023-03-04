package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import subway.utils.DatabaseCleanup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.steps.LineSteps.*;
import static subway.steps.StationSteps.지하철_역_생성_요청;

@DisplayName("지하철 노선 관련 기능")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    private static Long GANG_NAM_STATION;
    private static Long YANG_JAE_STATION;
    private static Long SEO_HYEON_STATION;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        GANG_NAM_STATION = 지하철_역_생성_요청("강남역");
        YANG_JAE_STATION = 지하철_역_생성_요청("양재역");
        SEO_HYEON_STATION = 지하철_역_생성_요청("서현역");
    }

    /*
    * 지하철 노선 생성
    * when 지하철 노선을 생성하면
    * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
    * */
    @Test
    void 지하철_노선_생성_인수_테스트() {
        //when
        지하철_노선_생성_요청("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);

        List<String> lineList = 지하철_노선_목록_조회_요청();

        assertThat(lineList).containsAnyOf("신분당선");
    }




    /*
    * 지하철 노선 목록 조회
    * given 2개의 지하철 노선을 생성하고
    * when 지하철 노선 목록을 조회하면
    * then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    * */

    @Test
    void 지하철_노선_목록_조회_인수_테스트() {
        //given
        지하철_노선_생성_요청("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);
        지하철_노선_생성_요청("분당선", "bg-green-600", GANG_NAM_STATION, SEO_HYEON_STATION, 10);

        //when
        List<String> lineList = 지하철_노선_목록_조회_요청();

        //then
        assertThat(lineList).containsExactly("신분당선", "분당선");
    }

    /*
    * given 지하철 노선을 생성하고
    * when 생성한 지하철 노선을 조회하면
    * then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    * */

    @Test
    void 지하철_노선_조회_인수_테스트() {
        //given
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);
        var id = response.jsonPath().getLong("id");
        //when
        List<String> line = 지하철_노선_단건_조회_요청(id).getList("stations.name", String.class);
        //then
        assertAll(
                () -> assertThat(line).containsAnyOf("강남역"),
                () -> assertThat(getJson(id).get(0)).isEqualTo("신분당선"),
                () -> assertThat(getJson(id).get(1)).isEqualTo("bg-red-600")
        );
    }




    /*
    * 지하철노선 수정
    * given 지하철 노선을 생성하고
    * when 생성한 지하철 노선을 수정하면
    * then 해당 지하철 노선 정보는 수정된다.
    * */

    @DisplayName("지하철노선 수정 인수테스트")
    @Test
    void 지하철_노선_수정_인수_테스트() {
        //given
        ExtractableResponse<Response> createResponse
                = 지하철_노선_생성_요청("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);
        var id = createResponse.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> patchResponse = 지하철_노선_수정_요청("다른분당선", "bg-red-600", id);
        assertThat(patchResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        assertAll(
                () -> assertThat(getJson(id).get(0)).isEqualTo("다른분당선"),
                () -> assertThat(getJson(id).get(1)).isEqualTo("bg-red-600")
        );
    }

    private List<String> getJson(Long id) {
        return List.of(지하철_노선_단건_조회_요청(id).getString("name"), 지하철_노선_단건_조회_요청(id).getString("color"));
    }




    /*
    * Given 지하철 노선을 생성하고
      When 생성한 지하철 노선을 삭제하면
      Then 해당 지하철 노선 정보는 삭제된다
    * */

    @DisplayName("지하철노선 삭제 인수테스트")
    @Test
    void 지하철_노선_삭제_인수_테스트() {

        //given
        var id = 지하철_노선_생성_요청("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }




}
