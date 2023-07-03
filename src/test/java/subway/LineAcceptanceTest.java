package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    //    When 지하철 노선을 생성하면
    //    Then 지하철 노선이 생성된다
    //    Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @DisplayName("지하철 노선을 생성한다.")
    @Sql("/station-setup.sql")
    @Test
    void createLine() {
        // when
        LineRequest request = new LineRequest(
                "신분당선",
                "bg-red-600",
                1L,
                2L,
                10L
        );
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
//        List<String> lineNames =
//                RestAssured.given().log().all()
//                        .when().get("/lines")
//                        .then().log().all()
//                        .extract().jsonPath().getList("name", String.class);
//        assertThat(lineNames).containsAnyOf("신분당선");
    }


    //    Given 2개의 지하철 노선을 생성하고
    //    When 지하철 노선 목록을 조회하면
    //    Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    //    TODO: 지하철노선 목록 조회 인수 테스트 작

    //    Given 지하철 노선을 생성하고
    //    When 생성한 지하철 노선을 조회하면
    //    Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    //    TODO: 지하철노선 조회 인수 테스트 작

    //    Given 지하철 노선을 생성하고
    //    When 생성한 지하철 노선을 수정하면
    //    Then 해당 지하철 노선 정보는 수정된다
    //    TODO: 지하철노선 수정 인수 테스트 작

    //    Given 지하철 노선을 생성하고
    //    When 생성한 지하철 노선을 삭제하면
    //    Then 해당 지하철 노선 정보는 삭제된다
    //    TODO: 지하철노선 삭제 인수 테스트 작성
}
