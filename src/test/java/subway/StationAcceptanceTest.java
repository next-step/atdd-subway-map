package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private static ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철역을_생성한다(String 역이름) {
        Map<String, String> params = new HashMap<>();
        params.put("name", 역이름);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지할척역_제거한다(int stationId) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .extract();
    }


    private static List<String> 지하철역_목록의_이름을_파싱한다(ExtractableResponse<Response> 지하철역_목록_조회_결과) {
        return 지하철역_목록_조회_결과.jsonPath().getList("name", String.class);
    }

    private static int 지하철역의_아이디를_파싱한다(ExtractableResponse<Response> 지하철역_조회_결과) {
        return 지하철역_조회_결과.jsonPath().getInt("id");
    }

    private static List<Integer> 지하철역_목록에서_아이디를_파싱한다(ExtractableResponse<Response> 지하철역_목록_조회_결과) {
        return 지하철역_목록_조회_결과.jsonPath().getList("id", Integer.class);
    }


    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var 강남역 = 지하철역을_생성한다("강남역");

        // then
        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> 지하철역_목록_이름 = 지하철역_목록의_이름을_파싱한다(지하철역_목록_조회());
        assertThat(지하철역_목록_이름).containsAnyOf("강남역");
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void searchSubwayStationList() {
        // given
        지하철역을_생성한다("강남역");
        지하철역을_생성한다("이수역");

        // when
        List<String> 지하철역_목록_이름 = 지하철역_목록의_이름을_파싱한다(지하철역_목록_조회());

        // then
        assertThat(지하철역_목록_이름).contains("강남역", "이수역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거")
    @Test
    public void removeSubwayStation() {
        // given
        var 강남역 = 지하철역을_생성한다("강남역");
        int 강남역_아이디 = 지하철역의_아이디를_파싱한다(강남역);

        // when
        지할척역_제거한다(강남역_아이디);

        // then
        List<Integer> 지할철역_목록_아이디 = 지하철역_목록에서_아이디를_파싱한다(지하철역_목록_조회());
        assertThat(지할철역_목록_아이디).doesNotContain(강남역_아이디);
    }

}
