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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import subway.fixture.AcceptanceTest;
import subway.fixture.LineSteps;
import subway.fixture.StationSteps;
import subway.line.Color;
import subway.line.LineResponse;
import subway.station.StationResponse;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@AcceptanceTest
@Transactional
class LineAcceptanceTest {

    private static StationResponse 선릉역;
    private static StationResponse 삼성역;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        선릉역 = StationSteps.createStation("선릉역");
        삼성역 = StationSteps.createStation("삼성역");
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
    void create_line() {

        //given
        Map<String, Object> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());
        param.put("upStationId", 선릉역.getId());
        param.put("downStationId", 삼성역.getId());

        //When 지하철 노선을 생성하면
        LineResponse lineResponse = LineSteps.createLine(param);

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
    void findLines() {



        //given
        Map<String, Object> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());
        param.put("upStationId", 선릉역.getId());
        param.put("downStationId", 삼성역.getId());

        //When 지하철 노선을 생성하면
        LineSteps.createLine(param);


        // when
        List<String> names = RestAssured.given().log().all()
                .when()
                    .get("/lines")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .jsonPath().getList("name", String.class);

        // then
        assertThat(names).containsExactly("2호선");
    }


    //Given 지하철 노선을 생성하고
    //When 생성한 지하철 노선을 조회하면
    //Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void findLine() {


        //given
        Map<String, Object> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());
        param.put("upStationId", 선릉역.getId());
        param.put("downStationId", 삼성역.getId());

        //When 지하철 노선을 생성하면
        LineResponse lineResponse = LineSteps.createLine(param);


        // when
        LineResponse actual = RestAssured.given().log().all()
                .when()
                    .get("/lines/" + lineResponse.getId())
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(LineResponse.class);

        // then
        assertThat(actual).isEqualTo(lineResponse);
    }



    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 수정하면
    // Then 해당 지하철 노선 정보는 수정된다
    @DisplayName("지하철 노선 수정한다")
    @Test
    void updateLine() {


        //given
        Map<String, Object> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());
        param.put("upStationId", 선릉역.getId());
        param.put("downStationId", 삼성역.getId());

        //When 지하철 노선을 생성하면
        LineResponse lineResponse = LineSteps.createLine(param);

        // when
        param.put("color", Color.RED.name());
        RestAssured.given().log().all()
                .when()
                    .body(param)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .put("/lines/" + lineResponse.getId())
                .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        LineResponse actual = RestAssured.given().log().all()
                .when()
                    .get("/lines/" + lineResponse.getId())
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
    void deleteLine() {


        //given
        Map<String, Object> param = new HashMap<>();
        param.put("name", "2호선");
        param.put("color", Color.GREEN.name());
        param.put("upStationId", 선릉역.getId());
        param.put("downStationId", 삼성역.getId());

        LineResponse lineResponse = LineSteps.createLine(param);

        // when
        RestAssured.given().log().all()
                .when()
                    .delete("/lines/" + lineResponse.getId())
                .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given().log().all()
                .when()
                    .get("/lines/" + lineResponse.getId())
                .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }


}
