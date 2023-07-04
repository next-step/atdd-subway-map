package subway.helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SubwayLineHelper {

    public static final String SUBWAY_LINE_API_URL = "/subway-lines";
    public static final Map<String, Object> SUBWAY_LIEN_PARAMETERS_1 = Map.of("name", "신분당선"
            , "color", "bg-red-600"
            , "upStationId", 1, "downStationId", 2
            , "distance", 10);
    public static final Map<String, Object> SUBWAY_LIEN_PARAMETERS_2 = Map.of("name", "분당선"
            , "color", "bg-green-600"
            , "upStationId", 3, "downStationId"
            , 4, "distance", 10);

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> parameters) {
        ExtractableResponse<Response> createSubwayLineApiResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(parameters)
                .when().log().all()
                    .post(SubwayLineHelper.SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract();

        return createSubwayLineApiResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        ExtractableResponse<Response> showSubwayLinesApiResponse = RestAssured
                .given().log().all()
                .when().log().all()
                    .get(SubwayLineHelper.SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract();

        return showSubwayLinesApiResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_정보_조회_요청(String url) {
        ExtractableResponse<Response> showSubwayLineApiResponse = RestAssured
                .given().log().all()
                .when().log().all()
                .get(url)
                .then().log().all()
                .extract();

        return showSubwayLineApiResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_정보_수정_요청(String url, Map<String, Object> updateRequest) {
        ExtractableResponse<Response> updateSubwayLineApiResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(updateRequest)
                .when().log().all()
                    .put(url)
                .then().log().all()
                .extract();

        return updateSubwayLineApiResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String url) {
        ExtractableResponse<Response> deleteSubwayLineApiResponse = RestAssured
                .given().log().all()
                .when().log().all()
                    .delete(url)
                .then().log().all()
                .extract();

        return deleteSubwayLineApiResponse;
    }
}