package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineTest {
    /**
     * When 지하철노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createSubwayLine() {
        //When 지하철노선을 생성하면
        LineRequest Line1 = new LineRequest("1호선", "blue", 1L,2L,10L);
        createSubwayLine(Line1);

        //Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        assertThat(getAllSubwayLines()).containsAnyOf(Line1.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getSubwayLineWithCreatedSubwayLines() {

        // 데이터 무결성을 위함.
        deleteAllSubway();

        // Given 2개의 지하철 노선을 생성하고
        LineRequest Line1 = new LineRequest("1호선", "blue", 1L,2L,10L);
        createSubwayLine(Line1);
        LineRequest Line5 = new LineRequest("5호선", "purple", 3L, 4L, 20L);
        createSubwayLine(Line5);

        // When 지하철 노선 목록을 조회하면 // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(getAllSubwayLines()).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getStationLine() {

        // Given 지하철 노선을 생성하고
        LineRequest Line8 = new LineRequest("8호선", "pink", 6L,10L,200L);
        Long lineId = createSubwayLine(Line8);

        // When 생성한 지하철 노선을 조회하면
        LineResponse lineResponse = getSubwayLine(lineId);

        // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertThat(Line8.getName()).isEqualTo(lineResponse.getName());
        assertThat(Line8.getColor()).isEqualTo(lineResponse.getColor());
        assertThat(Line8.getUpStationId()).isEqualTo(lineResponse.getUpStationId());
        assertThat(Line8.getDownStationId()).isEqualTo(lineResponse.getDownStationId());
        assertThat(Line8.getDistance()).isEqualTo(lineResponse.getDistance());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateSubwayLine() {

        // Given 지하철 노선을 생성하고
        LineRequest Line8 = new LineRequest("8호선", "pink", 6L,10L,200L);
        Long lineId = createSubwayLine(Line8);

        // When 생성한 지하철 노선을 수정하면
        LineRequest updateLine8 = new LineRequest(Line8.getName(), "black", Line8.getUpStationId(),4L,100L);
        LineResponse lineResponse = updateSubwayLine(lineId, updateLine8);

        // Then 해당 지하철 노선 정보는 수정된다
        assertThat(updateLine8.getName()).isEqualTo(lineResponse.getName());
        assertThat(updateLine8.getColor()).isEqualTo(lineResponse.getColor());
        assertThat(updateLine8.getUpStationId()).isEqualTo(lineResponse.getUpStationId());
        assertThat(updateLine8.getDownStationId()).isEqualTo(lineResponse.getDownStationId());
        assertThat(updateLine8.getDistance()).isEqualTo(lineResponse.getDistance());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteSubwayLine() {

        // Given 지하철 노선을 생성하고
        LineRequest Line7 = new LineRequest("7호선","green", 3L, 5L, 30L);
        Long lineId = createSubwayLine(Line7);

        // When 생성한 지하철 노선을 삭제하면 // Then 해당 지하철 노선 정보는 삭제된다
        deleteSubwayLine(lineId);
        //assertThat(getAllSubwayLines()).doesNotContain(Line7.getName());
    }

    private Long createSubwayLine(LineRequest lineRequest) {
        // when 지하철역을 생성하면

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(lineRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then 지하철역이 생성된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 지하철역의 ID 가 응답 된다
        LineResponse stationLine = response.body().as(LineResponse.class);
        return stationLine.getId();
    }

    private LineResponse getSubwayLine(Long id) {

        //Given 노선 id 을 입력 받으면
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines/" + id)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then 노선 정보가 응답 된다.
        return response.body().as(LineResponse.class);
    }

    private List<String> getAllSubwayLines() {

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList("name", String.class);
    }

    private void deleteAllSubway() {
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void deleteSubwayLine(Long id) {
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/lines/"+id)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private LineResponse updateSubwayLine(Long id, LineRequest lineRequest) {

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(lineRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().put("/lines/"+id)
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.body().as(LineResponse.class);
    }
}
