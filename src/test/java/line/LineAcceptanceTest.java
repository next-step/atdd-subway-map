package line;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import net.bytebuddy.utility.dispatcher.JavaDispatcher.Defaults;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.SubwayApplication;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static final String API_CREATE_LINE = "/lines";

    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        // given
        Map<String, Object> params = new HashMap<>();

        // when
        ExtractableResponse<Response> createdResponse = RestAssured.given().log().all()
                                                                   .body(params)
                                                                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                   .when().post(API_CREATE_LINE)
                                                                   .then().log().all()
                                                                   .extract();

        // then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


}
