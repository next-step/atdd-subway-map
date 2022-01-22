package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.step.StationStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {

        String 강남역 = "강남역";
        ExtractableResponse<Response> response = StationStep.saveStation(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {

        String 강남역 = "강남역";
        StationStep.saveStation(강남역);

        String 사당역 = "사당역";
        StationStep.saveStation(사당역);

        // when
        ExtractableResponse<Response> response = StationStep.showStation();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 사당역);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {

        String 강남역 = "강남역";
        ExtractableResponse<Response> createResponse = StationStep.saveStation(강남역);

        // when
        ExtractableResponse<Response> response =
                StationStep.deleteStation(createResponse.header("Location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철을 생성 요청 한다.
     * When 같은 이름으로 지하철 역을 생성 요청한다.
     * Then 지하철 생성이 실패한다.
     */
    @DisplayName("중복 지하철역 생성 실패")
    @Test
    void createStation_duplication() {

        // 노선을 생성한다.
        StationStep.saveStation("name_1");

        // 중복으로 생성할 때
        ExtractableResponse<Response> response = StationStep.saveStation("name_1");

        // 실패를 한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
