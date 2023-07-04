package subway.helper;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class LineTestHelper {

    public static Long getId(ExtractableResponse<Response> response){
        return response.jsonPath().getLong("id");
    }

    public static String getName(ExtractableResponse<Response> response){
        return response.jsonPath().getString("name");
    }

    public static String getColor(ExtractableResponse<Response> response){
        return response.jsonPath().getString("color");
    }

    public static List<Integer> getStationIds(ExtractableResponse<Response> response){
        return response.jsonPath().getList("id", Integer.class);
    }
}
