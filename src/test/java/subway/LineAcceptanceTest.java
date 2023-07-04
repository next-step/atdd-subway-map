package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private final static String 신분당선 = "신분당선";
    private final static String 분당선 = "분당선";
    private static final String 다른분당선 = "다른분당선";
    private final static String red = "bg-red-600";
    private final static String green = "bg-green-600";
    private final static long 지하철역_id = 1;
    private final static long 새로운지하철역_id = 2;
    private final static long 또다른지하철역_id = 3;
    private final static int distance = 10;


    /**
     * When 지하철 노선을 생성하면 <br> Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        //when
        ExtractableResponse<Response> response = 지하철역_노선_등록_요청(
            신분당선, red, 지하철역_id, 새로운지하철역_id, distance
        );

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(1L),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo(red),
            () -> assertThat(response.jsonPath().getList("stations")).hasSize(2),
            () -> assertThat(response.jsonPath().getList("stations.id")).contains(
                Long.valueOf(지하철역_id).intValue(),
                Long.valueOf(새로운지하철역_id).intValue()
            )
        );

        //Then
        assertThat(지하철역_노선_목록_조회_요청().jsonPath().getList("id"))
            .contains(Long.valueOf(response.jsonPath().getLong("id")).intValue());
    }

    private static ExtractableResponse<Response> 지하철역_노선_등록_요청(String name, String color
        , long upStationId, long downStationId, int distance) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 지하철역_노선_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    /**
     * Given 2개의 지하철 노선을 생성하고 <br> When 지하철 노선 목록을 조회하면 <br> Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getStationLines() {

        //given
        지하철역_노선_등록_요청(신분당선, red, 지하철역_id, 새로운지하철역_id, distance);
        지하철역_노선_등록_요청(분당선, green, 지하철역_id, 또다른지하철역_id, distance);

        //when
        ExtractableResponse<Response> response = 지하철역_노선_목록_조회_요청();

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("")).hasSize(2),
            () -> assertThat(response.jsonPath().getList("name")).containsOnly(신분당선, 분당선)
        );
    }

    /**
     * Given 지하철 노선을 생성하고 <br> When 생성한 지하철 노선을 조회하면 <br> Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    @DisplayName("지하철 노선 조회")
    @Test
    void getStationLine() {

        //given
        long 신규등록_노선_id = 지하철역_노선_등록_요청(신분당선, red, 지하철역_id, 새로운지하철역_id, distance).jsonPath()
            .getLong("id");

        //when
        ExtractableResponse<Response> response = 지하철역_노선_단건_조회(신규등록_노선_id);

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(1L),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo(red),
            () -> assertThat(response.jsonPath().getList("stations")).hasSize(2),
            () -> assertThat(response.jsonPath().getList("stations.id")).contains(
                Long.valueOf(지하철역_id).intValue(),
                Long.valueOf(새로운지하철역_id).intValue()
            )
        );

    }

    private static ExtractableResponse<Response> 지하철역_노선_단건_조회(long 신규_등록_노선_id) {
        return RestAssured
            .given().log().all()
            .when().get("/lines/{id}", 신규_등록_노선_id)
            .then().log().all()
            .extract();
    }

    /**
     * Given 지하철 노선을 생성하고 <br> When 생성한 지하철 노선을 수정하면 <br> Then 해당 지하철 노선 정보는 수정된다 <br>
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyStationLine() {

        //given
        long 신규등록_노선_id = 지하철역_노선_등록_요청(신분당선, red, 지하철역_id, 새로운지하철역_id, distance).jsonPath()
            .getLong("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", 다른분당선);
        params.put("color", red);

        //when
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("lines/{id}", 신규등록_노선_id)
            .then().log().all()
            .extract();

        //then
        ExtractableResponse<Response> response = 지하철역_노선_단건_조회(신규등록_노선_id);

        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(다른분당선),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo(red)
        );
    }

    /**
     * Given 지하철 노선을 생성하고 <br> When 생성한 지하철 노선을 삭제하면 <br> Then 해당 지하철 노선 정보는 삭제된다 <br>
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteStationLine() {

        //given
        long 신규_등록_노선_id = 지하철역_노선_등록_요청(신분당선, red, 지하철역_id, 새로운지하철역_id, distance).jsonPath()
            .getLong("id");

        //when
        RestAssured
            .given().log().all()
            .when().delete("lines/{id}", 신규_등록_노선_id)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> response = 지하철역_노선_단건_조회(신규_등록_노선_id);

        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("해당하는 id 에 맞는 지하철 노선이 존재하지 않습니다.")
        );

    }

}
