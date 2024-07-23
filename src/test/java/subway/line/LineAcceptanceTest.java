package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.util.TestUtil;
import subway.dto.line.LineRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.TestUtil.createStation;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @BeforeEach
    void initStation(){
        createStation("지하철역");
        createStation("새로운지하철역");
        createStation("또다른지하철역");
    }
    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
    */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        // given
        // when
        // then
        assertThat(TestUtil.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L))
                .statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        assertThat(TestUtil.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L))
                .statusCode()).isEqualTo(HttpStatus.CREATED.value());

        assertThat(TestUtil.createLine(new LineRequest("분당선", "bg-green-600", 1L, 3L, 20L))
                .statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        List<String> lineNames =
                given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().jsonPath().getList("name", String.class);
        //then
        assertThat(lineNames).containsExactlyInAnyOrder("신분당선", "분당선");
    }


    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {

        // given
        String showLineUrl = TestUtil.createLine(new LineRequest("신분당선", "bg-red-400", 1L, 2L, 10L))
                .getHeader("Location");

        // when
        String lineName =
                given().log().all()
                        .when().get(showLineUrl)
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().jsonPath().get("name");
        //then
        assertThat(lineName).contains("신분당선");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void editLine() {
        // given
        String editLineUrl = TestUtil.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L))
                .getHeader("Location");


        //when
        Map<String, String> editParam = new HashMap<>();
        editParam.put("name", "다른분당선");
        editParam.put("color", "bg-red-600");

        ExtractableResponse<Response> response =
                given().log().all()
                        .body(editParam)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().put(editLineUrl)
                        .then().log().all()
                        .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        String deleteLineUrl = TestUtil.createLine(new LineRequest("신분당선", "bg-red-400", 1L, 2L, 10L))
                .getHeader("Location");

        //when
        ExtractableResponse<Response> response =
                given().log().all()
                        .when().delete(deleteLineUrl)
                        .then().log().all()
                        .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }


}
