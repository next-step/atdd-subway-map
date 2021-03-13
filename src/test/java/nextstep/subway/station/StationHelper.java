package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StationHelper {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String name){
        return RestAssured.given().log().all()
                .body(파라미터_생성(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 파라미터_생성(String name){
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        return param;
    }

    public static Long 생성된_지하철역_ID_가져오기(ExtractableResponse<Response> createResponse){
        return Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_결과_요청(){
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static List<Long> 지하철역_목록_예상_아이디_리스트(ExtractableResponse<Response>... responses){
        return Arrays.stream(responses)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
    }

    public static List<Long> 지하철_노선_목록_결과_아이디_리스트(ExtractableResponse<Response> response){
        return response.jsonPath().getList(".", StationResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(String uri){
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 응답_204_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 응답_200_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 응답_400_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 응답_201_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
