package subway.restAssured;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.SectionRequestTestDto;

import java.util.HashMap;
import java.util.Map;

public class SectionRestAssured {

    public Long saveSuccess(Long id, SectionRequestTestDto sectionRequestTestDto) {
        Map<String, String> sectionParams = new HashMap<>();
        putParams(sectionParams, sectionRequestTestDto);

        return saveSectionSuccess(id, sectionParams);
    }

    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록 되어있는 하행 종점역이 아닌 경우 -> Http 404 Error")
    public ExtractableResponse<Response> saveFailCaseOne(Long id, SectionRequestTestDto sectionRequestTestDto) {
        Map<String, String> sectionParams = new HashMap<>();
        putParams(sectionParams, sectionRequestTestDto);

        return saveSectionFail(id, sectionParams);
    }

    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어있는 역인 경우 -> Http 404 Error")
    public ExtractableResponse<Response> saveFailCaseTwo(Long id, SectionRequestTestDto sectionRequestTestDto) {
        Map<String, String> sectionParams = new HashMap<>();
        putParams(sectionParams, sectionRequestTestDto);

        return saveSectionFail(id, sectionParams);
    }

    public void deleteSuccess(Long lineId, Long stationId) {
        RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId + "/sections/" + stationId)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("삭제할 지하철 역(하행 종정점)이 마지막이 구간이 아닌 경우 -> Http 404 Error")
    public void deleteFailCaseOne(Long lineId, Long stationId) {
        deleteFail(lineId, stationId);
    }

    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우 -> Http 404 Error")
    public void deleteFailCaseTwo(Long lineId, Long stationId) {
        deleteFail(lineId, stationId);
    }

    public void deleteFail(Long lineId, Long stationId) {
        RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId + "/sections/" + stationId)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Long saveSectionSuccess(Long id, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + id + "/sections/")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject("id", Long.class);
    }

    private ExtractableResponse<Response> saveSectionFail(Long id, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + id + "/sections/")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.NOT_FOUND.value())
                .extract();
    }

    private void putParams(Map<String, String> params, SectionRequestTestDto sectionRequestTestDto) {
        params.put("upStationId", String.valueOf(sectionRequestTestDto.getUpStationId()));
        params.put("downStationId", String.valueOf(sectionRequestTestDto.getDownStationId()));
        params.put("distance", String.valueOf(sectionRequestTestDto.getDistance()));
    }
}
