package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import subway.Mocks.MockStation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.StationTestUtils.지하철역_삭제;
import static subway.StationTestUtils.지하철역_생성;
import static subway.StationTestUtils.지하철역_조회;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", MockStation.강남역.getName());

        ExtractableResponse<Response> response = StationTestUtils.prepareRestAssuredGiven(params)
            .when().post("/stations")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_조회();
        assertThat(stationNames).containsAnyOf(MockStation.강남역.getName());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("등록된 지하철역 목록을 조회한다.")
    void showStations() {
        // given
        지하철역_생성(List.of(MockStation.서울대입구역.getName(), MockStation.봉천역.getName()));

        // when
        List<String> stationNames = 지하철역_조회();

        // then
        assertAll(
            () -> assertThat(stationNames.size()).isEqualTo(2),
            () -> assertThat(stationNames).containsAll(
                List.of(MockStation.서울대입구역.getName(), MockStation.봉천역.getName())
            )
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("생성해둔 지하철 역을 삭제하면, 목록 조회시 해당 역을 찾을 수 없다.")
    void deleteStation() {
        // given
        String stationName = "강남역";
        Long id = 지하철역_생성(stationName);

        // when
        지하철역_삭제(id);

        // then
        assertThat(지하철역_조회()).doesNotContain(stationName);
    }
}
