package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Color;
import subway.line.LineResponse;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void tearDown() {
        em.createNativeQuery("TRUNCATE TABLE Line").executeUpdate();
        em.flush();
    }

    /**
     * 기능 목록
     * 지하철 노선 생성
     * 지하철 노선 목록 조회
     * 지하철 노선 조회
     * 지하철 노선 수정
     * 지하철 노선 삭제
     *
     * 아래의 순서로 기능을 구현하세요.
     * 인수 조건을 검증하는 인수 테스트 작성
     * 인수 테스트를 충족하는 기능 구현
     */

    @DisplayName("지하철 노선을 생성한다")
    @Test
    public void create_line() {

        //given
        Map<String, String> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());

        //When 지하철 노선을 생성하면
        LineResponse lineResponse = createLine(param);

        //Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        List<String> names = RestAssured.given().log().all()
                .when()
                    .get("/lines")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .jsonPath().getList("name", String.class);

        assertThat(names).containsAnyOf(lineResponse.getName());
    }


    //Given 2개의 지하철 노선을 생성하고
    //When 지하철 노선 목록을 조회하면
    //Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    public void findLines() {

        //given
        Map<String, String> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());

        createLine(param);

        param.put("name", "7호선");
        param.put("color", Color.GREEN.name());

        createLine(param);


        // when
        List<String> names = RestAssured.given().log().all()
                .when()
                    .get("/lines")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .jsonPath().getList("name", String.class);

        // then
        assertThat(names).containsExactly("2호선", "7호선");
    }


    //Given 지하철 노선을 생성하고
    //When 생성한 지하철 노선을 조회하면
    //Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    @DisplayName("지하철 노선을 조회한다")
    @Test
    public void findLine() {

        // given
        Map<String, String> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());

        LineResponse expected = createLine(param);

        // when
        LineResponse actual = RestAssured.given().log().all()
                .when()
                    .get("/lines/" + expected.getId())
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(LineResponse.class);

        // then
        assertThat(actual.equals(expected)).isTrue();
    }



    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 수정하면
    // Then 해당 지하철 노선 정보는 수정된다
    @DisplayName("지하철 노선 수정한다")
    @Test
    public void updateLine() {

        // given
        Map<String, String> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());

        LineResponse expected = createLine(param);

        // when
        param.put("color", Color.RED.name());
        RestAssured.given().log().all()
                .when()
                    .body(param)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .put("/lines/" + expected.getId())
                .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        LineResponse actual = RestAssured.given().log().all()
                .when()
                    .get("/lines/" + expected.getId())
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(LineResponse.class);

        assertThat(actual.getColor()).isEqualTo(Color.RED);
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 삭제하면
    // Then 해당 지하철 노선 정보는 삭제된다
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    public void deleteLine() {

        // given
        Map<String, String> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());

        LineResponse expected = createLine(param);

        // when
        RestAssured.given().log().all()
                .when()
                    .delete("/lines/" + expected.getId())
                .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given().log().all()
                .when()
                    .get("/lines/" + expected.getId())
                .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract();

    }



    private static LineResponse createLine(Map<String, String> param) {
        return RestAssured.given().log().all()
                .when()
                    .body(param)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .post("/lines")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(LineResponse.class);
    }

}
