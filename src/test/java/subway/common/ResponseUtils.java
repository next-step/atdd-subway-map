package subway.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpStatus;

public class ResponseUtils {

    public static void httpStatus_확인(ExtractableResponse<Response> response,
        HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static Long ID_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    public static void 목록_개수_및_이름_확인(ExtractableResponse<Response> response, String[] stations) {
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames.size()).isEqualTo(stations.length);
        assertThat(stationNames).containsAnyOf(stations);
    }

    public static void 목록에_이름_없음(ExtractableResponse<Response> response, String station) {
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain(station);
    }

}
