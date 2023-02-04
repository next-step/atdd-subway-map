package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.section.SectionCreateRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionRestAssuredTest {

    public static void createSection(Long lineId, SectionCreateRequest param) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    public static void deleteSection(Long lineId, Long stationId) {
        var response = RestAssured.given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
