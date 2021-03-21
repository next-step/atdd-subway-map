package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SectionTestStep {
    public static void 지하철_구간_등록_확인(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long lineId, Long upStationId, Long downStationId, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().extract();
        return response;
    }
}
