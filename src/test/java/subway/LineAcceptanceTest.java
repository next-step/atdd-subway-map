package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTest.지하철역을_생성한다;
import static subway.StationAcceptanceTest.지하철역의_아이디를_파싱한다;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
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


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createSubwayLine() {
        // given
        var 강남역 = 지하철역을_생성한다("강남역");
        int 강남역_아이디 = 지하철역의_아이디를_파싱한다(강남역);

        var 판교역 = 지하철역을_생성한다("판교역");
        int 판교역_아이디 = 지하철역의_아이디를_파싱한다(판교역);

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

}
