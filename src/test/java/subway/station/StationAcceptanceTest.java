package subway.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void reset() {
        stationRepository.deleteAll();
    }

    @Test
    void 지하철역을_생성한다() {
        // when 지하철 노선을 생성하면
        createStation("강남역");

        // then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        List<String> stationNames = getStationNames();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @Test
    void 지하철_목록_조회() {
        //given 2개의 지하철역을 생성하고
        createStation("강남역");
        createStation("역삼역");

        //when 지하철역 목록을 조회하면
        List<String> stationNames = getStationNames();

        //then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(stationNames).contains("강남역", "역삼역"); // contains 를 이용해 두 역이 모두 포함되어 있는지 검증
    }

    @Test
    void 지하철역_제거() {
        //Given 지하철역을 생성하고
        createStation("강남역");
        Long stationId = createStation("역삼역");

        //When 생성한 지하철 노선을 삭제하면
        given()
                .pathParam("id", stationId)
                .when()
                .delete("/stations/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Then 해당 지하철 노선 정보는 삭제된다
        List<String> stationNameList = getStationNames();

        assertThat(stationNameList).containsOnly("강남역"); // containsOnly 를 이용해 강남역만 포함되어 있는지 검증
    }

    public static Long createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath()
                .getLong("id"); // 응답 객체에서 id값을 추출해 리턴
    }

    private List<String> getStationNames() {
        return when().get("/stations")
                .then()
                .extract()
                .jsonPath()
                .getList("name", String.class);
    }
}