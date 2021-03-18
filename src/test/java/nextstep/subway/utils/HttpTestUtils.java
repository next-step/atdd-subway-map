package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.StationSteps;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class HttpTestUtils {

    public static Long 리소스_ID(ExtractableResponse<Response> response) {
        return Optional.ofNullable(response)
                .map(HttpTestUtils::리소스_URI)
                .map(it -> StringUtils.substringAfterLast(it, "/"))
                .map(Long::parseLong)
                .orElse(null);
    }

    public static String 리소스_URI(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

}
