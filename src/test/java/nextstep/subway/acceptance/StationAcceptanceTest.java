package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.domain.Station;
import nextstep.subway.enums.SubwayRequestPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends SpringBootTestConfig {

    private final SubwayRestAssured<Station> stationRestAssured = new SubwayRestAssured<>();

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        String defaultPath = SubwayRequestPath.STATION.getValue();
        ValidatableResponse 강남역 = stationRestAssured.postRequest(defaultPath, new Station("강남역"));

        강남역.statusCode(HttpStatus.CREATED.value());

        stationRestAssured.getRequest(defaultPath).body("name", contains("강남역"));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test()
    void getStations() {
        String defaultPath = SubwayRequestPath.STATION.getValue();
        stationRestAssured.postRequest(defaultPath, new Station("선릉역"));
        stationRestAssured.postRequest(defaultPath, new Station("영통역"));

        stationRestAssured.getRequest(defaultPath).body("name", contains("선릉역", "영통역"));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test()
    void deleteStation() {
        String defaultPath = SubwayRequestPath.STATION.getValue();
        ValidatableResponse postResponse = stationRestAssured.postRequest(defaultPath, new Station("사당역"));

        Station 사당역 = postResponse.extract().as(Station.class);
        stationRestAssured.deleteRequest(SubwayRequestPath.STATION.addPathParam(), 사당역.getId());

        stationRestAssured.getRequest(defaultPath).body("id", hasSize(0));
    }

}