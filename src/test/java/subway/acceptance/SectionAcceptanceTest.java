package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.acceptance.LineAcceptanceTest.*;
import static subway.acceptance.StationAcceptanceTest.신규_역;
import static subway.acceptance.StationAcceptanceTest.역_목록_조회;

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
        restAssuredUtil.테스트_컨텍스트를_초기화합니다(port);

    }

    public static ExtractableResponse<Response> 노선_구간_추가(Long 노선_ID, Long 노선_하행_종착역_ID, Long 추가_구간_역_ID, Long 추가_구간_길이) {
        return RestAssuredUtil.createWithCreated("/lines/" + 노선_ID + "/sections", Map.of(
            "upStationId", 노선_하행_종착역_ID,
            "downStationId", 추가_구간_역_ID,
            "distance", 추가_구간_길이
        ));
    }

    /**
     * given 등록하려는 노선ID와 추가하려는 구간 요청 정보가 있을때
     * and 노선ID에 해당하는 노선이 존재하지 않으면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록_노선 미존재 오류")
    @Test
    void createSectionWithLineNotFoundException() {
        //given
        Long 대상_노선 = 미존재노선();
        Long 구간_상행역 = 강남역();
        Long 구간_하행역 = 신논현역();
        Long 구간_길이 = 5L;
        Map<String, Object> 구간_추가_요청 = 구간_추가_요청정보(구간_상행역, 구간_하행역, 구간_길이);

        // and
        assertThat(지하철_노선_목록()).doesNotContain(대상_노선);

        //when
        ExtractableResponse<Response> 구간_추가_응답 = 지하철_구간을_추가합니다(대상_노선, 구간_추가_요청);

        // then
        노선이_존재하지_않습니다(대상_노선, 구간_추가_응답);
    }

    private static long 미존재노선() {
        return 1L;
    }

    private long 신논현역() {
        return 신규_역("신논현역").jsonPath().getLong("id");
    }

    private long 강남역() {
        return 신규_역("강남역").jsonPath().getLong("id");
    }

    private void 노선이_존재하지_않습니다(Long 대상_노선, ExtractableResponse<Response> 구간_추가_응답) {
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(구간_추가_응답),
            () -> assertThat(응답_메시지를_가져옵니다(구간_추가_응답)).isEqualTo("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + 대상_노선)
        );
    }

    private List<Long> 지하철_노선_목록() {
        return ID_목록_변환(지하철_노선_목록을_조회합니다());
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
        Long 구간_상행역 = 미존재역_1();
        Long 구간_하행역 = 미존재역_2();
        Long 구간_길이 = 5L;
        Map<String, Object> 구간_추가_요청 = 구간_추가_요청정보(구간_상행역, 구간_하행역,
                구간_길이);

        // and
        Long 노선_하행종착역 = 강남역();
        Long 노선_상행종착역 = 신논현역();
        Long 노선_길이 = 10L;
        String 노선_이름 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행종착역, 노선_하행종착역, 노선_이름, 노선_색상, 노선_길이);

        // and
        assertThat(지하철_노선_목록()).doesNotContain(구간_하행역);

        //when
        ExtractableResponse<Response> 응답 = 지하철_구간을_추가합니다(대상_노선(신분당선), 구간_추가_요청);

        // then
        요청_역이_존재하지_않습니다(구간_하행역, 응답);
    }

    private static long 미존재역_2() {
        return 4L;
    }

    private static long 미존재역_1() {
        return 3L;
    }

    private static long 대상_노선(ExtractableResponse<Response> 신분당선) {
        return 신분당선.jsonPath().getLong("id");
    }

    private void 요청_역이_존재하지_않습니다(Long 구간_하행역, ExtractableResponse<Response> 응답) {
        역이_존재하지_않습니다(구간_하행역, 응답);
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
        Long 구간_하행역 = 판교역();
        Long 구간_상행역 = 미존재역_2();
        Long 구간_길이 = 5L;
        Map<String, Object> 구간_추가_요청 = 구간_추가_요청정보(구간_상행역, 구간_하행역, 구간_길이);

        // and
        Long 노선_하행종착역 = 강남역();
        Long 노선_상행종착역 = 신논현역();
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행종착역, 노선_하행종착역, 노선명, 노선_색상, 노선_길이);

        // and
        assertThat(지하철_역_목록()).contains(구간_하행역);

        // and
        assertThat(지하철_역_목록()).doesNotContain(구간_상행역);

        //when
        ExtractableResponse<Response> 응답 = 지하철_구간을_추가합니다(대상_노선(신분당선), 구간_추가_요청);

        // then
        요청_역이_존재하지_않습니다(구간_상행역, 응답);
    }

    private Long 판교역() {
        return 신규_역("판교역").jsonPath().getLong("id");
    }

    private List<Long> 지하철_역_목록() {
        return ID_변환(역_목록_조회());
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
        Long 구간_상행역 = 판교역();
        Long 구간_하행역 = 양재역();
        Long 구간_길이 = 5L;
        Map<String, Object> 구간_추가_요청 = 구간_추가_요청정보(구간_상행역, 구간_하행역, 구간_길이);

        // and
        Long 노선_하행종착역 = 강남역();
        Long 노선_상행종착역 = 신논현역();
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행종착역, 노선_하행종착역, 노선명, 노선_색상, 노선_길이);

        // and
        assertThat(지하철_역_목록()).contains(구간_하행역);

        // and
        assertThat(지하철_역_목록()).contains(구간_상행역);

        // and
        assertThat(구간_상행역).isNotEqualTo(노선_하행종착역);

        //when
        ExtractableResponse<Response> 응답 = 지하철_구간을_추가합니다(대상_노선(신분당선), 구간_추가_요청);

        // then
        노선의_하행종착역에_이어지는_구간이_아닙니다(구간_상행역, 노선_하행종착역, 응답);
    }

    private long 양재역() {
        return 신규_역("양재역").jsonPath().getLong("id");
    }

    private void 노선의_하행종착역에_이어지는_구간이_아닙니다(Long 구간_상행역, Long 노선_하행종착역, ExtractableResponse<Response> 응답) {
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(응답),
            () -> assertThat(응답_메시지를_가져옵니다(응답))
                .isEqualTo("새로운 구간은 노선의 하행종착역을 상행역으로 설정해야 합니다: " + 노선_하행종착역 + " <> " + 구간_상행역)
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
        Long 판교역 = 판교역();
        Long 양재역 = 양재역();

        Long 구간_상행역 = 판교역;
        Long 구간_하행역 = 양재역;
        Long 구간_길이 = 5L;
        Map<String, Object> 구간_추가_요청 = 구간_추가_요청정보(구간_상행역, 구간_하행역, 구간_길이);

        // and
        Long 노선_하행종착역 = 판교역;
        Long 노선_상행종착역 = 양재역;
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        신규_노선(노선_상행종착역, 노선_하행종착역, 노선명, 노선_색상, 노선_길이);

        // and
        assertThat(지하철_역_목록()).contains(구간_하행역);

        // and
        assertThat(지하철_역_목록()).contains(구간_상행역);

        // and
        assertThat(구간_상행역).isEqualTo(노선_하행종착역);

        // and
        ExtractableResponse<Response> 신분당선 = 지하철_노선을_ID로_조회합니다(1L);
        assertThat(노선_역_목록(신분당선)).contains(구간_하행역);

        //when
        ExtractableResponse<Response> 응답 = 지하철_구간을_추가합니다(대상_노선(신분당선), 구간_추가_요청);

        // then
        구간_하행역이_기존_노선에_포함되어_있습니다(구간_하행역, 응답);
    }

    private void 구간_하행역이_기존_노선에_포함되어_있습니다(Long 구간_하행역, ExtractableResponse<Response> 응답) {
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(응답),
            () -> assertThat(응답_메시지를_가져옵니다(응답)).isEqualTo("신규 구간의 하행역이 기존 노선에 포함되어 있습니다: " + 구간_하행역)
        );
    }

    private List<Long> 노선_역_목록(ExtractableResponse<Response> line) {
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
        Long 강남역 = 강남역();
        Long 구간_상행역_ID = 강남역;
        Long 구간_하행역_ID = 양재역();
        Long 구간_길이 = 5L;
        Map<String, Object> 구간_추가_요청 = 구간_추가_요청정보(구간_상행역_ID, 구간_하행역_ID, 구간_길이);

        // and
        Long 노선_하행종착역_ID = 강남역;
        Long 노선_상행종착역_ID = 신논현역();
        Long 노선_최초길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행종착역_ID, 노선_하행종착역_ID, 노선명, 노선_색상, 노선_최초길이);

        // and
        신규_역("판교역");
        assertThat(지하철_역_목록()).contains(구간_하행역_ID);

        // and
        assertThat(지하철_역_목록()).contains(구간_상행역_ID);

        // and
        assertThat(구간_상행역_ID).isEqualTo(노선_하행종착역_ID);

        // and
        assertThat(노선_역_목록(신분당선)).doesNotContain(구간_하행역_ID);

        //when
        ExtractableResponse<Response> 응답 = 지하철_노선에_구간을_추가합니다(구간_추가_요청, 신분당선);

        // then
        구간이_정상_생성되었습니다(구간_하행역_ID, 노선_상행종착역_ID, 응답);
    }

    private void 구간이_정상_생성되었습니다(Long 구간_하행역_ID, Long 노선_상행종착역_ID, ExtractableResponse<Response> 응답) {
        assertAll(
            () -> assertThat(응답상태코드를_반환합니다(응답)).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(하행_종착역_ID를_반환합니다(응답)).isEqualTo(구간_하행역_ID),
            () -> assertThat(상행_종착역_ID를_반환합니다(응답)).isEqualTo(노선_상행종착역_ID),
            () -> assertThat(노선의_길이를_반환합니다(응답)).isEqualTo(15L)
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
        Long 제거_역 = 미존재역_1();
        Long 제거_노선 = 미존재노선();

        // and
        assertThat(지하철_노선_목록()).doesNotContain(제거_노선);

        //when
        ExtractableResponse<Response> 응답 = 지하철_노선에서_역을_제거합니다(제거_역, 제거_노선);

        // then
        노선이_존재하지_않습니다(제거_노선, 응답);
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
        Long 노선_하행_종착역 = 강남역();
        Long 노선_상행_종착역 = 신논현역();
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행_종착역, 노선_하행_종착역, 노선명, 노선_색상, 노선_길이);

        Long 제거_역 = 미존재역_1();
        Long 대상_노선 = 대상(신분당선);

        // and
        assertThat(지하철_노선_목록()).contains(대상_노선);

        // and
        assertThat(지하철_역_목록()).doesNotContain(제거_역);

        //when
        ExtractableResponse<Response> 응답 = 지하철_노선에서_역을_제거합니다(제거_역, 대상_노선);

        // then
        역이_존재하지_않습니다(제거_역, 응답);
    }

    private static long 대상(ExtractableResponse<Response> 신분당선) {
        return 신분당선.jsonPath().getLong("id");
    }

    private void 역이_존재하지_않습니다(Long 제거_역, ExtractableResponse<Response> response) {
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(response),
            () -> assertThat(응답_메시지를_가져옵니다(response)).isEqualTo("입력된 ID에 해당하는 역이 존재하지 않습니다: " + 제거_역)
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
        Long 노선_하행_종착역 = 강남역();
        Long 노선_상행_종착역 = 신논현역();
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행_종착역, 노선_하행_종착역, 노선명, 노선_색상, 노선_길이);

        Long 삭제_역 = 양재역();
        Long 대상_노선 = 대상(신분당선);

        // and
        assertThat(지하철_노선_목록()).contains(대상_노선);

        // and
        assertThat(지하철_역_목록()).contains(삭제_역);

        // and
        assertThat(노선_역_목록(신분당선)).doesNotContain(삭제_역);

        //when
        ExtractableResponse<Response> 응답 = 지하철_노선에서_역을_제거합니다(삭제_역, 대상_노선);

        // then
        노선에_포함되지_않는_역입니다(삭제_역, 응답);
    }

    private void 노선에_포함되지_않는_역입니다(Long 삭제_역, ExtractableResponse<Response> 응답) {
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(응답),
            () -> assertThat(응답_메시지를_가져옵니다(응답)).isEqualTo("입력된 ID에 해당하는 역이 노선에 포함되지 않습니다: " + 삭제_역)
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
        Long 신논현역 = 신논현역();
        Long 노선_하행_종착역 = 강남역();
        Long 노선_상행_종착역 = 신논현역;
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행_종착역, 노선_하행_종착역, 노선명, 노선_색상, 노선_길이);

        Long 삭제_역 = 신논현역;
        Long 대상_노선 = 대상(신분당선);

        // and
        assertThat(지하철_노선_목록()).contains(대상_노선);

        // and
        assertThat(지하철_역_목록()).contains(삭제_역);

        // and
        assertThat(노선_역_목록(신분당선)).contains(삭제_역);

        // and
        assertThat(삭제_역).isNotEqualTo(노선_하행_종착역);

        //when
        ExtractableResponse<Response> 응답 = 지하철_노선에서_역을_제거합니다(삭제_역, 대상_노선);

        // then
        노선의_하행종착역만_삭제_가능합니다(삭제_역, 노선_하행_종착역, 응답);
    }

    private void 노선의_하행종착역만_삭제_가능합니다(Long 삭제_역, Long 노선_하행_종착역, ExtractableResponse<Response> 응답) {
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(응답),
            () -> assertThat(응답_메시지를_가져옵니다(응답)).isEqualTo("노선의 하행종착역만 삭제 가능합니다: " + 노선_하행_종착역 + " <> " + 삭제_역)
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
        Long 강남역 = 강남역();

        Long 노선_하행_종착역 = 강남역;
        Long 노선_상행_종착역 = 양재역();
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행_종착역, 노선_하행_종착역, 노선명, 노선_색상, 노선_길이);

        Long 삭제_역 = 강남역;
        Long 대상_노선 = 대상(신분당선);

        // and
        assertThat(지하철_노선_목록()).contains(대상_노선);

        // and
        assertThat(지하철_역_목록()).contains(삭제_역);

        // and
        assertThat(노선_역_목록(신분당선)).contains(삭제_역);

        // and
        assertThat(삭제_역).isEqualTo(노선_하행_종착역);

        // and
        Set<Long> stations = new HashSet<>(Set.of(노선_상행_종착역, 노선_하행_종착역));
        stations.remove(삭제_역);
        assertThat(stations).hasSize(1);

        //when
        ExtractableResponse<Response> 응답 = 지하철_노선에서_역을_제거합니다(삭제_역, 대상_노선);

        // then
        단일_구간_노선은_삭제할_수_없습니다(응답);
    }

    private void 단일_구간_노선은_삭제할_수_없습니다(ExtractableResponse<Response> 응답) {
        assertAll(
            () -> BAD_REQUEST_응답코드가_반환됩니다(응답),
            () -> assertThat(응답_메시지를_가져옵니다(응답)).isEqualTo("단일 구간 노선은 구간 제거가 불가합니다.")
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
        Long 양재역 = 양재역();
        Long 노선_하행_종착역 = 강남역();
        Long 노선_상행_종착역 = 신논현역();
        Long 노선_길이 = 10L;
        String 노선명 = "신분당선";
        String 노선_색상 = "bg-red-600";
        ExtractableResponse<Response> 신분당선 = 신규_노선(노선_상행_종착역, 노선_하행_종착역, 노선명, 노선_색상, 노선_길이);

        Long 삭제_역 = 양재역;
        Long 대상_노선 = 대상(신분당선);

        Long 추가_구간_역 = 양재역;
        Long 추가_구간_길이 = 30L;
        노선_구간_추가(대상_노선, 노선_하행_종착역, 추가_구간_역, 추가_구간_길이);

        // and
        assertThat(지하철_노선_목록()).contains(대상_노선);

        // and
        assertThat(지하철_역_목록()).contains(삭제_역);

        // and
        assertThat(List.of(노선_역_목록(신분당선), 추가_구간_역)).contains(삭제_역);

        // and
        assertThat(삭제_역).isEqualTo(추가_구간_역);

        // and
        Set<Long> 노선_역_ID_목록 = new HashSet<>(Set.of(노선_상행_종착역, 노선_하행_종착역, 추가_구간_역));
        노선_역_ID_목록.remove(삭제_역);
        assertThat(노선_역_ID_목록).hasSizeGreaterThan(1);

        //when
        ExtractableResponse<Response> 응답 = 노선에서_역을_삭제합니다(삭제_역, 대상_노선);

        // then
        구간이_정상_삭제되었습니다(노선_하행_종착역, 노선_상행_종착역, 응답);
    }

    private void 구간이_정상_삭제되었습니다(Long 노선_하행_종착역, Long 노선_상행_종착역, ExtractableResponse<Response> 응답) {
        assertAll(
            () -> assertThat(응답상태코드를_반환합니다(응답)).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(상행_종착역_ID를_반환합니다(응답)).isEqualTo(노선_상행_종착역),
            () -> assertThat(하행_종착역_ID를_반환합니다(응답)).isEqualTo(노선_하행_종착역),
            () -> assertThat(노선의_길이를_반환합니다(응답)).isEqualTo(10L)
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

    private List<Long> ID_변환(ExtractableResponse<Response> lines) {
        return lines.jsonPath().getList("id", Long.class);
    }

    private List<Long> ID_목록_변환(ExtractableResponse<Response> lines) {
        return lines.jsonPath().getList("lines.id", Long.class);
    }

    private ExtractableResponse<Response> 지하철_구간을_추가합니다(Long lineId,
        Map<String, Object> param) {
        return RestAssuredUtil
            .createWithBadRequest("/lines/" + lineId + "/sections", param);
    }

    private Map<String, Object> 구간_추가_요청정보(Long sectionUpStationId, Long sectionDownStationId,
                                           Long sectionDistance) {
        return Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);
    }
}
