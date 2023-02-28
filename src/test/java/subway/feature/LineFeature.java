package subway.feature;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;


public class LineFeature extends RestAssuredFeature{

    private static final String BASE_URL = "/lines";
    private static final String NAME_KEY = "name";
    private static final String COLOR_KEY = "color";

    private static final String UP_ST_KEY = "upStationId";

    private static final String DOWN_ST_KEY = "downStationId";

    private static final String DISTANCE_KEY = "distance";

    public static ExtractableResponse<Response> callCreateLine(final String name,
                                                               final String color,
                                                               final Long upStationId,
                                                               final Long downStationId,
                                                               final Integer distance){
        return callCreate(BASE_URL, Map.of(NAME_KEY, name,
                COLOR_KEY, color,
                UP_ST_KEY, upStationId,
                DOWN_ST_KEY, downStationId,
                DISTANCE_KEY, distance));
    }

    public static ExtractableResponse<Response> callGetLines(){
        return callGet(BASE_URL);
    }

    public static ExtractableResponse<Response> callGetLine(final Integer id){
        return callGet(BASE_URL + "/" + id);
    }

    public static ExtractableResponse<Response> callModifyLine(final Integer id, final String name, final String color){
        return callModify(BASE_URL + "/" + id, Map.of(NAME_KEY, name, COLOR_KEY, color));
    }

    public static ExtractableResponse<Response> deleteStation(final Long id) {
        return callDelete(BASE_URL + "/" + id);
    }
}