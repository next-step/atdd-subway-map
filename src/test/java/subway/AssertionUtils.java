package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public class AssertionUtils {

    // status code
    public static void 응답코드_201을_반환한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    public static void 응답코드_204를_반환한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    // list
    public static <T> void 목록은_다음을_포함한다(final List<T> list, T... obj) {
        assertThat(list).containsAnyOf(obj);
    }

    public static <T> void 목록은_다음을_포함하지_않는다(final List<T> list, final T... obj) {
        assertThat(list).doesNotContain(obj);
    }
}
