package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.domain.Station;
import nextstep.subway.acceptance.enums.SubwayRequestPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.*;

@DisplayName("지하철역 관련 기능")
@ActiveProfiles("test")
class StationAcceptanceTest extends SpringBootTestConfig {
    protected final SubwayRestAssured<Station> stationRestAssured = new SubwayRestAssured<>();

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        String stationRootPath = SubwayRequestPath.STATION.getValue();
        ValidatableResponse 강남역 = stationRestAssured.postRequest(stationRootPath, new Station("강남역"));

        강남역.statusCode(HttpStatus.CREATED.value());

        stationRestAssured.getRequest(stationRootPath).body("name", contains("강남역"));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test()
    void getStations() {
        String stationRootPath = SubwayRequestPath.STATION.getValue();
        stationRestAssured.postRequest(stationRootPath, new Station("선릉역"));
        stationRestAssured.postRequest(stationRootPath, new Station("영통역"));

        stationRestAssured.getRequest(stationRootPath).body("name", contains("선릉역", "영통역"));
    }

    /**
     * Given 지하철역을 생성하고
     * When, Then  지하철역을 조회하면 생성한 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test()
    void getStation() {
        String stationRootPath = SubwayRequestPath.STATION.getValue();
        ValidatableResponse 선릉역 = stationRestAssured.postRequest(stationRootPath, new Station("선릉역"));

        String 생성된_지하철역_조회_경로 = 선릉역.extract().header("Location");
        stationRestAssured.getRequest(생성된_지하철역_조회_경로).body("name", equalTo("선릉역"));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test()
    void deleteStation() {
        String stationRootPath = SubwayRequestPath.STATION.getValue();
        ValidatableResponse 지하철역_등록결과 = stationRestAssured.postRequest(stationRootPath, new Station("사당역"));

        String nextLocation = 지하철역_등록결과.extract().header("Location");
        stationRestAssured.deleteRequest(nextLocation);

        stationRestAssured.getRequest(stationRootPath).body("id", hasSize(0));
    }

}