package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 역 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationPracticeTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("지하철 역을 생성한다.")
    void createStation() {
        // given
        Map<String, String> params = 지하철_역_생성();

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(params);

        // then - 지하철 역 생성 응답 확인
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("지하철 역을 삭제한다.")
    void deleteStation() {
        // given
        Map<String, String> params = 지하철_역_생성();
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청(params);

        // 지하철 역 생성 응답 확인
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();

        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_역_삭제_요청(uri);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철_역_생성_요청(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private Map<String, String> 지하철_역_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        return params;
    }

    private ExtractableResponse<Response> 지하철_역_삭제_요청(String uri) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
