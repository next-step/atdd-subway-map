package nextstep.subway.acceptance.linestep;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.testenum.TestLine;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class LineValidateStep {
    private final static String BODY_ELEMENT_NAME = "name";
    private final static String BODY_ELEMENT_COLOR = "color";

    public static void 노선_응답_검증(ExtractableResponse<Response> response, HttpStatus status, TestLine 노선) {
        응답_상태코드_검증(response, status);
        응답_노선_검증(response, 노선.getName(), 노선.getColor());
    }

    public static void 노선_목록_조회_응답_검증(ExtractableResponse<Response> response, HttpStatus status,
                                      TestLine 노선1, TestLine 노선2) {
        응답_상태코드_검증(response, status);
        응답_노선_여러개_검증(response, 노선1, 노선2);
    }

    public static void 노선_응답_상태_검증(ExtractableResponse<Response> response, HttpStatus status) {
        응답_상태코드_검증(response, status);
    }

    private static void 응답_상태코드_검증(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private static void 응답_노선_검증(ExtractableResponse<Response> response, String name, String color) {
        assertThat(response.body().jsonPath().getString(BODY_ELEMENT_NAME)).isEqualTo(name);
        assertThat(response.body().jsonPath().getString(BODY_ELEMENT_COLOR)).isEqualTo(color);
    }

    private static void 응답_노선_여러개_검증(ExtractableResponse<Response> response, TestLine 노선1, TestLine 노선2) {
        assertThat(response.body().jsonPath().getList(BODY_ELEMENT_NAME))
                .containsExactly(노선1.getName(), 노선2.getName());
        assertThat(response.body().jsonPath().getList(BODY_ELEMENT_COLOR))
                .containsExactly(노선1.getColor(), 노선2.getColor());
    }
}
