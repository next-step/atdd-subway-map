package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

//TODO 0307_실습1 - random_port는 8080포트만 열리는 거 방지 & 충돌방지
//dto, controller도 다 구성돼있는 경우의 인수테스트를 작성하는 실습예제!
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest extends AcceptanceTest {

    //Test가 동작할때, 정상적인 서버 포트를 바라보도록 모든 RestAssured에 동작중인 로컬서버 port를 설정
    //이런 부분을 따로 떼어내서 DatabaseCleanUp & AcceptanceTest 클래스를 만들어놓고 활용중! (extends AcceptanceTest)
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.port = port;
//    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given --상황을 제공
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        //RestAssured.given().when().then() 쓰고 사이사이에 원하는 요구사항을 넣어주는 것
        ExtractableResponse<Response> response = RestAssured.given().log().all() //시작점 로그를 받을래
                .body(params) //강남역이 들어있는 map형태의 파라미터를 넘길거다
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations") //post요청을 이 경로로 보낼거다
                .then().log().all() //로그를 받을래 (아래 then절 있으므로 꼭 필요한 건 아님)
                .extract(); //수행결과에 대한 값을 가진 객체를 반환

        // then - when을 수행했을 때의 응답값에 대한 체크
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()); //201 enum에 대한 int값
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        ExtractableResponse<Response> createResponse1 = RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        ExtractableResponse<Response> createResponse2 = RestAssured.given().log().all()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        List<Long> expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
//                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
//                .collect(Collectors.toList());
//        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
//                .map(it -> it.getId())
//                .collect(Collectors.toList());
//        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
