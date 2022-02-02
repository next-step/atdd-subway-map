package nextstep.subway.acceptance.step;

import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    public static void 요청_성공(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());
    }

    public static void 요청_성공_컨텐츠_미제공(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 생성_성공(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 데이터_기존재로_실패(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 잘못된_데이터_전달로_실패(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
