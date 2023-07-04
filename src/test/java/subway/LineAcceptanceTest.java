package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/station-setup.sql")
public class LineAcceptanceTest {

    //    When 지하철 노선을 생성하면
    //    Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        지하철_노선을_생성한다("신분당선", "bg-red-600", 1L, 2L, 10L);

        // then
        List<String> lineNames = 지하철_노선_목록을_조회한다();
        생성된_노선이_노선_목록에_포함된다(lineNames, "신분당선");
    }

    //    Given 2개의 지하철 노선을 생성하고
    //    When 지하철 노선 목록을 조회하면
    //    Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        LineRequest request1 = new LineRequest(
                "신분당선",
                "bg-red-600",
                1L,
                2L,
                10L
        );
        RestAssured.given().log().all()
                .body(request1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();

        LineRequest request2 = new LineRequest(
                "분당선",
                "bg-green-600",
                1L,
                3L,
                15L
        );
        RestAssured.given().log().all()
                .body(request2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();

        // when
        List<String> lineNames = 지하철_노선_목록을_조회한다();

        // then
        assertThat(lineNames.size()).isEqualTo(2);
    }

    private static void 생성된_노선이_노선_목록에_포함된다(List<String> lineNames, String createdLineName) {
        assertThat(lineNames).containsAnyOf(createdLineName);
    }

    private static List<String> 지하철_노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private static void 지하철_노선을_생성한다(String name, String color, Long upStationId, Long downStationId, Long distance) {
        LineRequest request = new LineRequest(
                name,
                color,
                upStationId,
                downStationId,
                distance
        );

        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    //    Given 지하철 노선을 생성하고
    //    When 생성한 지하철 노선을 조회하면
    //    Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    //    TODO: 지하철노선 조회 인수 테스트 작성

    //    Given 지하철 노선을 생성하고
    //    When 생성한 지하철 노선을 수정하면
    //    Then 해당 지하철 노선 정보는 수정된다
    //    TODO: 지하철노선 수정 인수 테스트 작성

    //    Given 지하철 노선을 생성하고
    //    When 생성한 지하철 노선을 삭제하면
    //    Then 해당 지하철 노선 정보는 삭제된다
    //    TODO: 지하철노선 삭제 인수 테스트 작성
}
