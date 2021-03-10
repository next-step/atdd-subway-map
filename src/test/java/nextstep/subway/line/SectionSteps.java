package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.LineSteps.지하철_노선_생성요청;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionSteps {

    public static Long 지하철_노선_생성됨(LineRequest lineRequest) {
        LineResponse lineResponse = 지하철_노선_생성요청(lineRequest).as(LineResponse.class);
        Long lineId = lineResponse.getId();

        return lineId;
    }

    public static ExtractableResponse<Response> 지하철노선_구간_등록요청(Long lineId, SectionRequest request) {
        return RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(request)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static void 지하철노선_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 지하철노선_구간_제거요청(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }

    public static void 지하철노선_구간_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철노선_구간_등록실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
