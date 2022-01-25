package nextstep.subway.acceptance.linestep;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.testenum.TestLine.LINE_NEW_BOONDANG;
import static nextstep.subway.acceptance.testenum.TestLine.LINE_TWO;
import static org.assertj.core.api.Assertions.assertThat;

public class LineValidateStep {
    private final static String BODY_ELEMENT_NAME = "name";
    private final static String BODY_ELEMENT_COLOR = "color";

    public static void 응답_상태코드_검증(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    public static void 응답_바디_각_요소_검증(ExtractableResponse<Response> response, String name, String color) {
        assertThat(response.body().jsonPath().getString(BODY_ELEMENT_NAME)).isEqualTo(name);
        assertThat(response.body().jsonPath().getString(BODY_ELEMENT_COLOR)).isEqualTo(color);
    }

    public static void 응답_바디_여러_요소_검증(ExtractableResponse<Response> response) {
        assertThat(response.body().jsonPath().getList(BODY_ELEMENT_NAME))
                .containsExactly(LINE_NEW_BOONDANG.getName(), LINE_TWO.getName());
        assertThat(response.body().jsonPath().getList(BODY_ELEMENT_COLOR))
                .containsExactly(LINE_NEW_BOONDANG.getColor(), LINE_TWO.getColor());
    }
}
