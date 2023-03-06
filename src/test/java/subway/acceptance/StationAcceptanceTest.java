package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static subway.steps.StationSteps.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp(){
        RestAssured.port = port;
    }



    /** 주어진 요구사항
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @Test
    void 지하철_역_생성_인수_테스트() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철_역_목록_조회_요청();
        assertThat(stationNames).containsAnyOf("강남역");
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성

    @Test
    void 지하철_역_목록_조회_인수_테스트() {

        // given
        지하철_역_생성_요청("강남역");
        지하철_역_생성_요청("양재역");

        //when

        List<String> stationList = 지하철_역_목록_조회_요청();

        //then
        assertThat(stationList).containsExactly("강남역", "양재역");
    }





    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성

    @Test
    void 지하철_역_제거_인수_테스트() {

        // given
        Long id = 지하철_역_생성_요청("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_삭제_요청(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> stationNames = 지하철_역_목록_조회_요청();
        assertThat(stationNames).isNotIn("강남역");
    }

}