package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;
import subway.line.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/insert_test_station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineAcceptanceTest {

    private static String GANGNAM = "강남역";
    private static String YEOKSAM = "역삼역";

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        LineRequest request = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        //when 지하철 노선을 생성하면.
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();


        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat((LineResponse) response.jsonPath().get()).isInstanceOf(LineResponse.class);

        //지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있음.
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .body(request)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().post("/lines")
//                .then().log().all()
//                .extract();

    }

    @DisplayName("지하철 노선 생성2")
    @Test
    void createLine2() {


    }
}
