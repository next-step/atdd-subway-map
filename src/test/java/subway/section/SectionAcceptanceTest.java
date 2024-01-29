package subway.section;

import annotation.TestIsolation;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineApiRequester;
import subway.line.dto.LineCreateRequest;
import subway.station.StationApiRequester;
import subway.station.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@TestIsolation
@DisplayName("지하철노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private final StationApiRequester stationApiRequester = new StationApiRequester();

    private final LineApiRequester lineApiRequester = new LineApiRequester();

    @BeforeEach
    void setUp() {
        stationApiRequester.createStationApiCall("잠실역");
        stationApiRequester.createStationApiCall("용산역");
        stationApiRequester.createStationApiCall("건대입구역");
        stationApiRequester.createStationApiCall("성수역");
    }

    /**
     * When 노선에 구간을 등록하면
     * Then 노선을 조회 했을때 등록한 구간이 조회된다
     */
    @DisplayName("지하철 노선 구간 등록")
    @Test
    void createSection() {
        //given
        lineApiRequester.createLineApiCall(new LineCreateRequest("2호선", "green", 1L, 2L, 10));

        //when
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "5");

        ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> findLine = lineApiRequester.findLineApiCall(1L);
        List<Long> stations = findLine.jsonPath().getList("stations", StationResponse.class)
                .stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(stations).containsExactly(1L, 2L, 3L);
    }

    /**
     * When 등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 구간을 등록하면
     * Then 예외가 발생한다
     */
    @DisplayName("등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 경우 구간을 등록할 수 없다")
    @Test
    void createSectionException() {
        //given
        lineApiRequester.createLineApiCall(new LineCreateRequest("2호선", "green", 1L, 2L, 10));

        //when
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "3");
        params.put("downStationId", "4");
        params.put("distance", "5");

        ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString())
                .isEqualTo("등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 경우 구간 등록이 불가능합니다.");
    }

    /**
     * When 등록할 구간의 하행역이 이미 해당 노선에 등록되어있으면
     * Then 예외가 발생한다
     */
    @DisplayName("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.")
    @Test
    void createAlreadySection() {
        //given
        lineApiRequester.createLineApiCall(new LineCreateRequest("2호선", "green", 1L, 2L, 10));

        //when
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "1");
        params.put("distance", "5");

        ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString())
                .isEqualTo("이미 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
    }


    /**
     * Given 2개의 지하철 구간을 등록하고
     * When 구간이 2개인 노선의 구간중 1개를 삭제하면
     * Then 삭제한 1개의 구간이 삭제된다
     */
    @DisplayName("지하철 노선 구간 삭제")
    @Test
    void deleteSection() {
        //given
        lineApiRequester.createLineApiCall(new LineCreateRequest("2호선", "green", 1L, 2L, 10));

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "5");

        given().log().all()
                .body(params)
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //when
        ExtractableResponse<Response> response = given().log().all()
                .pathParam("id", 1L)
                .queryParam("stationId", 3L)
                .when().delete("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> findLine = lineApiRequester.findLineApiCall(1L);
        List<Long> stations = findLine.jsonPath().getList("stations", StationResponse.class)
                .stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(stations).containsExactly(1L, 2L);
    }

    /**
     * Given 2개의 지하철 구간을 등록하고
     * When 노선의 하행종점역이 아닌 구간을 삭제하면
     * Then 예외가 발생한다
     */
    @DisplayName("노선의 하행종점역이 아닌 구간은 삭제할 수 없다")
    @Test
    void deletNotDownSection() {
        //given
        lineApiRequester.createLineApiCall(new LineCreateRequest("2호선", "green", 1L, 2L, 10));

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "5");

        given().log().all()
                .body(params)
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //when
        ExtractableResponse<Response> response = given().log().all()
                .pathParam("id", 1L)
                .queryParam("stationId", 2L)
                .when().delete("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString())
                .isEqualTo("노선의 하행종점역만 제거할 수 있습니다.");
    }

    /**
     * Given 1개의 지하철 구간을 등록하고
     * When 구간이 1개인 노선의 구간을 삭제하면
     * Then 예외가 발생한다
     */
    @DisplayName("구간이 1개인 노선의 구간은 삭제할 수 없다")
    @Test
    void deleteSectionException() {
        //given
        lineApiRequester.createLineApiCall(new LineCreateRequest("2호선", "green", 1L, 2L, 10));

        //when
        ExtractableResponse<Response> response = given().log().all()
                .pathParam("id", 1L)
                .queryParam("stationId", 1L)
                .when().delete("/lines/{id}/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString())
                .isEqualTo("구간이 1개인 노선의 구간은 삭제할 수 없습니다.");
    }
}
