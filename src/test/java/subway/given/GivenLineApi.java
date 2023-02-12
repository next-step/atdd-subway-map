package subway.given;

import io.restassured.response.*;
import org.springframework.http.*;

import java.util.*;

import static io.restassured.RestAssured.*;

public class GivenLineApi {

    public static final String LINE_PATH = "/lines";
    public static final String BG_COLOR_600 = "bg-color-600";

    public static final String LINE_1 = "신분당선";
    public static final String LINE_2 = "분당선";

    public static final Long LINE_ID_1 = 1L;

    public static final Long STATION_ID_1 = 1L;
    public static final Long STATION_ID_2 = 2L;
    public static final Long STATION_ID_3 = 3L;

    public static ExtractableResponse<Response> createLine(
            final String name,
            final Long upStationId,
            final Long downStationId
    ) {

        final var params = new HashMap<>();
        params.put("name", name);
        params.put("color", BG_COLOR_600);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", "10");

        // When
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(LINE_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getLineById(final Long lineId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(LINE_PATH + "/" + lineId)
                .then().log().all()
                .extract();
    }
}
