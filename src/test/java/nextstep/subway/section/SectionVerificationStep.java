package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

public class SectionVerificationStep {

    public static void 지하철_구간_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    public static void 지하철_구간_등록실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    public static void 지하철_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.body().as(LineResponse.class).getSections().size()).isEqualTo(1);
    }
}
