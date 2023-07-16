package subway.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.AcceptanceTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ApiTester {
    /**
     * 생성 요청에 대한 응답을 확인한다.
     * @param response 생성 요청에 대한 응답
     */
    protected void 생성_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * query parameter 를 정의한다.
     */
    protected static class RequestBuilder {
        private final Map<String, String> params;

        public RequestBuilder() {
            this.params = new HashMap<>();
        }

        /**
         * query parameter 를 추가한다.
         * @param key query parameter 의 key
         * @param value query parameter 의 value
         * @return query parameter 가 추가된 결과를 뱉는다.
         */
        public ApiTester.RequestBuilder add(String key, String value) {
            params.put(key, value);
            return this;
        }

        public ApiTester.RequestBuilder add(String key, int value) {
            return add(key, String.valueOf(value));
        }

        public ApiTester.RequestBuilder add(String key, long value) {
            return add(key, String.valueOf(value));
        }

        public ApiTester.RequestBuilder add(String key, char value) {
            return add(key, String.valueOf(value));
        }

        public ApiTester.RequestBuilder add(String key, double value) {
            return add(key, String.valueOf(value));
        }

        public ApiTester.RequestBuilder add(String key, float value) {
            return add(key, String.valueOf(value));
        }

        public ApiTester.RequestBuilder add(String key, boolean value) {
            return add(key, String.valueOf(value));
        }


        public ApiTester.RequestBuilder add(String key, BigDecimal value) {
            return add(key, String.valueOf(value));
        }
        /**
         * query parameter 를 빌드한다.
         * @return query parameter
         */
        public Map<String, String> build() {
            return params;
        }

    }
}
