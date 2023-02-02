package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;
import subway.station.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineRestAssuredTest {
    @DisplayName("지하철 노선을 삭제할 수 있다.")
    public static void deleteLine(Long lineId) {
        var response = RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선도를 조회할 수 있다.")
    public static List<LineResponse> getLineResponseList() {
        var response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.jsonPath().getList(".", LineResponse.class);
    }

    @DisplayName("지하철 노선을 등록할 수 있다.")
    public static Long createLine(LineCreateRequest lineCreateRequest) {
        var response = RestAssured.given().log().all()
                .body(lineCreateRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.jsonPath().getLong("id");
    }

    @DisplayName("입력한 지하철 노선도 정보와 조회한 지하철 노선도 정보를 비교할 수 있다.")
    public static void checkLine(LineCreateRequest lineCreateRequest, Long lineId, LineResponse lineResponse, Long stationId1, Long stationId2) {
        var stationIdList = lineResponse.getStationResponseList().stream().mapToLong(StationResponse::getId);

        assertAll(
                () -> assertThat(lineResponse.getId()).isEqualTo(lineId),
                () -> assertThat(lineResponse.getName()).isEqualTo(lineCreateRequest.getName()),
                () -> assertThat(stationIdList).containsAnyOf(stationId1, stationId2)
        );
    }

    @DisplayName("지하철 노선 아이디를 통해 해당 지하철 노선을 조회할 수 있다.")
    public static LineResponse getLine(Long lineId) {
        var response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.as(LineResponse.class);
    }

    @DisplayName("지하철 노선도를 수정할 수 있다.")
    public static void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineUpdateRequest)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
