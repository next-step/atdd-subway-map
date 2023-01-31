package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineApiClient.*;
import static subway.station.StationApiClient.requestCreateStation;

@DisplayName("자하철 구간 관리 기능")
@Sql("classpath:sql/delete-records.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private StationResponse stationA;
    private StationResponse stationB;
    private StationResponse stationC;

    @BeforeEach
    void beforeEach() {
        stationA = requestCreateStation("A역").body().as(StationResponse.class);
        stationB = requestCreateStation("B역").body().as(StationResponse.class);
        stationC = requestCreateStation("C역").body().as(StationResponse.class);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 조회시 기존 구간과 등록한 구간이 합쳐진 노선을 확인할 수 있다.
     */
    @DisplayName("구간 등록 기능 - 성공 케이스")
    @Test
    void createSectionSuccess() {
        // given
        LineResponse lineOne = requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        // when
        requestAppendSection(lineOne.getId(), stationC.getId(), stationB.getId(), 10);

        // then
        ExtractableResponse<Response> showLineResponse = requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC, stationB);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 조회시 기존 노선과 동일하다.
     */
    @DisplayName("구간 등록 기능 - 실패 케이스")
    @Test
    void createSectionFail() {
        // given
        LineResponse lineOne = requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        // when
        ExtractableResponse<Response> appendSectionResponse = requestAppendSection(lineOne.getId(), stationA.getId(), stationA.getId(), 10);

        assertThat(appendSectionResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        ExtractableResponse<Response> showLineResponse = requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC);
    }
}
