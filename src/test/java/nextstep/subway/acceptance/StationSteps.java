package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.utils.RestAssuredRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StationSteps {
    private static final String STATIONS_PATH = "/stations";

    public static final Map<String, String> 강남역 = new HashMap<>();
    public static final Map<String, String> 역삼역 = new HashMap<>();
    public static final Map<String, String> 양재역 = new HashMap<>();
    public static final Map<String, String> 판교역 = new HashMap<>();

    static {
        강남역.put("name", "강남역");
        역삼역.put("name", "역삼역");
        양재역.put("name", "양재역");
        판교역.put("name", "판교역");
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> params) {
        return post(params, STATIONS_PATH);
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return get(STATIONS_PATH);
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(String uri) {
        return delete(uri);
    }

    public static ExtractableResponse<Response> 지하철역_등록되어_있음(Map<String, String> params) {
        ExtractableResponse<Response> response = 지하철역_생성_요청(params);
        지하철역_생성됨(response);

        return response;
    }

    public static void 지하철역_생성됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_목록_조회됨(ExtractableResponse<Response> response, Map<String, String>... params) {
        응답_요청_확인(response, HttpStatus.OK);
        List<String> stationNames = response.jsonPath().getList("name");
        List<String> names = Arrays.stream(params)
                .map(param -> param.get("name"))
                .collect(Collectors.toList());
        assertThat(stationNames).isEqualTo(names);
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.NO_CONTENT);
    }

    public static void 지하철역_이름_중복됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.CONFLICT);
    }

    public static void 응답_요청_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
