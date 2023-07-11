package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.util.Extractor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationStep.강남역;
import static subway.station.StationStep.역삼역;
import static subway.util.Extractor.getId;

@Sql("/sql/all-table-truncate.sql")
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = StationStep.지하철역을_저장(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(StationStep.전체_지하철역의_이름을_조회()).containsAnyOf(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void searchStation() {
        List<String> list = Arrays.asList(강남역, 역삼역);
        //given
        list.forEach(StationStep::지하철역을_저장);

        //when
        List<String> stations = StationStep.전체_지하철역의_이름을_조회();

        //then
        assertThat(stations).hasSize(list.size());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void removeStation() {
        //given
        Long id = getId(StationStep.지하철역을_저장(강남역));

        //when
        StationStep.지하철역을_삭제(id);

        //then
        assertThat(StationStep.전체_지하철역의_이름을_조회()).doesNotContain(강남역);
    }
}