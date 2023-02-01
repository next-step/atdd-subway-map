package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineApiClient;
import subway.line.LineResponse;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineApiClient.requestAppendSection;
import static subway.line.LineApiClient.requestDeleteSection;
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
     * Then 지하철 노선 조회시 기존 구간과 등록한 구간이 합쳐진 노선을 확인할 수 있다
     */
    @DisplayName("구간 등록 기능 - 성공 케이스")
    @Test
    void createSectionSuccess() {
        // given
        LineResponse lineOne = LineApiClient.requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        // when
        requestAppendSection(lineOne.getId(), stationC.getId(), stationB.getId(), 3);

        // then
        ExtractableResponse<Response> showLineResponse = LineApiClient.requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC, stationB);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 등록 불가능한 지하철 구간을 등록하면
     * Then 지하철 노선 조회시 기존 노선과 동일하다
     */
    @DisplayName("구간 등록 기능 - 실패 케이스")
    @Test
    void createSectionFail() {
        // given
        LineResponse lineOne = LineApiClient.requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        // when
        ExtractableResponse<Response> appendSectionResponse = requestAppendSection(lineOne.getId(), stationA.getId(), stationA.getId(), 3);

        assertThat(appendSectionResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        ExtractableResponse<Response> showLineResponse = LineApiClient.requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC);
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 지하철 구간 하나를 추가하고
     * When 추가한 구간을 삭제하면
     * Then 지하철 노선 조회시 처음 등록한 지하철 노선과 동일하다
     */
    @DisplayName("구간 삭제 기능 - 성공 케이스")
    @Test
    void deleteSectionSuccess() {
        // given
        LineResponse lineOne = LineApiClient.requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        requestAppendSection(lineOne.getId(), stationC.getId(), stationB.getId(), 3);

        // when
        ExtractableResponse<Response> deleteSectionResponse = requestDeleteSection(lineOne.getId(), stationB.getId());
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> showLineResponse = LineApiClient.requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC);
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 지하철 구간 하나를 추가하고
     * When 하행 종점역이 아닐때 지하철 구간을 삭제하면
     * Then 지하철 노선 조회시 구간이 그대로 등록되어 있다.
     */
    @DisplayName("구간 삭제 기능 - 실패 케이스")
    @Test
    void deleteSectionFail() {
        // given
        LineResponse lineOne = LineApiClient.requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        requestAppendSection(lineOne.getId(), stationC.getId(), stationB.getId(), 3);

        // when
        ExtractableResponse<Response> deleteSectionResponse = requestDeleteSection(lineOne.getId(), stationA.getId());
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        ExtractableResponse<Response> showLineResponse = LineApiClient.requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC, stationB);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 삭제하면 구간이 한개 이므로
     * Then 지하철 노선 조회시 처음 생성한 노선 그대로 조회된다.
     */
    @DisplayName("구간 삭제 기능 - 실패 케이스2")
    @Test
    void deleteSectionFail2() {
        // given
        LineResponse lineOne = LineApiClient.requestCreateLine("1호선", "#0052A4", stationA.getId(), stationC.getId(), 7)
                .body().as(LineResponse.class);

        // when
        ExtractableResponse<Response> deleteSectionResponse = requestDeleteSection(lineOne.getId(), stationC.getId());
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        ExtractableResponse<Response> showLineResponse = LineApiClient.requestShowLine(lineOne.getId());
        LineResponse updatedLineOne = showLineResponse.body().as(LineResponse.class);

        assertThat(showLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineOne.getStations()).containsExactly(stationA, stationC);
    }
}
