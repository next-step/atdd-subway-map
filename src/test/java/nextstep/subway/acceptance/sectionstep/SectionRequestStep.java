package nextstep.subway.acceptance.sectionstep;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionRequestStep {
    public static void 응답_상태_검증(ExtractableResponse<Response> postResponse, HttpStatus status) {
        assertThat(postResponse.statusCode()).isEqualTo(status.value());
    }

    public static void 구간_개수_검증(Long 구간Id, int size) {
        ExtractableResponse<Response> getResponse = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/lines/" + 구간Id + "/sections")

                .then().log().all()
                .extract();

        assertThat(((List<Object>) getResponse.body().jsonPath().get()).size()).isEqualTo(size);
    }

    public static ExtractableResponse<Response> 구간_생성_요청(Long 상행선Id, Long 하행선Id, int 거리, Long 노선Id) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("downStationId", String.valueOf(하행선Id));
        requestBody.put("upStationId", String.valueOf(상행선Id));
        requestBody.put("distance", 거리);

        return RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL_VALUE)

                .when()
                .post("/lines/" + 노선Id + "/sections")

                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(Long 대상_노선Id, Long 대상_지하철역Id) {
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .accept(MediaType.ALL_VALUE)

                .when()
                .delete("/lines/" + 대상_노선Id + "/sections?stationId=" + 대상_지하철역Id)

                .then().log().all()
                .extract();
        return deleteResponse;
    }
}
