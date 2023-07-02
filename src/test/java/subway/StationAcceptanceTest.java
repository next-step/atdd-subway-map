package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성하면 생성된 지하철역이 조회된다.")
    @ParameterizedTest
    @ValueSource(strings = {"강남역"})
    void createStation(String name) {
        // when
        TestHelper.createStation(name);

        // then
        assertThat(TestHelper.selectAllStations())
                .extracting(StationResponse::getName)
                .contains(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역을 생성한 후 목록을 조회하면 생성한 지하철 역이 조회된다.")
    void selectStations() {
        // given
        TestHelper.createStation("강남역");
        TestHelper.createStation("역삼역");

        // when
        List<StationResponse> stationResponses = TestHelper.selectAllStations();

        // then
        assertThat(stationResponses).hasSize(2)
                .map(StationResponse::getName)
                .containsExactly("강남역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @ParameterizedTest
    @ValueSource(strings = {"강남역"})
    @DisplayName("지하철역을 제거하면 목록 조회시 조회되지 않는다")
    void deleteStation(String name) {
        // given
        final StationResponse station = TestHelper.createStation(name);

        //when
        RestAssured.given().log().all()
                .when().log().all()
                    .delete("/stations/{id}", station.getId())
                .then()
                    .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        //then
        assertThat(TestHelper.selectAllStations())
                .extracting(StationResponse::getName)
                .allMatch(it -> !it.equals(name));
    }

}