package subway.acceptance.common.handler;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;


/**
 * Json 데이터 경로 관련
 */
public class BodyJsonPathHandler {

    /**
     * Json 데이터 경로셋
     */
    enum Path {
        이름("name"),
        아이디("id"),
        색상("color"),
        지하철역_목록("stations"),
        ;

        private String path;

        Path(String path) {
            this.path = path;
        }

        private String path() {
            return this.path;
        }
    }

    public static String 이름_추출(ExtractableResponse<Response> 응답) {
        return 응답.jsonPath().get(Path.이름.path());
    }

    public static List<String> 이름_목록_추출(ExtractableResponse<Response> 응답) {
        return 응답.jsonPath().getList(Path.이름.path(), String.class);
    }

    public static Long 아이디_추출(ExtractableResponse<Response> 응답) {
        return 응답.jsonPath().getLong(Path.아이디.path());
    }

    public static List<Long> 아이디_목록_추출(ExtractableResponse<Response> 응답) {
        return 응답.jsonPath().getList(Path.아이디.path(), Long.class);
    }

    public static String 색상_추출(ExtractableResponse<Response> 응답) {
        return 응답.jsonPath().get(Path.색상.path());
    }

    public static List<String> 지하철역_이름_목록_추출(ExtractableResponse<Response> 응답) {
        return 응답.jsonPath().getList("stations.name", String.class);
    }

}