package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.SectionResponse;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

public class LineVerificationStep {

    public static void 지하철_노선_등록됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, CREATED);
    }

    public static void 지하철_노선_목록조회됨(Long blueLineId, Long redLineId, ExtractableResponse<Response> response) {
        응답코드_확인(response, OK);
        assertThat(getLineIds(response)).containsAll(Arrays.asList(blueLineId, redLineId));
    }

    public static void 지하철_노선_조회됨(Long lineId, ExtractableResponse<Response> response) {
        응답코드_확인(response, OK);
        assertThat(response.body().as(SectionResponse.class)).isEqualTo(lineId);
    }

    public static void 지하철_노선_수정됨(Long lineId, ExtractableResponse<Response> response) {
        응답코드_확인(response, OK);
        assertThat(getLineId(response)).isEqualTo(lineId);
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, NO_CONTENT);
    }

    private static void 응답코드_확인(ExtractableResponse<Response> response, HttpStatus created) {
        assertThat(response.statusCode()).isEqualTo(created.value());
    }

    private static List<Long> getLineIds(ExtractableResponse<Response> response) {
        return response.body().jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    private static long getLineId(ExtractableResponse<Response> response) {
        return response.body().as(LineResponse.class).getId();
    }
}
