package subway.helper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTestAssuredHelper {


    public static void 지하철노선_검증하기(ExtractableResponse<Response> createResponse, ExtractableResponse<Response> getResponse){
        assertThat(createResponse.jsonPath().getString("name")).isEqualTo(getResponse.jsonPath().getString("name"));
        assertThat(createResponse.jsonPath().getString("color")).isEqualTo(getResponse.jsonPath().getString("color"));
        // TODO : nested 배열도 비교하는 법 조사해서 코드 채우기
    }

    public static void 지하철노선_검증하기(ExtractableResponse<Response> getResponse, String name, String color) {
        assertThat(getResponse.jsonPath().getList("name", String.class)).containsAnyOf(name);
        assertThat(getResponse.jsonPath().getList("color", String.class)).containsAnyOf(color);
    }

    public static void 지하철노선_수정_검증(String changeResponse, String station, String changeResponse1, String color) {
        assertThat(changeResponse).isEqualTo(station);
        assertThat(changeResponse1).isEqualTo(color);
    }

    public static void 지하철노선_삭제_검증하기(ExtractableResponse<Response> getResponse, String name, String color) {
        assertThat(getResponse.jsonPath().getList("name", String.class)).doesNotContain(name);
        assertThat(getResponse.jsonPath().getList("color", String.class)).doesNotContain(color);
    }
}
