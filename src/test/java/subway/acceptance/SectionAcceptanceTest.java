package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.acceptance.LineAcceptanceTest.*;
import static subway.acceptance.StationAcceptanceTest.*;
import static subway.acceptance.StationAcceptanceTest.지하철_역_생성;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestAssuredUtil restAssuredUtil;

    @BeforeEach
    void setup() {
        restAssuredUtil.cleanup();
        restAssuredUtil.initializePort(port);
        지하철_역_생성("강남역");
        지하철_역_생성("신논현역");
    }

    public static ExtractableResponse<Response> 노선에_구간을_추가합니다(Long 노선_ID, Long 노선_하행_종착역_ID, Long 추가_구간_역_ID, Long 추가_구간_길이) {
        return RestAssuredUtil.createWithCreated("/lines/" + 노선_ID + "/sections", Map.of(
            "upStationId", 노선_하행_종착역_ID,
            "downStationId", 추가_구간_역_ID,
            "distance", 추가_구간_길이
        ));
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

        // and
        Long lineId = 1L;

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = 구간_추가_요청정보를_생성합니다(sectionUpStationId, sectionDownStationId,
            sectionDistance);

        // and
        ExtractableResponse<Response> lines = 지하철_노선_목록을_조회합니다();
        assertThat(역ID_목록(lines)).doesNotContain(lineId);

        //when
        ExtractableResponse<Response> response = 지하철_구간을_추가합니다(lineId, param);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + lineId)
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

        // and
        ExtractableResponse<Response> line = 지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = 구간_추가_요청정보를_생성합니다(sectionUpStationId, sectionDownStationId,
            sectionDistance);

        // and
        ExtractableResponse<Response> downStation = 지하철_역_목록을_조회합니다();
        assertThat(역ID_목록(downStation)).doesNotContain(sectionDownStationId);

        //when
        ExtractableResponse<Response> response = 지하철_구간을_추가합니다(line.jsonPath().getLong("id"),
            param);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("입력된 ID에 해당하는 역이 존재하지 않습니다: " + sectionDownStationId)
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

        // and
        Long sectionDownStationId = 3L;
        Long sectionUpStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = 구간_추가_요청정보를_생성합니다(sectionUpStationId, sectionDownStationId,
            sectionDistance);

        // and
        ExtractableResponse<Response> line = 지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        // and
        지하철_역_생성("판교역");
        List<Long> stationIdList = 역ID_목록(지하철_역_목록을_조회합니다());
        assertThat(stationIdList).contains(sectionDownStationId);

        // and
        assertThat(stationIdList).doesNotContain(sectionUpStationId);

        //when
        ExtractableResponse<Response> response = 지하철_구간을_추가합니다(line.jsonPath().getLong("id"),
            param);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("입력된 ID에 해당하는 역이 존재하지 않습니다: " + sectionUpStationId)
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
        지하철_역_생성("판교역");
        지하철_역_생성("양재역");

        // and
        ExtractableResponse<Response> line = 지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = 구간_추가_요청정보를_생성합니다(sectionUpStationId, sectionDownStationId,
            sectionDistance);

        // and
        List<Long> stationIdList = 역ID_목록(RestAssuredUtil.findAllWithOk("/stations"));
        assertThat(stationIdList).contains(sectionDownStationId);

        // and
        assertThat(stationIdList).contains(sectionUpStationId);

        // and
        assertThat(sectionUpStationId).isNotEqualTo(downStationId);

        //when
        ExtractableResponse<Response> response = 지하철_구간을_추가합니다(line.jsonPath().getLong("id"),
            param);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response))
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

        지하철_역_생성("판교역");
        지하철_역_생성("양재역");
        지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        // and
        Long sectionUpStationId = 1L;
        Long sectionDownStationId = 2L;
        Long sectionDistance = 5L;
        Map<String, Object> param = 구간_추가_요청정보를_생성합니다(sectionUpStationId, sectionDownStationId,
            sectionDistance);

        // and
        List<Long> stationIdList = 역ID_목록(지하철_역_목록을_조회합니다());
        assertThat(stationIdList).contains(sectionDownStationId);

        // and
        assertThat(stationIdList).contains(sectionUpStationId);

        // and
        assertThat(sectionUpStationId).isEqualTo(downStationId);

        // and
        ExtractableResponse<Response> line = 지하철_노선을_ID로_조회합니다(1L);
        assertThat(노선에_포함된_역_목록을_반환합니다(line)).contains(sectionDownStationId);

        //when
        ExtractableResponse<Response> response = 지하철_구간을_추가합니다(line.jsonPath().getLong("id"),
            param);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("신규 구간의 하행역이 기존 노선에 포함되어 있습니다: " + sectionDownStationId)
        );
    }

    private List<Long> 노선에_포함된_역_목록을_반환합니다(ExtractableResponse<Response> line) {
        return List.of(line.jsonPath().getLong("upStation.id"),
            line.jsonPath().getLong("downStation.id"));
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
        Long 노선_하행종착역_ID = 1L;
        Long 노선_상행종착역_ID = 2L;
        Long 노선_최초길이 = 10L;
        String name = "신분당선";
        String color = "bg-red-600";

        지하철_역_생성("판교역");
        지하철_노선을_생성합니다(노선_상행종착역_ID, 노선_하행종착역_ID, name, color, 노선_최초길이);

        // and
        Long 구간_상행역_ID = 1L;
        Long 구간_하행역_ID = 3L;
        Long 구간_길이 = 5L;
        Map<String, Object> param = 구간_추가_요청정보를_생성합니다(구간_상행역_ID, 구간_하행역_ID,
            구간_길이);

        // and
        List<Long> 존재하는_역_ID_목록 = 역ID_목록(지하철_역_목록을_조회합니다());
        assertThat(존재하는_역_ID_목록).contains(구간_하행역_ID);

        // and
        assertThat(존재하는_역_ID_목록).contains(구간_상행역_ID);

        // and
        assertThat(구간_상행역_ID).isEqualTo(노선_하행종착역_ID);

        // and
        ExtractableResponse<Response> line = 지하철_노선을_ID로_조회합니다(1L);
        assertThat(노선에_포함된_역_목록을_반환합니다(line)).doesNotContain(구간_하행역_ID);

        //when
        ExtractableResponse<Response> response = 지하철_노선에_구간을_추가합니다(param, line);

        // then
        assertAll(
            () -> assertThat(응답상태코드를_반환합니다(response)).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(하행_종착역_ID를_반환합니다(response)).isEqualTo(구간_하행역_ID),
            () -> assertThat(상행_종착역_ID를_반환합니다(response)).isEqualTo(노선_상행종착역_ID),
            () -> assertThat(노선의_길이를_반환합니다(response)).isEqualTo(15L)
        );
    }

    private long 하행_종착역_ID를_반환합니다(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("downStationId");
    }

    private int 응답상태코드를_반환합니다(ExtractableResponse<Response> response) {
        return response.statusCode();
    }

    private ExtractableResponse<Response> 지하철_노선에_구간을_추가합니다(Map<String, Object> param,
        ExtractableResponse<Response> line) {
        return RestAssuredUtil
            .createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections", param);
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
        Long 제거_역_ID = 1L;
        Long 제거_노선_ID = 1L;

        // and
        ExtractableResponse<Response> 전체_지하철_노선_목록 = 지하철_노선_목록을_조회합니다();
        assertThat(역ID_목록(전체_지하철_노선_목록)).doesNotContain(제거_노선_ID);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_역을_제거합니다(제거_역_ID, 제거_노선_ID);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + 제거_노선_ID)
        );
    }

    private ExtractableResponse<Response> 지하철_노선에서_역을_제거합니다(Long 제거_역_ID, Long 제거_노선_ID) {
        return RestAssuredUtil.deleteWithBadRequest(
            String.format("/lines/%d/sections?stationId={stationId}", 제거_노선_ID),
            제거_역_ID);
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
        Long 제거_역_ID = 3L;
        Long 제거_노선_ID = 1L;

        Long 노선_하행_종착역_ID = 1L;
        Long 노선_상행_종착역_ID = 2L;
        Long 노선_길이 = 10L;
        String name = "신분당선";
        String color = "bg-red-600";

        지하철_노선을_생성합니다(노선_상행_종착역_ID, 노선_하행_종착역_ID, name, color, 노선_길이);

        // and
        ExtractableResponse<Response> 노선_목록 = 지하철_노선_목록을_조회합니다();
        assertThat(노선의_ID_목록(노선_목록)).contains(제거_노선_ID);

        // and
        ExtractableResponse<Response> 역_목록 = 지하철_역_목록을_조회합니다();
        assertThat(역ID_목록(역_목록)).doesNotContain(제거_역_ID);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_역을_제거합니다(제거_역_ID, 제거_노선_ID);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("입력된 ID에 해당하는 역이 존재하지 않습니다: " + 제거_역_ID)
        );
    }

    private List<Long> 노선의_ID_목록(ExtractableResponse<Response> lineList) {
        return lineList.jsonPath().getList("lines.id", Long.class);
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
        Long 삭제_역_ID = 3L;
        Long 노선_ID = 1L;
        지하철_역_생성("양재역");

        Long 노선_하행_종착역_ID = 1L;
        Long 노선_상행_종착역_ID = 2L;
        Long 노선_길이 = 10L;
        String name = "신분당선";
        String color = "bg-red-600";

        지하철_노선을_생성합니다(노선_상행_종착역_ID, 노선_하행_종착역_ID, name, color, 노선_길이);

        // and
        ExtractableResponse<Response> 지하철_노선_목록 = 지하철_노선_목록을_조회합니다();
        assertThat(노선의_ID_목록(지하철_노선_목록)).contains(노선_ID);

        // and
        ExtractableResponse<Response> 역_목록 = 지하철_역_목록을_조회합니다();
        assertThat(역ID_목록(역_목록)).contains(삭제_역_ID);

        // and
        assertThat(List.of(노선_하행_종착역_ID, 노선_상행_종착역_ID)).doesNotContain(삭제_역_ID);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_역을_제거합니다(삭제_역_ID, 노선_ID);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("입력된 ID에 해당하는 역이 노선에 포함되지 않습니다: " + 삭제_역_ID)
        );
    }

    /**
     * given 삭제하려는 노선ID와 역ID가 있을때
     * and 입력된 ID에 해당하는 노선이 존재하고
     * and 입력된 ID에 해당하는 역이 존재하고
     * and 삭제하려는 역이 해당 노선에 포함되는 역이고
     * and 삭제하려는 역이 해당 노선의 하행종착역이 아니면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_하행 종착역 아님")
    @Test
    void deleteWithNotDownEndStation() {
        //given
        Long 삭제_역_ID = 2L;
        Long 노선_ID = 1L;
        지하철_역_생성("양재역");

        Long 노선_하행_종착역_ID = 1L;
        Long 노선_상행_종착역_ID = 2L;
        Long 노선_길이 = 10L;
        String name = "신분당선";
        String color = "bg-red-600";

        지하철_노선을_생성합니다(노선_상행_종착역_ID, 노선_하행_종착역_ID, name, color, 노선_길이);

        // and
        ExtractableResponse<Response> 지하철_노선_목록 = 지하철_노선_목록을_조회합니다();
        assertThat(노선의_ID_목록(지하철_노선_목록)).contains(노선_ID);

        // and
        ExtractableResponse<Response> 역_목록 = 지하철_역_목록을_조회합니다();
        assertThat(역ID_목록(역_목록)).contains(삭제_역_ID);

        // and
        assertThat(List.of(노선_하행_종착역_ID, 노선_상행_종착역_ID)).contains(삭제_역_ID);

        // and
        assertThat(삭제_역_ID).isNotEqualTo(노선_하행_종착역_ID);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_역을_제거합니다(삭제_역_ID, 노선_ID);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("노선의 하행종착역만 삭제 가능합니다: " + 노선_하행_종착역_ID + " <> " + 삭제_역_ID)
        );
    }

    /**
     * given 삭제하려는 노선ID와 역ID가 있을때
     * and 입력된 ID에 해당하는 노선이 존재하고
     * and 입력된 ID에 해당하는 역이 존재하고
     * and 삭제하려는 역이 해당 노선에 포함되는 역이고
     * and 삭제하려는 역이 해당 노선의 하행종착역이고
     * and 노선에 구간이 하나만 존재하면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_단일 구간 노선")
    @Test
    void deleteStationFromLineContainingOnlyOneSection() {
        //given
        Long 삭제_역_ID = 1L;
        Long 노선_ID = 1L;

        Long 노선_하행_종착역_ID = 1L;
        Long 노선_상행_종착역_ID = 2L;
        Long 노선_길이 = 10L;
        String name = "신분당선";
        String color = "bg-red-600";

        지하철_노선을_생성합니다(노선_상행_종착역_ID, 노선_하행_종착역_ID, name, color, 노선_길이);

        // and
        ExtractableResponse<Response> 지하철_노선_목록 = 지하철_노선_목록을_조회합니다();
        assertThat(노선의_ID_목록(지하철_노선_목록)).contains(노선_ID);

        // and
        ExtractableResponse<Response> 역_목록 = 지하철_역_목록을_조회합니다();
        assertThat(역ID_목록(역_목록)).contains(삭제_역_ID);

        // and
        assertThat(List.of(노선_하행_종착역_ID, 노선_상행_종착역_ID)).contains(삭제_역_ID);

        // and
        assertThat(삭제_역_ID).isEqualTo(노선_하행_종착역_ID);

        // and
        Set<Long> stations = new HashSet<>(Set.of(노선_상행_종착역_ID, 노선_하행_종착역_ID));
        stations.remove(삭제_역_ID);
        assertThat(stations).hasSize(1);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_역을_제거합니다(삭제_역_ID, 노선_ID);

        // then
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("단일 구간 노선은 구간 제거가 불가합니다.")
        );
    }

    /**
     * given 삭제하려는 노선ID와 역ID가 있을때
     * and 입력된 ID에 해당하는 노선이 존재하고
     * and 입력된 ID에 해당하는 역이 존재하고
     * and 삭제하려는 역이 해당 노선에 포함되는 역이고
     * and 삭제하려는 역이 해당 노선의 하행종착역이고
     * and 노선에 구간이 2개 이상 존재하면
     * when 노선에서 구간을 삭제하려 할때
     * then 정상 삭제됩니다.
     */
    @DisplayName("구간 제거_정상")
    @Test
    void deleteSectionFromLine() {
        //given
        Long 삭제_역_ID = 3L;
        Long 삭제_노선_ID = 1L;
        지하철_역_생성("양재역");

        Long 노선_하행_종착역_ID = 1L;
        Long 노선_상행_종착역_ID = 2L;
        Long 노선_길이 = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        지하철_노선을_생성합니다(노선_상행_종착역_ID, 노선_하행_종착역_ID, name, color, 노선_길이);

        Long 추가_구간_역_ID = 3L;
        Long 추가_구간_길이 = 30L;
        노선에_구간을_추가합니다(삭제_노선_ID, 노선_하행_종착역_ID, 추가_구간_역_ID, 추가_구간_길이);

        // and
        ExtractableResponse<Response> 지하철_노선_목록 = 지하철_노선_목록을_조회합니다();
        assertThat(노선의_ID_목록(지하철_노선_목록)).contains(삭제_노선_ID);

        // and
        ExtractableResponse<Response> 역_목록 = 지하철_역_목록을_조회합니다();
        assertThat(역ID_목록(역_목록)).contains(삭제_역_ID);

        // and
        assertThat(노선_전체_역_목록(노선_하행_종착역_ID, 노선_상행_종착역_ID, 추가_구간_역_ID)).contains(삭제_역_ID);

        // and
        assertThat(삭제_역_ID).isEqualTo(추가_구간_역_ID);

        // and
        Set<Long> 노선_역_ID_목록 = new HashSet<>(Set.of(노선_상행_종착역_ID, 노선_하행_종착역_ID, 추가_구간_역_ID));
        노선_역_ID_목록.remove(삭제_역_ID);
        assertThat(노선_역_ID_목록).hasSizeGreaterThan(1);

        //when
        ExtractableResponse<Response> response = 노선에서_역을_삭제합니다(삭제_역_ID, 삭제_노선_ID);

        // then
        assertAll(
            () -> assertThat(응답상태코드를_반환합니다(response)).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(상행_종착역_ID를_반환합니다(response)).isEqualTo(노선_상행_종착역_ID),
            () -> assertThat(하행_종착역_ID를_반환합니다(response)).isEqualTo(노선_하행_종착역_ID),
            () -> assertThat(노선의_길이를_반환합니다(response)).isEqualTo(10L)
        );
    }

    private ExtractableResponse<Response> 노선에서_역을_삭제합니다(Long 삭제_역_ID, Long 노선_ID) {
        return RestAssuredUtil.delete(
            String.format("/lines/%d/sections?stationId={stationId}", 노선_ID),
            삭제_역_ID);
    }

    private List<Long> 노선_전체_역_목록(Long 노선_하행_종착역_ID, Long 노선_상행_종착역_ID, Long 추가_구간_역_ID) {
        return List.of(노선_하행_종착역_ID, 노선_상행_종착역_ID, 추가_구간_역_ID);
    }

    private long 노선의_길이를_반환합니다(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("distance");
    }

    private long 상행_종착역_ID를_반환합니다(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("upStationId");
    }

    private String 응답_메시지를_가져옵니다(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("message");
    }

    private AbstractIntegerAssert<?> BAD_REQUEST_응답코드가_반환됩니다(ExtractableResponse<Response> response) {
        return assertThat(응답상태코드를_반환합니다(response)).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private List<Long> 역ID_목록(ExtractableResponse<Response> lines) {
        return lines.jsonPath().getList("id", Long.class);
    }

    private ExtractableResponse<Response> 지하철_구간을_추가합니다(Long lineId,
        Map<String, Object> param) {
        return RestAssuredUtil
            .createWithBadRequest("/lines/" + lineId + "/sections", param);
    }

    private Map<String, Object> 구간_추가_요청정보를_생성합니다(Long sectionUpStationId, Long sectionDownStationId,
        Long sectionDistance) {
        return Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);
    }
}
