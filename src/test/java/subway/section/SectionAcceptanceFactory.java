package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;

public class SectionAcceptanceFactory {

    private static final String SECTION_BASE_URL = "/lines/1/sections";


    public static ExtractableResponse<Response> createSection(
            String downStationId,
            String upStationId,
            int distance
    ) {
        HashMap<String, Object> params = createSectionHashMap(
                Long.parseLong(downStationId),
                Long.parseLong(upStationId),
                distance
        );

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(SECTION_BASE_URL)
                .then().log().all()
                .extract();
    }

    private static HashMap<String, Object> createSectionHashMap(
            long downStationId,
            long upStationId,
            int distance
    ) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return params;
    }
}
