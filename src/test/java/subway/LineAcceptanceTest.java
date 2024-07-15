package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성된다.
     */
    // todo 관리자 인가 처리 필요
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        final String 수인분당선 = "수인분당선";
        //given
        LineRequest request = new LineRequest(수인분당선);

        //when
        ExtractableResponse<Response> response = createLine(request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given: 관리자가 노선을 생성하고
     * When: 노선 목록을 조회하면
     * Then: 해당 노선이 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성하고 목록에 포함되었는지 확인한다")
    @Test
    void createAndLoadLines() {
        final String 수인분당선 = "수인분당선";
        //given
        createLine(new LineRequest(수인분당선));

        //when
        ExtractableResponse<Response> response = loadLines();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        List<String> names = response.jsonPath().getList("name", String.class);
        assertThat(names).size().isEqualTo(1);
        assertThat(names).contains(수인분당선);
        assertThat(names).doesNotContain("존재하지않는노선");
    }

    private ExtractableResponse<Response> createLine(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> loadLines() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

}
