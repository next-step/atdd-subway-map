package subway;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.LineController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineTest {


    //        When 지하철 노선을 생성하면
//        Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @Test
    void createLineTest() {
        //given
        //when
        Map<String,String> param =new HashMap<>();
        param.put("name","강남역삼");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/line")
                .then().log().all()
                .extract();
        //then

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> lineNames = RestAssured.given().log().all()
                .when().get("lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        assertThat(lineNames).contains("강남역삼");

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     * */
    @Test
    void showLines() {

    }
}
