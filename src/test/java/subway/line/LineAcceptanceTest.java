package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.JsonParser.아이디;
import static subway.JsonParser.아이디_리스트;
import static subway.JsonParser.이름;
import static subway.JsonParser.이름_리스트;
import static subway.JsonParser.컬러;
import static subway.JsonParser.컬러_리스트;
import static subway.station.StationAcceptanceTest.지하철역을_생성한다;

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
            int 상행종점역, int 하행종점역, int 거리) {
        Map<String, Object> params = new HashMap<>();
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

    public static ExtractableResponse<Response> 지하철_노선_목록_조회한다() {
        return RestAssured
                .given().accept(ContentType.JSON).log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private static List<String> 스테이션_이름_리스트(ExtractableResponse<Response> 지하철_노선_목록, int 위치) {
        return 지하철_노선_목록.jsonPath().getList("stations[" + 위치 + "].name", String.class);
    }

    private static List<Integer> 스테이션_아이디_리스트(ExtractableResponse<Response> 지하철_노선_목록, int 위치) {
        return 지하철_노선_목록.jsonPath().getList("stations[" + 위치 + "].id", Integer.class);
    }

    private static List<String> 스테이션_이름_리스트(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("stations.name", String.class);
    }

    private static List<Integer> 스테이션_아이디_리스트(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("stations.id", Integer.class);
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createSubwayLine() {
        // given
        int 강남역_아이디 = 아이디(지하철역을_생성한다("강남역"));
        int 판교역_아이디 = 아이디(지하철역을_생성한다("판교역"));

        // when
        var 등록된_지하철노선 = 지하철_노선_등록한다(
                "신분당선",
                "bg-red-600",
                강남역_아이디,
                판교역_아이디,
                10);

        // then
        assertThat(아이디(등록된_지하철노선)).isNotNull();
        assertThat(이름(등록된_지하철노선)).isEqualTo("신분당선");
        assertThat(컬러(등록된_지하철노선)).isEqualTo("bg-red-600");
        assertThat(스테이션_아이디_리스트(등록된_지하철노선)).contains(강남역_아이디, 판교역_아이디);
        assertThat(스테이션_이름_리스트(등록된_지하철노선)).contains("강남역", "판교역");
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
        int 강남역_아이디 = 아이디(지하철역을_생성한다("강남역"));
        int 판교역_아이디 = 아이디(지하철역을_생성한다("판교역"));
        int 사당역_아이디 = 아이디(지하철역을_생성한다("사당역"));
        var 신분당선 = 지하철_노선_등록한다(
                "신분당선",
                "bg-red-600",
                강남역_아이디,
                판교역_아이디,
                10);
        var _2호선 = 지하철_노선_등록한다(
                "2호선",
                "bg-red-500",
                강남역_아이디,
                사당역_아이디,
                5);

        // when
        var 지하철_노선_목록 = 지하철_노선_목록_조회한다();

        // then
        assertThat(아이디_리스트(지하철_노선_목록)).contains(아이디(신분당선), 아이디(_2호선));
        assertThat(이름_리스트(지하철_노선_목록)).contains("신분당선", "2호선");
        assertThat(컬러_리스트(지하철_노선_목록)).contains("bg-red-600", "bg-red-500");
        assertThat(스테이션_아이디_리스트(지하철_노선_목록, 0)).contains(강남역_아이디, 판교역_아이디);
        assertThat(스테이션_이름_리스트(지하철_노선_목록, 0)).contains("강남역", "판교역");
        assertThat(스테이션_아이디_리스트(지하철_노선_목록, 1)).contains(강남역_아이디, 사당역_아이디);
        assertThat(스테이션_이름_리스트(지하철_노선_목록, 1)).contains("강남역", "사당역");
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
        int 강남역_아이디 = 아이디(지하철역을_생성한다("강남역"));
        int 판교역_아이디 = 아이디(지하철역을_생성한다("판교역"));
        var 신분당선 = 지하철_노선_등록한다(
                "신분당선",
                "bg-red-600",
                강남역_아이디,
                판교역_아이디,
                10);
        int 신분당선_아이디 = 아이디(신분당선);

        // when
        var 조회된_지하철노선 = 지하철노선_조회(신분당선_아이디);

        // then
        assertThat(아이디(조회된_지하철노선)).isEqualTo(신분당선_아이디);
        assertThat(이름(조회된_지하철노선)).isEqualTo("신분당선");
        assertThat(컬러(조회된_지하철노선)).isEqualTo("bg-red-600");
        assertThat(스테이션_아이디_리스트(조회된_지하철노선)).contains(강남역_아이디, 판교역_아이디);
        assertThat(스테이션_이름_리스트(조회된_지하철노선)).contains("강남역", "판교역");
    }


}
