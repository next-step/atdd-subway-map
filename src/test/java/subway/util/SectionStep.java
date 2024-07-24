package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.dto.ErrorResponse;
import subway.section.dto.SectionRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SectionStep {

    public static List<String> 지하철_구간_등록(Long lineId, SectionRequest sectionRequest) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/"+ lineId +"/sections")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

    }

    public static ErrorResponse 지하철_구간_등록_실패(Long lineId, SectionRequest sectionRequest) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract().as(ErrorResponse.class);
    }

    public static void 지하철_구간_삭제(Long lineId, Long stationId) {

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/"+ lineId +"/sections?stationId=" + stationId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ErrorResponse 지하철_구간_삭제_실패(Long lineId, Long stationId) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/"+ lineId +"/sections?stationId=" + stationId)
                .then().log().all()
                .extract().as(ErrorResponse.class);
    }
}

