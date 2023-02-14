package subway.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpStatus;

public class ResponseUtils {

    public static void 적절한_응답_코드를_받을_수_있다(ExtractableResponse<Response> response,
        HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static Long ID_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    public static void n개의_이름_목록을_조회할_수_있다(ExtractableResponse<Response> response, String[] names) {
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames.size()).isEqualTo(names.length);
        assertThat(stationNames).containsAnyOf(names);
    }

    public static void 목록에서_이름을_찾을_수_없다(ExtractableResponse<Response> response, String name) {
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain(name);
    }

}
