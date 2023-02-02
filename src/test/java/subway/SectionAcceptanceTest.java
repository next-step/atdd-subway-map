package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;
import subway.section.SectionRequest;
import subway.station.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.AcceptanceTestHelper.*;

@DisplayName("지하철 구간 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/stations.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {
    private static final LineRequest 신분당선 =
            LineRequest.of("신분당선", "bg-red-600", 1L, 2L, 10);
    private static final SectionRequest 구간 = SectionRequest.of(2L, 3L, 3);
    private static final SectionRequest 노선의하행역과일치하지않은구간 = SectionRequest.of(3L, 3L, 3);
    private static final SectionRequest 하행역이이미포함된구간 = SectionRequest.of(2L, 1L, 3);

    /**
     * Given 처음 지하철 노선이 생성되면 구간도 함꼐 생성된다.
     * When 지하철 노선에 새로운 구간을 추가한다.
     * Then 지하철 노선 조회 시 모든 구간의 지하철역들을 찾을 수 있다.
     */
    @DisplayName("지하철 노선에 지하철 구간을 추가한다")
    @Test
    void createSection() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> postResponse = post("/lines/{id}/sections", 1, 구간);
        ExtractableResponse<Response> listResponse = get("/lines/{id}", 1);

        //then
        assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.jsonPath().getList("stations.name")).containsExactly("지하철역", "또다른지하철역", "새로운지하철역");
    }

    /**
     * Given 지하철 노선에 새로운 구간을 추가한다.
     * When 지하철 노선에 추가하려는 구간의 상행역이 노선의 하행역과 일치하지 않은 구간을 추가한다.
     * Then 에러가 발생한다.
     */
    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이지 않을 때 에러 체크한다.")
    @Test
    void lastStationToAddSectionTest() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> response = post("/lines/{id}/sections", 1, 노선의하행역과일치하지않은구간);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
    }

    /**
     * Given 지하철 노선에 새로운 구간을 추가한다.
     * When 지하철 노선에 추가하려는 새로운 구간의 하행역에 이 노선에 포함된 역을 추가한다.
     * Then 에러가 발생한다.
     */
    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어 있으면 에러 체크한다.")
    @Test
    void containsLastStationTest() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> response = post("/lines/{id}/sections", 1, 하행역이이미포함된구간);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");
    }

    /**
     * Given 지하철 노선에 새로운 구간을 추가한다.
     * When 지하철 노선의 마지막 구간을 제거한다.
     * Then 지하철 노선에서 해당 구간이 제거되고, 제거된 역은 제외한 해당 노선의 모든 지하철 역을 조회한다.
     */
    @DisplayName("지하철 노선에 지하철 구간을 제거한다.")
    @Test
    void deleteSection() {
        //given
        post("/lines", 신분당선);
        post("/lines/{id}/sections", 1, 구간);

        //when
        ExtractableResponse<Response> deleteResponse =
                delete("/lines/{id}/sections", 1, "stationId", 3L);
        ExtractableResponse<Response> getListResponse = get("/lines/{id}", 1);
        List<StationResponse> stations = getListResponse.jsonPath().getList("stations");

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(stations).hasSize(2);
    }

    /**
     * Given 지하철 노선에 새로운 구간을 추가한다.
     * When 그 구간은 마지막 노선이 아닌 곳을 제거한다.
     * Then StatusCode는 BadRequest이고, 에러 발생한다.
     */
    @DisplayName("지하철 노선에 등록된 하행 종점역(마지막 구간의 하행 종점역)이 아니면 에러 발생한다.")
    @Test
    void isLastStationInLastSectionErrorTest() {
        //given
        post("/lines", 신분당선);
        post("/lines/{id}/sections", 1, 구간);

        //when
        ExtractableResponse<Response> response =
                delete("/lines/{id}/sections", 1, "stationId", 2L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("지하철 노선에 등록된 하행 종점역만 제거할 수 있습니다.");
    }

    /**
     * Given 구간이 하나인 노선을 생성한다.
     * When 그 노선을 지운다.
     * Then StatusCode는 BadRequest이고, 에러 발생한다.
     */
    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 에러 발생.")
    @Test
    void singleSectionErrorTest() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> response =
                delete("/lines/{id}/sections", 1, "stationId", 2L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("지하철 노선에 구간이 1개인 경우에는 역을 삭제할 수 없습니다.");
    }
}