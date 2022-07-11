package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

/**
 * 지하철 노선 관련 API 호출 관련 클래스
 */
public class SubwayLineCallApi {
    /**
     * 지하철 노선 저장
     * @param params
     * @return
     */
    public ExtractableResponse<Response> saveSubwayLine(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선 수정
     * @param id
     * @param params
     * @return
     */
    public ExtractableResponse<Response> modifySubwayLineById(Long id, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선 삭제
     * @param id
     * @return
     */
    public ExtractableResponse<Response> deleteSubwayLineById(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선 목록 조회
     * @return
     */
    public ExtractableResponse<Response> findSubwayLines() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선 조회
     * @param id
     * @return
     */
    public ExtractableResponse<Response> findSubwayLineById(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 저장
     * @param params
     * @return
     */
    public ExtractableResponse<Response> saveStation(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 구간 저장
     * @param lineId
     * @param params
     * @return
     */
    public ExtractableResponse<Response> saveSubwaySection(Long lineId, Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 구간 삭제
     * @param lineId
     * @param stationId
     * @return
     */
    public ExtractableResponse<Response> deleteSubwaySectionById(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .param("stationId", stationId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }
}
