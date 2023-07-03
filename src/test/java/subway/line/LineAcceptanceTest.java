package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationAcceptanceTest.지하철역을_생성한다;
import static subway.station.StationAcceptanceTest.지하철역의_아이디를_파싱한다;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private static ExtractableResponse<Response> 지하철_노선_등록한다(String 노선이름, String 노선색상,
            String 상행종점역, String 하행종점역, String 거리) {
        Map<String, String> params = new HashMap<>();
        params.put("name", 노선이름);
        params.put("color", 노선색상);
        params.put("upStationId", 상행종점역);
        params.put("downStationId", 하행종점역);
        params.put("distance", 거리);

        return RestAssured
                .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(params).log()
                .all()
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static int 아이디를_파싱한다(ExtractableResponse<Response> 신분당선) {
        return 신분당선.jsonPath().getInt("id");
    }

    private static List<Integer> 아이디_리스트를_파싱한다(ExtractableResponse<Response> 지하철_노선_목록) {
        return 지하철_노선_목록.jsonPath().getList("id", Integer.class);
    }

    private static List<String> 이름_리스트를_파싱한다(ExtractableResponse<Response> 지하철_노선_목록) {
        return 지하철_노선_목록.jsonPath().getList("name", String.class);
    }

    private static List<String> 컬러_리스트를_파싱한다(ExtractableResponse<Response> 지하철_노선_목록) {
        return 지하철_노선_목록.jsonPath().getList("color", String.class);
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회한다() {
        return RestAssured
                .given().accept(ContentType.JSON).log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createSubwayLine() {
        // given
        int 강남역_아이디 = 지하철역의_아이디를_파싱한다(지하철역을_생성한다("강남역"));
        int 판교역_아이디 = 지하철역의_아이디를_파싱한다(지하철역을_생성한다("판교역"));

        // when
        var response = 지하철_노선_등록한다(
                "신분당선",
                "bg-red-600",
                String.valueOf(강남역_아이디),
                String.valueOf(판교역_아이디),
                String.valueOf(10));

        // then
        assertThat(response.jsonPath().getInt("id")).isNotNull();
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.jsonPath().getList("stations.id", Integer.class))
                .contains(강남역_아이디, 판교역_아이디);
        assertThat(response.jsonPath().getList("stations.name"))
                .contains("강남역", "판교역");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void showLines() {
        // given
        int 강남역_아이디 = 아이디를_파싱한다(지하철역을_생성한다("강남역"));
        int 판교역_아이디 = 아이디를_파싱한다(지하철역을_생성한다("판교역"));
        int 사당역_아이디 = 아이디를_파싱한다(지하철역을_생성한다("사당역"));
        var 신분당선 = 지하철_노선_등록한다(
                "신분당선",
                "bg-red-600",
                String.valueOf(강남역_아이디),
                String.valueOf(판교역_아이디),
                String.valueOf(10));
        var _2호선 = 지하철_노선_등록한다(
                "2호선",
                "bg-red-500",
                String.valueOf(강남역_아이디),
                String.valueOf(사당역_아이디),
                String.valueOf(5));

        // when
        var 지하철_노선_목록 = 지하철_노선_목록_조회한다();

        // then
        assertThat(아이디_리스트를_파싱한다(지하철_노선_목록))
                .contains(아이디를_파싱한다(신분당선), 아이디를_파싱한다(_2호선));
        assertThat(이름_리스트를_파싱한다(지하철_노선_목록))
                .contains("신분당선", "2호선");
        assertThat(컬러_리스트를_파싱한다(지하철_노선_목록))
                .contains("bg-red-600", "bg-red-500");
        assertThat(지하철_노선_목록.jsonPath().getList("stations[0].id", Integer.class))
                .contains(강남역_아이디, 판교역_아이디);
        assertThat(지하철_노선_목록.jsonPath().getList("stations[0].name", String.class))
                .contains("강남역", "판교역");
        assertThat(지하철_노선_목록.jsonPath().getList("stations[1].id", Integer.class))
                .contains(강남역_아이디, 사당역_아이디);
        assertThat(지하철_노선_목록.jsonPath().getList("stations[1].name", String.class))
                .contains("강남역", "사당역");
    }

    private static ExtractableResponse<Response> 지하철노선_조회(int 지하철노선_아이디) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + 지하철노선_아이디)
                .then().log().all()
                .extract();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void searchLine() {
        // given
        int 강남역_아이디 = 아이디를_파싱한다(지하철역을_생성한다("강남역"));
        int 판교역_아이디 = 아이디를_파싱한다(지하철역을_생성한다("판교역"));
        var 신분당선 = 지하철_노선_등록한다(
                "신분당선",
                "bg-red-600",
                String.valueOf(강남역_아이디),
                String.valueOf(판교역_아이디),
                String.valueOf(10));
        int 신분당선_아이디 = 아이디를_파싱한다(신분당선);

        // when
        var 조회된_지하철노선 = 지하철노선_조회(신분당선_아이디);

        // then
        assertThat(아이디를_파싱한다(조회된_지하철노선)).isEqualTo(신분당선_아이디);
        assertThat(이름(조회된_지하철노선)).isEqualTo("신분당선");
        assertThat(컬러(조회된_지하철노선)).isEqualTo("bg-red-600");
        assertThat(스테이션_아이디리스트(조회된_지하철노선)).contains(강남역_아이디, 판교역_아이디);
        assertThat(스테이션_이름(조회된_지하철노선)).contains("강남역", "판교역");
    }

    private List<String> 스테이션_이름(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("stations.name", String.class);
    }

    private List<Integer> 스테이션_아이디리스트(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("stations.id", Integer.class);
    }

    private String 컬러(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("color");
    }

    private String 이름(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("name");
    }

}
