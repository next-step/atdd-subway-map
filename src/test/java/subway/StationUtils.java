package subway;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationUtils {

    public static String GANG_NAM_STATION = "강남역";
    public static String SIN_SA_STATION = "신사역";
    public static String PAN_GYEO_STATION = "판교역";

    public static Map<String, Object> SIN_BUN_DANG_STATION_LINE = new HashMap<>();
    public static Map<String, Object> BUN_DANG_STATION_LINE = new HashMap<>();

    public static String SIN_BUN_DANG_NAME = "신분당선";
    public static String BUN_DANG_NAME = "분당선";
    public static String LINE_RED = "bg-red-600";
    public static String LINE_GREEN = "bg-green-600";


    static {
        SIN_BUN_DANG_STATION_LINE.put("name", SIN_BUN_DANG_NAME);
        SIN_BUN_DANG_STATION_LINE.put("color", LINE_RED);
        SIN_BUN_DANG_STATION_LINE.put("upStationId", 1);
        SIN_BUN_DANG_STATION_LINE.put("downStationId", 2);
        SIN_BUN_DANG_STATION_LINE.put("distance", 10);

        BUN_DANG_STATION_LINE.put("name", BUN_DANG_NAME);
        BUN_DANG_STATION_LINE.put("color", LINE_GREEN);
        BUN_DANG_STATION_LINE.put("upStationId", 1);
        BUN_DANG_STATION_LINE.put("downStationId", 3);
        BUN_DANG_STATION_LINE.put("distance", 20);
    }


    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static void createStation(String name) {
        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(getRequestSpecification())
                        .body(Map.entry("name", name))
                        .when().post("/stations")
                        .then()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> selectStations() {
        return RestAssured
                .given().spec(StationUtils.getRequestSpecification())
                .when().get("/stations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> createStationLine(Map<String, Object> body) {
        return RestAssured.given().spec(getRequestSpecification()).body(body)
                .when().post("/lines")
                .then().extract();
    }

}
