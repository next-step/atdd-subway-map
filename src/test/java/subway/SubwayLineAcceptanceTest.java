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

import java.util.HashMap;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.read;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubwayLineAcceptanceTest {
    private final static int PORT = 8080;
    private Long upStationId;
    private Long downStationId;
    private Long line5Id;

    @BeforeEach
    void setUp() {
        RestAssured.port = PORT;
        this.upStationId = beforeTestCreateStation("신사역");
        this.downStationId = beforeTestCreateStation("광교역");
        this.line5Id = beforeTestCreateLine5();
    }
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO 지하철 노선 생성
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //when
        String name = "신분당선";
        String color = "bg-red-600";
        int distance = 10;
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extract.body().jsonPath().getString("name")).isEqualTo(name);
        assertThat(extract.body().jsonPath().getString("color")).isEqualTo(color);
        assertThat(extract.body().jsonPath().getList("stationResponseList").size()).isEqualTo(2);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO 지하철 노선 목록 조회
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void findLines() {
        //given
        long newLineId = beforeTestCreateLine();

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
        //then
        String prettify = extract.body().jsonPath().prettify();
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat((String) read(prettify, "$.[0].name")).isEqualTo("5호선");
        assertThat((String) read(prettify, "$.[0].stationResponseList[0].name")).isEqualTo("신사역");
        assertThat((String) read(prettify, "$.[0].stationResponseList[1].name")).isEqualTo("광교역");
        assertThat((String) read(prettify, "$.[1].name")).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO 지하철 노선 조회
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void findLine() {
        //given
        //생성은 맨 처음
        //when
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}", this.line5Id)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extract.body().jsonPath().getString("name")).isEqualTo("5호선");
        assertThat(extract.body().jsonPath().getString("color")).isEqualTo("bg-purple-600");
        assertThat(extract.body().jsonPath().getList("stationResponseList").size()).isEqualTo(2);
    }
    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO 지하철 노선 수정
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "5호선");
        params.put("color", "bg-red-660");
        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", line5Id)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO 지하철 노선 삭제
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().delete("/lines/{id}", line5Id)
                .then().log().all()
                .extract();
        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선 구간을 만들어서 기존 노선에 추가한다.
     * Then 새롭게 구성된 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 구간 등록")
    @Test
    void addSection() {
        //given
        long suwonStationid = beforeTestCreateStation("수원역");
        Map<String, String> param = new HashMap<>();
        param.put("upStationId", String.valueOf(downStationId));
        param.put("downStationId", String.valueOf(suwonStationid));
        param.put("distance", String.valueOf(10));
        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections", line5Id)
                .then().log().all()
                .extract();
        //Then
        String prettify = extract.body().jsonPath().prettify();
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat((String) read(prettify, "$.stationResponseList[0].name")).isEqualTo("신사역");
        assertThat((String) read(prettify, "$.stationResponseList[1].name")).isEqualTo("수원역");
        assertThat(read(prettify, "$.stationResponseList[0].id").toString()).isEqualTo(String.valueOf(upStationId));
        assertThat(read(prettify, "$.stationResponseList[1].id").toString()).isEqualTo(String.valueOf(suwonStationid));
    }

    /**
     * Given 상행 종점역, 하생 종점역, 중간역이 있는 노선을 생성한다.
     * When 하행 종점역을 제거한다.
     * Then 제거한 결과를 본다.
     */
    @DisplayName("단일 누선 구간 제거")
    @Test
    void deleteSection() {
        // given
        long suwonStationId = beforeTestCreateStation("수원역");
        long lineId2 = beforeTestAddLine(line5Id, downStationId, suwonStationId);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("stationId", suwonStationId)
                .when().delete("/lines/{id}/sections", lineId2)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 상행 종점역, 하생 종점역 있는 노선을 생성한다.
     * When 하행 종점역을 제거한다.
     * Then 제거 시 오류 발생을 테스트한다.
     */
    @DisplayName("상행 종점역, 하행 종점역만 있는 노선 구간 제거 테스트")
    @Test
    void deleteSectionException() {
        // given

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("stationId", downStationId)
                .when().delete("/lines/{id}/sections", line5Id)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 상행 종점역, 하생 종점역, 중간역이 있는 노선을 생성한다.
     * When 상행 종점역을 제거한다.
     * Then 500 에러를 뱉는다.
     */
    @DisplayName("상행 종점역 제거시 에러를 뱉는다.")
    @Test
    void deleteUpStation() {
        // given
        long suwonStationId = beforeTestCreateStation("수원역");
        long lineId2 = beforeTestAddLine(line5Id, downStationId, suwonStationId);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("stationId", upStationId)
                .when().delete("/lines/{id}/sections", lineId2)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    // 지하철역 생성
    private long beforeTestCreateStation(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().body().jsonPath().getLong("id");
    }

    private long beforeTestCreateLine() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "red-600");
        params.put("upStationId", this.upStationId);
        params.put("downStationId", this.downStationId);
        params.put("distance", 10);
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        return extract.body().jsonPath().getLong("id");
    }

    private long beforeTestCreateLine5() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "5호선");
        params.put("color", "bg-purple-600");
        params.put("upStationId", this.upStationId);
        params.put("downStationId", this.downStationId);
        params.put("distance", 10);
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        return extract.body().jsonPath().getLong("id");
    }

    private long beforeTestAddLine(Long lineId, Long upStationId, Long downStationId) {
        Map<String, String> param = new HashMap<>();
        param.put("upStationId", String.valueOf(upStationId));
        param.put("downStationId", String.valueOf(downStationId));
        param.put("distance", String.valueOf(10));

        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines/{id}/sections", lineId)
                .then().log().all()
                .extract();
        return extract.body().jsonPath().getLong("id");
    }
}
