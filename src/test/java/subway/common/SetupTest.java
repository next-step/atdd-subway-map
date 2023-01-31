package subway.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static subway.common.DomainApiTest.지하철노선을_생성한다;

public class SetupTest {
    public static void 분당선_노선을_생성한다() {
        Map<String, Object> param = new HashMap<>();

        param.put("name", "분당선");
        param.put("color", "bg-green-600");
        param.put("upStationId", 2);
        param.put("downStationId", 3);
        param.put("distance", 5);

        지하철노선을_생성한다(param);
    }

    public static void 신분당선_노선을_생성한다() {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");
        param.put("upStationId", 1);
        param.put("downStationId", 2);
        param.put("distance", 10);

        지하철노선을_생성한다(param);
    }
}
