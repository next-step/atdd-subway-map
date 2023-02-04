package subway;


import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.SubwayFixture.LINE_SHIN_BUN_DANG_REQUEST;
import static subway.SubwayFixture.LINE_TWO_REQUEST;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;


@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/stations.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void 지하철_노선_생성() {

        //when
        ExtractableResponse<Response> response = createSubwayLine(
            new SubwayLineRequest("신분당선", "bg-red-600", 1L, 2L, 10));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).contains("신분당선");
    }


    private ExtractableResponse<Response> createSubwayLine(SubwayLineRequest subwayLineRequest) {
        return given().log().all()
            .body(subwayLineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then()
            .log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    private ExtractableResponse<Response> getSubwayLines() {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract();
//            .jsonPath().getList(".", SubwayLineResponse.class);
    }

    /**
     * 		given 2개의 지하철노선을 생성하고
     * 	  when 지하철노선 목록을 조회하면
     * 	  then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */

    @DisplayName("지하철 노선 목록을 조회")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        createSubwayLine(LINE_SHIN_BUN_DANG_REQUEST);
        createSubwayLine(LINE_TWO_REQUEST);

        // when
        ExtractableResponse<Response> response = getSubwayLines();

        // then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).containsAnyOf(LINE_SHIN_BUN_DANG_REQUEST.getName(),
            LINE_TWO_REQUEST.getName());
    }


}