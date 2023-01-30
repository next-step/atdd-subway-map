package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.BaseAcceptanceTest;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.executor.AcceptanceExecutor;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest extends BaseAcceptanceTest {

    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when 지하철역을 생성하면
        ExtractableResponse<Response> response = AcceptanceExecutor.post("/stations", new StationRequest("강남역"));

        // then 지하철역이 생성된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        List<StationResponse> stations = List.of(AcceptanceExecutor.get("/stations", StationResponse[].class));
        assertThat(stations.stream().map(StationResponse::getName)
                .collect(Collectors.toUnmodifiableList())).contains("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록 조회")
    void findStations() {
        //given 2개의 지하철역을 생성하고
        AcceptanceExecutor.post("/stations", new StationRequest("강남역"));
        AcceptanceExecutor.post("/stations", new StationRequest("언주역"));
        //when 지하철역 목록을 조회하면
        List<StationResponse> stations = List.of(AcceptanceExecutor.get("/stations", StationResponse[].class));
        //then
        assertAll(
                () -> assertThat(stations).hasSize(2),
                () -> assertThat(stations.stream().map(StationResponse::getName)
                        .collect(Collectors.toUnmodifiableList())).containsAnyOf("강남역", "언주역")
        );

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @Test
    @DisplayName("지하철역 삭제")
    void removeStation() {
        //given
        AcceptanceExecutor.post("/stations", new StationRequest("언주역"));
        AcceptanceExecutor.post("/stations", new StationRequest("개봉역"));
        Long 강남역Id = AcceptanceExecutor.post("/stations", new StationRequest("강남역"))
                .jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = AcceptanceExecutor.delete("/stations/" + 강남역Id);
        //then
        List<StationResponse> stations = List.of(AcceptanceExecutor.get("/stations", StationResponse[].class));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
                , () -> assertThat(stations).hasSize(2)
                , () -> assertThat(stations.stream().map(StationResponse::getName)
                        .collect(Collectors.toUnmodifiableList())).containsAnyOf("언주역", "개봉역")
        );

    }

}
