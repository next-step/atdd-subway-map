package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("지하철역 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql("/truncate.sql")  // 어떤 방식이 더 효율적인걸까..?
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationLineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO: 지하철 노선 생성 인수 테스트 메서드 생성
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // given
        ExtractableResponse<Response> response = saveStationLine("신분당선", "bg-red-600", 1L, 2L, 10L);

        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .when().get("/lines/{id}", 1)
//                .then().log().all()
//                .extract();

        // then
        String result = response.jsonPath().getString("name");
        Assertions.assertThat(result).isEqualTo("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO: 지하철 노선 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllStationLine() {
        // given
        saveStationLine("신분당선", "bg-red-600", 1L, 2L, 10L);
        saveStationLine("분당선", "bg-green-600", 1L, 3L, 10L);


        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        List<String> stations = response.jsonPath().getList("name", String.class);
        Assertions.assertThat(stations).containsExactly("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO: 지하철 노선 조회 테스트 메서드 생성
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStationLine() {
        // given
        saveStationLine("신분당선", "bg-red-600", 1L, 2L, 10L);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/{id}", 1)
                .then().log().all()
                .extract();

        // then
        String result = response.jsonPath().getString("name");
        Assertions.assertThat(result).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO: 지하철 노선 수정 인수 테스트 메서드 생성
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given
        saveStationLine("신분당선", "bg-red-600", 1L, 2L, 10L);
        Map<String, String> params = new HashMap<>();
        params.put("name", "다른 분당선");
        params.put("color", "bg-red-600");

        // when
        RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value()); // 상태 코드로 1차 검증

        // then (직접 조회해서 2차 검증)
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/{id}", 1)
                .then().log().all()
                .extract();
        Assertions.assertThat(response.jsonPath().getString("name")).isEqualTo("다른 분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO: 지하철 노선 삭제 인수 테스트 메서드 생성
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        saveStationLine("신분당선", "bg-red-600", 1L, 2L, 10L);

        // when
        RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", 1)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> saveStationLine(String name, String color, long upStationId, long downStationId, long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
