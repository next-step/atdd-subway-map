package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 검증 비교 대상 값 관련 클래스
 */
public class ActualUtils {

    private static final Map<Class<?>, BiFunction<ExtractableResponse<Response>, String, ?>> EXTRACT_INFO_AT_RESPONSE_FUNCTIONS = Map.of(
            Long.class, (response, path) -> response.body().jsonPath().getLong(path),
            String.class, (response, path) -> response.body().jsonPath().getString(path)
    );

    /**
     * type에 해당하는 값을 반환
     * @param response
     * @param path
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T get(ExtractableResponse<Response> response, String path, Class<T> type) {
        return (T) EXTRACT_INFO_AT_RESPONSE_FUNCTIONS.get(type).apply(response, path);
    }

    /**
     * type에 해당하는 리스트 반환
     * @param response
     * @param path
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(ExtractableResponse<Response> response, String path, Class<T> type) {
        return response.jsonPath().getList(path, type);
    }
}
