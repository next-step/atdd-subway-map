package nextstep.subway.line.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionVerifier {

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> expected) {
        assertAll(
                () -> assertThat(expected.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    public static void 지하철_노선에_지하철역_정렬됨(ExtractableResponse<Response> expected, List<Long> ids) {
        List<Long> stationIds = expected.as(LineResponse.class)
                                        .getStations().stream()
                                        .map(i -> i.getId())
                                        .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(ids);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선에_역_제거실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
