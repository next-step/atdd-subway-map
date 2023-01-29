package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    public void setup() {
        stationRepository.deleteAll();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("강남역 지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        createStation("강남역");

        // then
        List<String> stationNameList = getStationNameList();
        assertThat(stationNameList).containsAnyOf("강남역");
    }



    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStationList() {

        //Given
        createStation("강남역");
        createStation("역삼역");

        //Then
        List<String> stationNameList = getStationNameList();

        assertThat(stationNameList).containsAnyOf("강남역", "역삼역");
        assertThat(stationNameList).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제합니다.")
    @Test
    public void deleteStation() {
        // Given
        Long id = createStation("강남역");
        createStation("역삼역");

        // When
        var deleteResponse = RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Then

        var stationNameList = getStationNameList();

        assertThat(stationNameList).hasSize(1);
        assertThat(stationNameList).containsExactly("역삼역");
        assertThat(stationNameList).doesNotContain("강남역");
    }


    @DisplayName("주어진 이름의 지하철역을 생성한다.")
    private Long createStation(String station) {
        Map<String, String> params = new HashMap<>();
        params.put("name", station);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().jsonPath().getLong("id");
    }

    @DisplayName("지하철역 목록을 조회한다.")
    private List<String> getStationNameList() {
        var response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.jsonPath().getList("name", String.class);

    }
}