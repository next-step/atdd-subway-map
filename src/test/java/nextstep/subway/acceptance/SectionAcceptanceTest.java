package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.SubwayTestUtils.createLine;
import static nextstep.subway.acceptance.SubwayTestUtils.createStationWithName;
import static nextstep.subway.acceptance.SubwayTestUtils.getLine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.acceptance.SubwayTestUtils.STATIONS;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.SectionCreationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("구간관리 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private Map<STATIONS, Long> stationIds = new HashMap<>();

    @BeforeEach
    void setUpStations() {
        List.of(STATIONS.광교역, STATIONS.광교중앙역, STATIONS.상현역, STATIONS.성복역).forEach(station -> {
            var stationId = createStationWithName(station.name()).jsonPath().getLong("id");
            stationIds.put(station, stationId);
            }
        );
    }

    @AfterEach
    void cleanStations() {
        stationIds.clear();
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 노선에 구간을 등록하면
     * Then 지하철 노선 조회시 등록한 구간을 확인할 수 있다
     */
    @DisplayName("구간 등록")
    @Test
    void canFindSectionOfLineWhichCreated() {
        // given
        var lineCreationRequest = new LineCreationRequest(
                "신분당선",
                "bg-red-600",
                stationIds.get(STATIONS.광교역),
                stationIds.get(STATIONS.광교중앙역),
                10L);
        var lineId = createLine(lineCreationRequest).jsonPath().getLong("id");

        // when
        var sectionCreationResponse = createSection(lineId, STATIONS.광교중앙역, STATIONS.상현역, 3L);

        // then
        var stationNames = getLine(lineId)
                .jsonPath()
                .getList("stations.name", String.class);

        assertAll(
                () -> assertThat(sectionCreationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(stationNames).isEqualTo(List.of(
                        STATIONS.광교역.name(), STATIONS.광교중앙역.name(), STATIONS.상현역.name()))
        );
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 하행역이 노선 내 포함된 구간을 노선에 등록하면
     * Then 에러가 발생한다
     */
    @DisplayName("하행역이 노선에 포함된 구간 등록 불가")
    @Test
    void canNotCreateSectionWithDownStationAlreadyInLineSections() {
        // given
        var lineCreationRequest = new LineCreationRequest(
                "신분당선",
                "bg-red-600",
                stationIds.get(STATIONS.광교역),
                stationIds.get(STATIONS.광교중앙역),
                10L);
        var lineId = createLine(lineCreationRequest).jsonPath().getLong("id");

        // when
        var sectionCreationResponse = createSection(lineId, STATIONS.광교중앙역, STATIONS.광교역, 3L);

        // then
        assertThat(sectionCreationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 상행역이 노선의 하행 종점역이 아닌 구간을 노선에 등록하면
     * Then 에러가 발생한다
     */
    @DisplayName("구간의 상행역이 노선의 하행 종점역이 아닐때 등록 불가")
    @Test
    void canNotCreateSectionWithUpStationWhichIsNotLinesLastStation() {
        // given
        var lineCreationRequest = new LineCreationRequest(
                "신분당선",
                "bg-red-600",
                stationIds.get(STATIONS.광교역),
                stationIds.get(STATIONS.광교중앙역),
                10L);
        var lineId = createLine(lineCreationRequest).jsonPath().getLong("id");

        // when
        var sectionCreationResponse = createSection(lineId, STATIONS.광교역, STATIONS.상현역, 3L);

        // then
        assertThat(sectionCreationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 존재하지 않는 역을 포함한 구간을 노선에 등록하면
     * Then 에러가 발생한다
     */
    @DisplayName("존재하지 않는 역을 포함한 구간 등록 불가")
    @Test
    void canNotCreateSectionWithStationWhichIsNotExist() {
        // given
        var lineCreationRequest = new LineCreationRequest(
                "신분당선",
                "bg-red-600",
                stationIds.get(STATIONS.광교역),
                stationIds.get(STATIONS.광교중앙역),
                10L);
        var lineId = createLine(lineCreationRequest).jsonPath().getLong("id");

        // when
        var sectionCreationResponse = createSection(lineId, STATIONS.광교중앙역, STATIONS.수지구청역, 3L);

        // then
        assertThat(sectionCreationResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 2개 이상의 구간이 포함된 지하철 노선을 등록하고
     * When 마지막 구간을 제거하면
     * Then 지하철 노선 조회시 마지막 구간이 제거된 노선을 확인할 수 있다
     */
    @DisplayName("노선의 마지막 구간 삭제")
    @Test
    void canDeleteLastSectionFromLine() {
        // given
        var lineCreationRequest = new LineCreationRequest(
                "신분당선",
                "bg-red-600",
                stationIds.get(STATIONS.광교역),
                stationIds.get(STATIONS.광교중앙역),
                10L);
        var lineId = createLine(lineCreationRequest).jsonPath().getLong("id");
        createSection(lineId, STATIONS.광교중앙역, STATIONS.상현역, 3L);
        createSection(lineId, STATIONS.상현역, STATIONS.성복역, 4L);

        // when
        var deleteResponse = deleteSection(lineId, STATIONS.성복역);

        // then
        var stationNames = getLine(lineId).jsonPath()
                .getList("stations.name", String.class);

        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationNames).containsExactly(
                        STATIONS.광교역.name(), STATIONS.광교중앙역.name(), STATIONS.상현역.name()
                )
        );
    }

    /**
     * Given 1개 구간만 포함된 지하철 노선을 등록하고
     * When 하행종점역을 제거하면
     * Then 에러가 발생한다
     */
    @DisplayName("상/하행 좀점만 남은 경우 구간 삭제 불가")
    @Test
    void canNotDeleteLastRemainSection() {
        // given
        var lineCreationRequest = new LineCreationRequest(
                "신분당선",
                "bg-red-600",
                stationIds.get(STATIONS.광교역),
                stationIds.get(STATIONS.광교중앙역),
                10L);
        var lineId = createLine(lineCreationRequest).jsonPath().getLong("id");

        // when
        var deleteResponse = deleteSection(lineId, STATIONS.광교중앙역);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 2개 이상의 구간이 포함된 지하철 노선을 등록하고
     * When 하행종점역이 아닌 역을 제거하면
     * Then 에러가 발생한다
     */
    @DisplayName("하행 좀정역이 아닌 역 제거 불가")
    @Test
    void canNotDeleteNotLastStationFromLine() {
        // given
        var lineCreationRequest = new LineCreationRequest(
                "신분당선",
                "bg-red-600",
                stationIds.get(STATIONS.광교역),
                stationIds.get(STATIONS.광교중앙역),
                10L);
        var lineId = createLine(lineCreationRequest).jsonPath().getLong("id");
        createSection(lineId, STATIONS.광교중앙역, STATIONS.상현역, 3L);

        // when
        var deleteResponse = deleteSection(lineId, STATIONS.광교중앙역);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 노선에 등록되지 않은 역을 제거하면
     * Then 에러가 발생한다
     */


    private ExtractableResponse<Response> createSection(Long lineId, STATIONS upStation, STATIONS downStation, Long distance) {
        var sectionCreationRequest = new SectionCreationRequest(
                stationIds.get(downStation),
                stationIds.get(upStation),
                distance);
        return RestAssured
                .given()
                    .pathParam("lineId", lineId)
                    .body(sectionCreationRequest, ObjectMapperType.JACKSON_2)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines/{lineId}/sections")
                .then()
                    .extract();
    }

    private ExtractableResponse<Response> deleteSection(Long lineId, STATIONS station) {
        return RestAssured
                .given()
                    .pathParam("lineId", lineId)
                    .queryParam("stationId", stationIds.get(station))
                .when()
                    .delete("/lines/{lineId}/sections")
                .then().log().all()
                    .extract();
    }
}
