package subway.acceptance.common.handler;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.acceptance.line.fixture.LineFixture;
import subway.acceptance.station.fixture.StationFixture;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;


/**
 * 지하철 관련 요청 핸들러
 */
public class RequestHandler {

    public static ExtractableResponse<Response> 생성_지하철_노선에_구간(Long lineId, Long upStationId, Long downStationId, int distance) {
        Map tmp = new HashMap();
        tmp.put("upStationId", upStationId);
        tmp.put("downStationId", downStationId);
        tmp.put("distance", distance);
        return RestAssured.given().log().all()
                .body(tmp)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 삭제_지하철_노선의_구간() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}/sections?stationId={id}", 1, 1)
                .then().log().all()
                .extract();
    }

    /**
     * 요청을 위한 URL 경로셋
     */
    private enum UrlPath {
        // 지하철역(Station) 관련
        지하철역_기본_경로("/stations"),
        지하철역_아이디_기본_경로(지하철역_기본_경로.경로(), "/{id}"),

        // 지하철 노선(Line) 관련
        지하철_노선_기본_경로("/lines"),
        지하철_노선_아이디_기본_경로(지하철_노선_기본_경로.경로(), "/{id}"),

        // 지하철 구간(Line Section) 관련
        지하철_구간_기본_경로("/lines/{id}/sections");

        UrlPath(String... tokens) {
            this.tokens = tokens;
        }

        private String[] tokens;

        private String 경로() {
            return String.join("", this.tokens);
        }
    }

    public static ExtractableResponse<Response> 생성_지하철역(StationFixture station) {
        UrlPath.지하철역_아이디_기본_경로.경로();
        return RestAssured.given().log().all()
                .body(station.toMap())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(UrlPath.지하철역_기본_경로.경로())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 삭제_지하철역(Long stationId) {
        return RestAssured.given().log().all()
                .when().delete(UrlPath.지하철역_아이디_기본_경로.경로(), stationId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 조회_지하철역_목록() {
        return RestAssured.given().log().all()
                .when().get(UrlPath.지하철역_기본_경로.경로())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 생성_지하철_노선(LineFixture lineFixture, StationFixture upStation, StationFixture downStation, int distance) {
        Long upStationId = 조회_생성한_지하철역_아이디(upStation);
        Long downStationId = 조회_생성한_지하철역_아이디(downStation);
        return 생성_지하철_노선(lineFixture, upStationId, downStationId, distance);
    }

    public static ExtractableResponse<Response> 생성_지하철_노선(LineFixture lineFixture, Long upStationId, Long downStationId, int distance) {
        return RestAssured.given().log().all()
                .body(lineFixture.toMap(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(UrlPath.지하철_노선_기본_경로.경로())
                .then().log().all()
                .extract();
    }

    public static Long 조회_생성한_지하철역_아이디(StationFixture station) {
        return 생성_지하철역(station)
                .body()
                .jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 수정_지하철_노선(Long id, LineFixture lineFixture) {
        return RestAssured.given().log().all()
                .body(lineFixture.toMap())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(UrlPath.지하철_노선_아이디_기본_경로.경로(), id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 삭제_지하철_노선(Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(UrlPath.지하철_노선_아이디_기본_경로.경로(), id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 조회_지하철_노선_목록() {
        return RestAssured.given().log().all()
                .when().get(UrlPath.지하철_노선_기본_경로.경로())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 조회_지하철_노선(Long id) {
        return RestAssured.given().log().all()
                .when().get(UrlPath.지하철_노선_아이디_기본_경로.경로(), id)
                .then().log().all()
                .extract();
    }

}