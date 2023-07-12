package subway.acceptancetest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptancetest.AcceptanceTestHelper.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        final String name = "강남역";

        // when
        final ExtractableResponse<Response> response = 지하철역_생성(name);

        // then
        상태_코드_확인(response, HttpStatus.CREATED.value());
        final List<String> stationNames = 지하철역_목록_조회().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStationList() {
        // given
        지하철역_생성("강남역");
        지하철역_생성("역삼역");
        final int expectedListSize = 2;

        // when
        final ExtractableResponse<Response> response = 지하철역_목록_조회();

        // then
        상태_코드_확인(response, HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(expectedListSize);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void removeStation() {
        // given
        final String name = "강남역";
        final long id = 지하철역_생성(name).jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = 지하철역_삭제(id);

        // then
        상태_코드_확인(response, HttpStatus.NO_CONTENT.value());
        final List<String> stationNames = 지하철역_목록_조회().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain(name);
    }
}