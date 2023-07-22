package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.acceptance.StationAcceptanceTest.지하철_역_생성;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.domain.Line;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestAssuredUtil restAssuredUtil;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setup() {
        restAssuredUtil.cleanup();
        restAssuredUtil.initializePort(port);
        지하철_역_생성("강남역");
        지하철_역_생성("신논현역");
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선ID를 입력하고
     * and 노선ID에 해당하는 노선이 존재하지 않으면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록_노선 미존재 오류")
    @Test
    void createSectionWithLineNotFoundException() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        // and
        Long lineId = 1L;

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        // and
        ExtractableResponse<Response> lines = RestAssuredUtil.findAllWithOk("/lines");
        assertThat(lines.jsonPath().getList("id", Long.class)).doesNotContain(lineId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil
            .createWithBadRequest("/lines/" + lineId + "/sections", param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + lineId)
        );
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 하행역ID에 해당하는 역정보가 존재하지 않으면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록_하행역 미존재 오류")
    @Test
    void createSectionWithDownStationNotFoundException() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines", map);

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        // and
        ExtractableResponse<Response> downStation = RestAssuredUtil.findAllWithOk("/stations");
        assertThat(downStation.jsonPath().getList("id", Long.class)).doesNotContain(sectionDownStationId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.createWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("입력된 ID에 해당하는 역이 존재하지 않습니다: " + sectionDownStationId)
        );
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 하행역ID에 해당하는 역정보가 존재하고
     * and 상행역ID에 해당하는 역정보가 존재하지 않으면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록_상행역 미존재 오류")
    @Test
    void createSectionWithUpStationNotFoundException() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        // and
        Long sectionDownStationId = 3L;
        Long sectionUpStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines", map);

        // and
        지하철_역_생성("판교역");
        List<Long> stationIdList = RestAssuredUtil.findAllWithOk("/stations").jsonPath()
            .getList("id", Long.class);
        assertThat(stationIdList).contains(sectionDownStationId);

        // and
        assertThat(stationIdList).doesNotContain(sectionUpStationId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.createWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections",
            param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("입력된 ID에 해당하는 역이 존재하지 않습니다: " + sectionUpStationId)
        );
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 하행역ID에 해당하는 역정보가 존재하고
     * and 상행역ID에 해당하는 역정보가 존재하고
     * and 입력 구간의 상행역이 등록하려는 노선의 하행 종점역이 아니면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록_상행역 노선 하행 종착역 불일치 오류")
    @Test
    void createSectionWithUpStationNotEqualToLineDownEndStationException() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        지하철_역_생성("판교역");
        지하철_역_생성("양재역");

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines", map);

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of(
            "downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        // and
        List<Long> stationIdList = RestAssuredUtil.findAllWithOk("/stations")
            .jsonPath()
            .getList("id", Long.class);
        assertThat(stationIdList).contains(sectionDownStationId);

        // and
        assertThat(stationIdList).contains(sectionUpStationId);

        // and
        assertThat(sectionUpStationId).isNotEqualTo(downStationId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.createWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message"))
                .isEqualTo("새로운 구간은 노선의 하행종착역을 상행역으로 설정해야 합니다: " + downStationId + " <> " + sectionUpStationId)
        );
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 하행역ID에 해당하는 역정보가 존재하고
     * and 상행역ID에 해당하는 역정보가 존재하고
     * and 입력 구간의 상행역이 등록하려는 노선의 하행 종점역이고
     * and 입력 구간의 하행역이 등록하려는 노선에 존재하는 역이면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록_하행역 노선 기존재 오류")
    @Test
    void createSectionWithDownStationAlreadyExistsInLineException() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        지하철_역_생성("판교역");
        지하철_역_생성("양재역");
        RestAssuredUtil.createWithCreated("/lines", map);

        // and
        Long sectionUpStationId = 1L;
        Long sectionDownStationId = 2L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of(
            "downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        // and
        List<Long> stationIdList = RestAssuredUtil.findAllWithOk("/stations")
            .jsonPath()
            .getList("id", Long.class);
        assertThat(stationIdList).contains(sectionDownStationId);

        // and
        assertThat(stationIdList).contains(sectionUpStationId);

        // and
        assertThat(sectionUpStationId).isEqualTo(downStationId);

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.findByIdWithOk("/lines/{id}", 1L);
        assertThat(List.of(line.jsonPath().getLong("upStation.id"), line.jsonPath().getLong("downStation.id"))).contains(sectionDownStationId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil
            .createWithBadRequest("/lines/" + line.jsonPath().getLong("id") + "/sections", param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("신규 구간의 하행역이 기존 노선에 포함되어 있습니다: " + sectionDownStationId)
        );
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 하행역ID에 해당하는 역정보가 존재하고
     * and 상행역ID에 해당하는 역정보가 존재하고
     * and 입력 구간의 상행역이 등록하려는 노선의 하행 종점역이고
     * and 입력 구간의 하행역이 등록하려는 노선에 존재하지 않는 역이면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 정상 등록 됩니다.
     */
    @DisplayName("구간 등록_정상")
    @Test
    void createSection() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        지하철_역_생성("판교역");
        RestAssuredUtil.createWithCreated("/lines", map);

        // and
        Long sectionUpStationId = 1L;
        Long sectionDownStationId = 3L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of(
            "downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        // and
        List<Long> stationIdList = RestAssuredUtil.findAllWithOk("/stations")
            .jsonPath()
            .getList("id", Long.class);
        assertThat(stationIdList).contains(sectionDownStationId);

        // and
        assertThat(stationIdList).contains(sectionUpStationId);

        // and
        assertThat(sectionUpStationId).isEqualTo(downStationId);

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.findByIdWithOk("/lines/{id}", 1L);
        assertThat(List.of(line.jsonPath().getLong("upStation.id"), line.jsonPath().getLong("downStation.id"))).doesNotContain(sectionDownStationId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil
            .createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections", param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.jsonPath().getLong("downStationId")).isEqualTo(sectionDownStationId),
            () -> assertThat(response.jsonPath().getLong("upStationId")).isEqualTo(upStationId),
            () -> assertThat(response.jsonPath().getLong("distance")).isEqualTo(15L)
        );
    }

    /**
     * given 삭제하려는 노선ID와 역ID가 있을때
     * and 입력된 ID에 해당하는 노선이 존재하지 않으면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_미존재 노선")
    @Test
    void deleteWithNotExistsLine() {
        //given
        Long deleteStationId = 1L;
        Long lineId = 1L;

        // and
        ExtractableResponse<Response> lineList = RestAssuredUtil.findAllWithOk("/lines");
        assertThat(lineList.jsonPath().getList("id", Long.class)).doesNotContain(lineId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            String.format("/lines/%d/sections?stationId={stationId}", lineId),
            deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + lineId)
        );
    }

    /**
     * given 삭제하려는 노선ID와 역ID가 있을때
     * and 입력된 ID에 해당하는 노선이 존재하고
     * and 입력된 ID에 해당하는 역이 존재하지 않으면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_미존재 역")
    @Test
    void deleteWithNotExistsStation() {
        //given
        Long deleteStationId = 3L;
        Long lineId = 1L;

        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        RestAssuredUtil.createWithCreated("/lines", map);

        // and
        ExtractableResponse<Response> lineList = RestAssuredUtil.findAllWithOk("/lines");
        assertThat(lineList.jsonPath().getList("lines.id", Long.class)).contains(lineId);

        // and
        ExtractableResponse<Response> stationList = RestAssuredUtil.findAllWithOk("/stations");
        assertThat(stationList.jsonPath().getList("id", Long.class)).doesNotContain(deleteStationId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            String.format("/lines/%d/sections?stationId={stationId}", lineId),
            deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("입력된 ID에 해당하는 역이 존재하지 않습니다: " + deleteStationId)
        );
    }

    /**
     * given 삭제하려는 노선ID와 역ID가 있을때
     * and 입력된 ID에 해당하는 노선이 존재하고
     * and 입력된 ID에 해당하는 역이 존재하고
     * and 삭제하려는 역이 해당 노선에 포함되는 역이 아니면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_미포함 역")
    @Test
    void deleteWithNotContainedStation() {
        //given
        Long deleteStationId = 3L;
        Long lineId = 1L;
        지하철_역_생성("양재역");

        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        RestAssuredUtil.createWithCreated("/lines", map);

        // and
        ExtractableResponse<Response> lineList = RestAssuredUtil.findAllWithOk("/lines");
        assertThat(lineList.jsonPath().getList("lines.id", Long.class)).contains(lineId);

        // and
        ExtractableResponse<Response> stationList = RestAssuredUtil.findAllWithOk("/stations");
        assertThat(stationList.jsonPath().getList("id", Long.class)).contains(deleteStationId);

        // and
        Line line = lineRepository.findById(1L)
            .orElseThrow(() -> new InvalidParameterException("테스트_노선 조회 결과 없음"));
        Station deleteStation = stationRepository.findById(deleteStationId)
            .orElseThrow(() -> new InvalidParameterException("테스트_역 조회 결과 없음"));
        assertThat(line.containsStation(deleteStation)).isFalse();

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            String.format("/lines/%d/sections?stationId={stationId}", lineId),
            deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("입력된 ID에 해당하는 역이 노선에 포함되지 않습니다: " + deleteStationId)
        );
    }

    /**
     * given 2개 이상의 구간을 포함하는 노선과 삭제하려는 역ID가 있을때
     * and 삭제하려는 역이 해당 노선의 하행종착역이 아니면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_하행 종착역 아님")
    @Test
    void deleteWithNotDownEndStation() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        Long newSectionUpStationId = downStationId;
        Long newSectionDownStationId = 3L;
        Long newSectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", newSectionDownStationId,
            "upStationId", newSectionUpStationId,
            "distance", newSectionDistance);

        RestAssuredUtil.createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        Long deleteStationId = 4L;

        // and
        assertThat(deleteStationId).isNotEqualTo(line.jsonPath().getLong("stations.downStation.id"));

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections?stationsId=", deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("error.message")).isEqualTo("노선의 하행 종착역만 삭제 가능합니다.")
        );
    }

    /**
     * given 노선과 삭제하려는 역ID가 있고
     * and 역이 노선에 포함되고
     * and 노선에 구간이 하나만 존재하면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_하행 종착역 아님")
    @Test
    void deleteStationFromLineContainingOnlyOneSection() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines", map);

        Long newSectionUpStationId = downStationId;
        Long newSectionDownStationId = 3L;
        Long newSectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", newSectionDownStationId,
            "upStationId", newSectionUpStationId,
            "distance", newSectionDistance);

        RestAssuredUtil.createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections", param);

        Long deleteStationId = 4L;

        // and
        assertThat(line.jsonPath().getList("stations.id").contains(deleteStationId)).isTrue();

        // and
        assertThat(line.jsonPath().getList("sections")).hasSize(1);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections?stationsId=", deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("error.message")).isEqualTo("노선에 구간이 1개만 존재해 역을 삭제할 수 없습니다.")
        );
    }

    /**
     * given 노선과 삭제하려는 역ID가 있고
     * and 삭제하려는 역이 노선의 하행 종착역이고
     * and 노선에 구간이 2개 이상이면
     * when 노선에서 구간을 삭제하려 할때
     * then 정상 삭제됩니다.
     */
    @DisplayName("구간 제거 정상")
    @Test
    void deleteSectionFromLine() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines", map);

        Long newSectionUpStationId = downStationId;
        Long newSectionDownStationId = 3L;
        Long newSectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", newSectionDownStationId,
            "upStationId", newSectionUpStationId,
            "distance", newSectionDistance);

        RestAssuredUtil.createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        // and
        Long deleteStationId = newSectionDownStationId;

        // and
        assertThat(deleteStationId).isEqualTo(line.jsonPath().get("stations.downEndStation.id"));

        // and
        assertThat(line.jsonPath().getList("sections")).hasSizeGreaterThan(1);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithNoContent(
            "/lines/" + line.jsonPath().getLong("id") + "/sections?stationsId=", deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
            () -> assertThat(response.body().asString()).isBlank()
        );
    }
}
