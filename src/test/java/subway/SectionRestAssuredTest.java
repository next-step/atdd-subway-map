package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.section.SectionCreateRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionRestAssuredTest {

    @DisplayName("지하철 노선 구간을 추가할 수 있다.")
    public static void createSection(Long lineId, SectionCreateRequest param) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 구간을 추가할 수 없다")
    public static void createSectionFail(Long lineId, SectionCreateRequest param) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 구간을 삭제할 수 있다.")
    public static void deleteSection(Long lineId, Long stationId) {
        var response = RestAssured.given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    @DisplayName("지하철 노선 구간을 삭제할 수 없다.")
    public static void deleteSectionFail(Long lineId, Long stationId) {
        var response = RestAssured.given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
