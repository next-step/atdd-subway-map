package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        List<String> stations = List.of("강남역");

        // when - 지하철역 생성
        getIdOfcreateAndValidate(stations);

        // then - 목록 조회 후, 역을 찾을 수 있다.
        stationNameExists(stations, getStationNameOfselectStationsAndValidate(stations));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // when - 지하철역 생성
        List<String> stations = List.of("선릉역", "역삼역");
        getIdOfcreateAndValidate(stations);

        // then - 2개의 지하철역 응답 받기
        List<String> stationNames = getStationNameOfselectStationsAndValidate(stations);
        assertThat(stationNames).hasSize(2);
        stationNameExists(stations, stationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        List<String> stations = List.of("강남역");

        // when - 지하철역 생성 및 삭제
        deleteStationAndValidate(getIdOfcreateAndValidate(stations));

        // then - 지하철 목록을 찾을 수 없는지 확인
        List<String> stationNames = getStationNameOfselectStationsAndValidate(stations);
        assertThat(stationNames).doesNotContain(stations.toArray(String[]::new));
    }

    private void deleteStationAndValidate(List<Long> ids) {
        ids.stream()
                .map(id -> RestAssured
                        .given().log().all()
                        .when().delete("/stations/{id}", id)
                        .then().log().all()
                        .extract())
                .forEach(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()));
    }

    private  List<Long> getIdOfcreateAndValidate(List<String> stations) {
        return createAndValidate(stations).stream()
                .map(response -> response.jsonPath().getLong("id"))
                .collect(Collectors.toList());
    }

    private List<ExtractableResponse<Response>> createAndValidate(List<String> stations) {
        return stations.stream()
                .map(name -> RestAssured
                        .given().log().all()
                        .body(Collections.singletonMap("name", name))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract()
                )
                .peek(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()))
                .collect(Collectors.toList());
    }

    private void stationNameExists(List<String> stations, List<String> stationNames) {
        assertThat(stations).containsExactly(stationNames.toArray(String[]::new));
    }

    private List<String> getStationNameOfselectStationsAndValidate(List<String> stations) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList("name", String.class);
    }


}