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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Map<String, Long> stationIds = new HashMap<>();
    private final List<String> preparedStationNames = List.of(
            "광교역", "광교중앙역", "상현역", "성복역"
    );
    private final String defaultUpStation = "광교역";
    private final String defaultDownStation = "광교중앙역";
    private Long lineId;

    @BeforeEach
    void setUpStations() {
        preparedStationNames.forEach(name -> {
            var stationId = createStationWithName(name).jsonPath().getLong("id");
            stationIds.put(name, stationId);
            }
        );

        lineId = createLineWith(
                "신분당선",
                "red",
                stationIds.get(defaultUpStation),
                stationIds.get(defaultDownStation),
                10L);
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
        // when
        var newStation = "상현역";
        var sectionCreationResponse = createSection(lineId, defaultDownStation, newStation, 3L);

        // then
        var stationNames = getLine(lineId)
                .jsonPath()
                .getList("stations.name", String.class);

        assertAll(
                () -> assertThat(sectionCreationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(stationNames).isEqualTo(List.of(defaultUpStation, defaultDownStation, newStation))
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
        // when
        var sectionCreationResponse = createSection(lineId, defaultDownStation, defaultUpStation, 3L);

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
        // when
        var newStation = "상현역";
        var sectionCreationResponse = createSection(lineId, defaultUpStation, newStation, 3L);

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
        // when
        var stationNotExist = "수지구청역";
        var sectionCreationResponse = createSection(lineId, defaultDownStation, stationNotExist, 3L);

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
        var thirdStation = "상현역";
        var fourthStation = "성복역";
        createSection(lineId, defaultDownStation, thirdStation, 3L);
        createSection(lineId, thirdStation, fourthStation, 4L);

        // when
        var deleteResponse = deleteSection(lineId, fourthStation);

        // then
        var stationNames = getLine(lineId).jsonPath()
                .getList("stations.name", String.class);

        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationNames).containsExactly(
                        defaultUpStation, defaultDownStation, thirdStation
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
        // when
        var deleteResponse = deleteSection(lineId, defaultDownStation);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 2개 이상의 구간이 포함된 지하철 노선을 등록하고
     * When 하행종점역이 아닌 역을 제거하면
     * Then 에러가 발생한다
     */
    @DisplayName("하행 종정역이 아닌 역 제거 불가")
    @Test
    void canNotDeleteNotLastStationFromLine() {
        // given
        var lastStation = "상현역";
        createSection(lineId, defaultDownStation, lastStation, 3L);

        // when
        var deleteResponse = deleteSection(lineId, defaultDownStation);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 노선에 등록되지 않은 역을 제거하면
     * Then 에러가 발생한다
     */
    @DisplayName("노선에 등록되지 않은 역 제거 불가")
    @Test
    void canNotDeleteStationNotInLine() {
        // given
        var lastStation = "상현역";
        createSection(lineId, defaultDownStation, lastStation, 3L);

        // when
        var deleteResponse = deleteSection(lineId, "성복역");

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private ExtractableResponse<Response> createSection(Long lineId, String upStation, String downStation, Long distance) {
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

    private ExtractableResponse<Response> deleteSection(Long lineId, String station) {
        return RestAssured
                .given()
                    .pathParam("lineId", lineId)
                    .queryParam("stationId", stationIds.get(station))
                .when()
                    .delete("/lines/{lineId}/sections")
                .then().log().all()
                    .extract();
    }

    private Long createLineWith(String name, String color, Long upStationId, Long downStationId, Long distance) {
        var lineCreationRequest = new LineCreationRequest(
                name,
                color,
                upStationId,
                downStationId,
                10L);
        return createLine(lineCreationRequest).jsonPath().getLong("id");
    }
}
