package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("truncate_station.sql")
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineSaveRequestAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        // given
        String stationName = "강남역";
        LineSaveRequest request = LineSaveRequest.builder().name(stationName).color("bg-red-600").upStationId(1L)
                                                           .downStationId(2L).distance(10L).build();

        // when
        ExtractableResponse<Response> creationResponse = callApiToCreateLine(request);

        // then
        assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> names = callApiToGetLines().jsonPath().getList("name", String.class);
        assertThat(names.size()).isEqualTo(1);
        assertThat(names.get(0)).contains(stationName);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 단건 조회한다.")
    @Test
    void getSingleLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 정보를 수정한다.")
    @Test
    void modifyLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 정보를 삭제한다.")
    @Test
    void deleteLine() {

    }

    private static ExtractableResponse<Response> callApiToGetLines() {
        return RestAssured.given().log().all()
                          .when().get("/lines")
                          .then().log().all().extract();
    }

    private static ExtractableResponse<Response> callApiToGetSingleLine(Long lineId) {
        return RestAssured.given().log().all().pathParam("lienId", lineId)
                          .when().get("/lines/{lineId}")
                          .then().log().all().extract();
    }

    private static ExtractableResponse<Response> callApiToCreateLine(LineSaveRequest lineSaveRequest) {
        return RestAssured.given().log().all().body(lineSaveRequest).contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines")
                          .then().log().all().extract();
    }

    private static ExtractableResponse<Response> callApiToModifyLine(Long lineId, LineModifyRequest lineModifyRequest) {
        return RestAssured.given().log().all()
                            .pathParam("lienId", lineId).body(lineModifyRequest)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines/{lineId}")
                          .then().log().all().extract();
    }

    private static ExtractableResponse<Response> callApiToDeleteLine(Long lineId) {
        return RestAssured.given().log().all().pathParam("lineId", lineId)
                          .when().delete("/lines/{lineId}")
                          .then().log().all().extract();
    }
}
