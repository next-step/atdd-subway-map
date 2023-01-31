package line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.sound.sampled.Line;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private final static LineRequest 신분당선 = LineRequest.of(
            "신분당선", "bg-red-600", "1", "2", "10");
    private final static LineRequest 분당선 = LineRequest.of(
            "분당선", "bg-green-600", "1", "3", "15");

    /**
     * when 지하철 노선을 생성한다.
     * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void 노선_생성() {
        //when
        createLine(신분당선);
        List<String> lineNames = getLineNames()
                .jsonPath().getList("name", String.class);

        //Then
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * when 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void 노선_목록_조회() {
        //Given
        createLine(신분당선);
        createLine(분당선);

        //When
        List<String> lineNames = getLineNames()
                .jsonPath().getList("name", String.class);

        //Then
        assertThat(lineNames).containsAll(List.of("신분당선", "분당선"));
        assertThat(lineNames).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void 노선_조회() {
        //Given
        Long id = getCreatedLineId(신분당선);

        //When
        ExtractableResponse<Response> response = readLine(id);

        //Then
        String name = response.jsonPath().get("name");
        String color = response.jsonPath().get("color");
        assertThat(name).isEqualTo("신분당선");
        assertThat(color).isEqualTo("bg-red-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void 노선_수정() {
        //Given
        long updateId = getCreatedLineId(신분당선);

        //When
        ExtractableResponse<Response> response = updateLine(updateId, "다른분당선", "bg-red-600");

        //Then
        String name = response.jsonPath().get("name");
        String color = response.jsonPath().get("color");
        assertThat(name).isEqualTo("다른분당선");
        assertThat(color).isEqualTo("bg-red-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void 노선_삭제() {
        //Given
        long deleteId = getCreatedLineId(신분당선);
        createLine(분당선);

        //When
        ExtractableResponse<Response> response = deleteLine(deleteId);

        //Then
        List<String> names = response.jsonPath().getList("name", String.class);
        assertThat(names).doesNotContain("신분당선")

    }

    private static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete("Line/{id}", id)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> createLine(LineRequest request) {

        Map<String, String> params = makeRequestBody(request);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> makeRequestBody(LineRequest request) {
        Map<String, String> params = new HashMap<>();

        params.put("name", request.getName());
        params.put("color", request.getColor());
        params.put("upStationId", request.getUpStationId());
        params.put("downStationId", request.getDownStationId());
        params.put("distance", request.getDistance());

        return params;
    }

    private static long getCreatedLineId(LineRequest request) {
        ExtractableResponse<Response> createResponse = createLine(request);
        long updateId = createResponse.jsonPath().getLong("id");
        return updateId;
    }

    private static ExtractableResponse<Response> getLineNames() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> readLine(Long id) {
        return RestAssured.given().log().all()
                .when().get("lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> updateLine(Long id, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
}
