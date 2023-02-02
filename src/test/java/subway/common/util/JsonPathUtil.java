package subway.common.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.fixture.FieldFixture;

import java.util.List;

public class JsonPathUtil {

    public static String 문자열로_데이터_추출(ExtractableResponse<Response> API_응답_결과, FieldFixture 추출할_데이터_필드) {
        return API_응답_결과.jsonPath().getString(추출할_데이터_필드.필드명());
    }

    public static List<String> 리스트로_데이터_추출(ExtractableResponse<Response> API_응답_결과, FieldFixture 추출할_데이터_필드) {
        return API_응답_결과.jsonPath().getList(추출할_데이터_필드.필드명());
    }
}
