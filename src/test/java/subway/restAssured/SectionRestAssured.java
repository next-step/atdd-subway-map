package subway.restAssured;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.SectionRequestTestDto;

import java.util.HashMap;
import java.util.Map;

public class SectionRestAssured {

    public ExtractableResponse<Response> save(Long id, SectionRequestTestDto sectionRequestTestDto) {
        Map<String, String> sectionParams = new HashMap<>();
        putParams(sectionParams, sectionRequestTestDto);

        return saveSection(id, sectionParams);
    }

    public ExtractableResponse<Response> delete(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId + "/sections/" + stationId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> saveSection(Long id, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + id + "/sections/")
                .then().log().all()
                .extract();
    }

    private void putParams(Map<String, String> params, SectionRequestTestDto sectionRequestTestDto) {
        params.put("upStationId", String.valueOf(sectionRequestTestDto.getUpStationId()));
        params.put("downStationId", String.valueOf(sectionRequestTestDto.getDownStationId()));
        params.put("distance", String.valueOf(sectionRequestTestDto.getDistance()));
    }
}
