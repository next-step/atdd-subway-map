package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineApiClient.*;
import static subway.station.StationApiClient.requestCreateStation;

@DisplayName("자하철 구간 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * Given 지하철 역 3개를 생성하고
     * Given 지하철 노선 1개를 생성하고
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 기존 구간과 등록한 구간이 합쳐진 노선을 확인할 수 있다.
     */
    @DisplayName("구간 등록 기능")
    @Test
    void createSection() {
        // given
        StationResponse stationA = requestCreateStation("A역").body().as(StationResponse.class);
        StationResponse stationB = requestCreateStation("B역").body().as(StationResponse.class);
        StationResponse stationC = requestCreateStation("C역").body().as(StationResponse.class);

        LineResponse lineOne = requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        // when
        requestAppendSection(lineOne.getId(), stationA.getId(), stationB.getId(), 10);

        // then
        ExtractableResponse<Response> showLineResponse = requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC, stationB);
    }
}
