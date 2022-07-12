package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    public static final String CLEAN_UP_TABLE = "station";

    private SubwayCallApi subwayCallApi;

    public StationAcceptanceTest() {
        this.subwayCallApi = new SubwayCallApi();
    }

    @Override
    protected void preprocessing() {
        cleanUpSchema.execute(CLEAN_UP_TABLE);
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = subwayCallApi.saveStation(Map.of("name", "강남역"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(ActualUtils.get(response, "name", String.class)).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        subwayCallApi.saveStation(Map.of("name", "낙성대역"));
        subwayCallApi.saveStation(Map.of("name", "구로디지털단지역"));

        // when
        ExtractableResponse<Response> response = subwayCallApi.findStations();

        // then
        assertThat(ActualUtils.getList(response, "name", String.class)).isEqualTo(List.of("낙성대역", "구로디지털단지역"));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> saveResponse = subwayCallApi.saveStation(Map.of("name", "서울대입구역"));

        // when
        Long id = ActualUtils.get(saveResponse, "id", Long.class);
        ExtractableResponse<Response> deleteResponse = subwayCallApi.deleteStationById(id);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> response = subwayCallApi.findStations();
        assertThat(ActualUtils.getList(response, "name", String.class).contains("서울대입구역")).isFalse();
    }

}