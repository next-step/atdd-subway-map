package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.AbstractCollectionAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선의 인수 테스트입니다.")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    RestAssuredUtil restAssuredUtil;

    @BeforeEach
    @DisplayName("RestAssured 에서 요청 보낼 포트를 설정하고, 4개의 지하철 역 정보를 생성합니다.")
    void setup() {
        포트번호를_설정합니다();
        데이터베이스를_초기화합니다();
        지하철_역을_생성합니다("강남역", "양재역", "신사역", "논현역");
    }

    private void 데이터베이스를_초기화합니다() {
        restAssuredUtil.cleanup();
    }

    private void 포트번호를_설정합니다() {
        restAssuredUtil.initializePort(port);
    }

    private void 지하철_역을_생성합니다(String... name) {
        Stream.of(name).forEach(StationAcceptanceTest::지하철_역_생성);
    }

    /**
     * given 상행종점역, 하행종점역, 라인명, 라인색상, 라인길이를 통해
     * when 지하철 노선을 생성하면
     * then 지하철 노선이 정상 생성되고(CREATED),
     * then 저장한 노선을 조회할 수 있습니다.
     * */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Long upStationId = 1L;
        Long downStationId = 2L;
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;

        // when
        ExtractableResponse<Response> saved = 지하철_노선_정보를_생성합니다(upStationId, downStationId, name, color, distance);

        // then
        지하철_노선이_정상_생성되었습니다(saved);

        // then
        저장한_지하철_노선을_조회할_수_있습니다(saved, name, color, upStationId, downStationId);
    }

    private void 저장한_지하철_노선을_조회할_수_있습니다(ExtractableResponse<Response> saved, String name, String color, Long... id) {
        ExtractableResponse<Response> found = 저장한_지하철_노선_ID_조회(saved);
        assertAll(
            () -> 응답_상태코드가_OK_입니다(found),
            () -> 응답_노선의_이름이_입력_이름과_동일합니다(name, found),
            () -> 응답_노선의_색상이_입력_색상과_동일합니다(color, found),
            () -> 응답_노선의_지하철_역_목록에_입력한_지하철_역이_포함됩니다(found, id)
        );
    }

    private ListAssert<Long> 응답_노선의_지하철_역_목록에_입력한_지하철_역이_포함됩니다(ExtractableResponse<Response> found, Long[] id) {
        return assertThat(found.jsonPath().getList("stations.id", Long.class)).contains(id);
    }

    private AbstractStringAssert<?> 응답_노선의_색상이_입력_색상과_동일합니다(String color, ExtractableResponse<Response> found) {
        return assertThat(found.jsonPath().getString("color")).isEqualTo(color);
    }

    private AbstractStringAssert<?> 응답_노선의_이름이_입력_이름과_동일합니다(String name, ExtractableResponse<Response> found) {
        return assertThat(found.jsonPath().getString("name")).isEqualTo(name);
    }

    private AbstractIntegerAssert<?> 응답_상태코드가_OK_입니다(ExtractableResponse<Response> found) {
        return assertThat(found.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 저장한_지하철_노선_ID_조회(ExtractableResponse<Response> saved) {
        return selectLine(saved.jsonPath().getLong("id"));
    }

    private AbstractIntegerAssert<?> 지하철_노선이_정상_생성되었습니다(ExtractableResponse<Response> saved) {
        return assertThat(saved.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 지하철_노선_정보를_생성합니다(Long upStationId, Long downStationId,
        String name, String color, Long distance) {
        return createLineResult(
            requestBodyOf(upStationId, downStationId, name, color, distance));
    }

    /** given 지하철 노선을 2개 생성하고
     * when 지하철 노선 목록을 조회하면
     * then 전체 지하철 노선 목록이 조회되고
     * then 생성한 지하철 노선 2개가 포함됩니다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void selectAllLines() {
        // given
        Long upStationId = 1L;
        Long downStationId = 2L;
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        입력_정보로_지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        Long upStationId2 = 3L;
        Long downStationId2 = 4L;
        String name2 = "2호선";
        String color2 = "bg-green-600";
        Long distance2 = 15L;
        입력_정보로_지하철_노선을_생성합니다(upStationId2, downStationId2, name2, color2, distance2);

        // when
        ExtractableResponse<Response> found = 지하철_노선_목록을_조회합니다();

        // then
        List<List<Integer>> list = 지하철_노선_목록에서_지하철_역_ID_조회합니다(found);
        Set<Long> stationIdSet = 지하철_역_ID_목록으로_변환합니다(list);

        생성한_지하철_노선들이_응답_지하철_노선_목록에_포함됩니다(found, stationIdSet, upStationId, downStationId, upStationId2, downStationId2, name, color, name2, color2);
    }

    private void 생성한_지하철_노선들이_응답_지하철_노선_목록에_포함됩니다(ExtractableResponse<Response> found, Set<Long> stationIdSet,
        Long upStationId, Long downStationId, Long upStationId2, Long downStationId2,
        String name, String color, String name2, String color2) {
        assertAll(
            () -> 응답목록의_지하철_노선_이름에_생성시_입력된_이름들이_포함됩니다(found, name, name2),
            () -> 응답목록의_지하철_노선_색상에_생성시_입력된_색상들이_포함됩니다(found, color, color2),
            () -> 응답목록의_지하철_노선_역_ID_목록에_생성시_입력된_지하철_역_ID_들이_포함됩니다(stationIdSet, upStationId, downStationId, upStationId2, downStationId2)
        );
    }

    private AbstractCollectionAssert<?, Collection<? extends Long>, Long, ObjectAssert<Long>> 응답목록의_지하철_노선_역_ID_목록에_생성시_입력된_지하철_역_ID_들이_포함됩니다(
        Set<Long> stationIdSet, Long upStationId, Long downStationId, Long upStationId2,
        Long downStationId2) {
        return assertThat(stationIdSet).containsExactly(upStationId, downStationId, upStationId2,
            downStationId2);
    }

    private ListAssert<String> 응답목록의_지하철_노선_색상에_생성시_입력된_색상들이_포함됩니다(ExtractableResponse<Response> found,
        String color, String color2) {
        return assertThat(found.jsonPath().getList("lines.color", String.class)).containsExactly(
            color, color2);
    }

    private ListAssert<String> 응답목록의_지하철_노선_이름에_생성시_입력된_이름들이_포함됩니다(ExtractableResponse<Response> found, String name,
        String name2) {
        return assertThat(found.jsonPath().getList("lines.name", String.class)).containsExactly(
            name, name2);
    }

    private List<List<Integer>> 지하철_노선_목록에서_지하철_역_ID_조회합니다(ExtractableResponse<Response> found) {
        return found.jsonPath().getList("lines.stations.id");
    }

    private Set<Long> 지하철_역_ID_목록으로_변환합니다(List<List<Integer>> list) {
        return list.stream()
            .flatMap(List::stream)
            .mapToLong(Long::valueOf)
            .boxed()
            .collect(Collectors.toSet());
    }

    private ExtractableResponse<Response> 지하철_노선_목록을_조회합니다() {
        return selectAll();
    }

    private ExtractableResponse<Response> 입력_정보로_지하철_노선을_생성합니다(Long upStationId, Long downStationId, String name, String color,
        Long distance) {
        return createLineResult(
            requestBodyOf(upStationId, downStationId, name, color, distance));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성한 지하철 노선을 조회하면
     * then 생성한 지하철 노선 정보를 응답받습니다.
     **/
    @DisplayName("지하철 노선 조회")
    @Test
    void selectALine() {
        // given
        Long upStationId = 1L;
        Long downStationId = 2L;
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;

        ExtractableResponse<Response> saved = 입력_정보로_지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        // when
        ExtractableResponse<Response> found = 저장한_지하철_노선_ID_조회(saved);

        // then
        생성한_지하철_노선_정보를_응답받습니다(upStationId, downStationId, name, color, saved, found);
    }

    private void 생성한_지하철_노선_정보를_응답받습니다(Long upStationId, Long downStationId, String name, String color,
        ExtractableResponse<Response> saved, ExtractableResponse<Response> found) {
        assertAll(
            () -> 응답_노선의_ID_가_저장된_노선의_ID_와_동일합니다(saved, found),
            () -> 응답_노선의_이름이_입력_이름과_동일합니다(name, found),
            () -> 응답_노선의_색상이_입력_색상과_동일합니다(color, found),
            () -> 응답_노선의_지하철_역_ID_에_입력_지하철_역_ID_가_포함됩니다(upStationId, downStationId, found)
        );
    }

    private ListAssert<Long> 응답_노선의_지하철_역_ID_에_입력_지하철_역_ID_가_포함됩니다(Long upStationId, Long downStationId,
        ExtractableResponse<Response> found) {
        return assertThat(found.jsonPath().getList("stations.id", Long.class)).contains(
            upStationId, downStationId);
    }

    private AbstractLongAssert<?> 응답_노선의_ID_가_저장된_노선의_ID_와_동일합니다(ExtractableResponse<Response> saved,
        ExtractableResponse<Response> found) {
        return assertThat(found.jsonPath().getLong("id")).isEqualTo(saved.jsonPath().getLong("id"));
    }

    /**
     * given 지하철 노선을 생성하고
     * and 노선 수정 정보를 준비해
     * when 생성한 노선을 수정 정보로 수정 하면
     * then 정상 수정되고
     * then 생성 노선을 조회했을때 정보가 수정 정보와 일치합니다.
     **/
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        Long upStationId = 1L;
        Long downStationId = 2L;
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        ExtractableResponse<Response> saved = 입력_정보로_지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        // and
        Long newUpStationId = 3L;
        Long newDownStationId = 4L;
        String newName = "2호선";
        String newColor = "bg-green-600";
        Long newDistance = 20L;
        Map<String, Object> putRequestBody = 노선_정보를_준비합니다(newUpStationId, newDownStationId,
            newName, newColor, newDistance);

        // when
        ExtractableResponse<Response> updated = 입력_정보로_지하철_노선을_수정합니다(saved, putRequestBody);

        // then
        응답_상태코드가_OK_입니다(updated);

        // then
        ExtractableResponse<Response> found = 저장한_지하철_노선_ID_조회(saved);

        노선_조회_응답_정보가_수정_정보와_일치합니다(saved, newUpStationId, newDownStationId, newName, newColor, found);
    }

    private Map<String, Object> 노선_정보를_준비합니다(Long newUpStationId, Long newDownStationId,
        String newName, String newColor, Long newDistance) {
        return requestBodyOf(
            newUpStationId, newDownStationId, newName, newColor, newDistance);
    }

    private ExtractableResponse<Response> 입력_정보로_지하철_노선을_수정합니다(ExtractableResponse<Response> saved,
        Map<String, Object> putRequestBody) {
        return updateLine(
            saved.jsonPath().getLong("id"), putRequestBody);
    }

    private void 노선_조회_응답_정보가_수정_정보와_일치합니다(ExtractableResponse<Response> saved, Long newUpStationId,
        Long newDownStationId, String newName, String newColor,
        ExtractableResponse<Response> found) {
        assertAll(
            () -> 응답_상태코드가_OK_입니다(found),
            () -> 응답_노선의_ID_가_저장된_노선의_ID_와_동일합니다(saved, found),
            () -> 응답_노선의_이름이_입력_이름과_동일합니다(newName, found),
            () -> 응답_노선의_색상이_입력_색상과_동일합니다(newColor, found),
            () -> 응답_노선의_지하철_역_ID_에_입력_지하철_역_ID_가_포함됩니다(newUpStationId, newDownStationId, found)
        );
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성한 노선을 삭제하면
     * then 해당 지하철 노선을 조회했을때 결과가 존재하지 않습니다.
     **/
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        Long upStationId = 1L;
        Long downStationId = 2L;
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        ExtractableResponse<Response> saved = 입력_정보로_지하철_노선을_생성합니다(upStationId, downStationId, name, color, distance);

        // when
        ExtractableResponse<Response> deleted = 생성한_노선을_삭제합니다(saved);

        // then
        생성한_지하철_노선을_조회하면_결과가_존재하지_않습니다(deleted);
    }

    private void 생성한_지하철_노선을_조회하면_결과가_존재하지_않습니다(ExtractableResponse<Response> deleted) {
        assertAll(
            () -> 응답_결과의_BODY_가_빈_문자열입니다(deleted),
            () -> 응답_결과의_상태코드가_NO_CONTENT_입니다(deleted)
        );
    }

    private AbstractIntegerAssert<?> 응답_결과의_상태코드가_NO_CONTENT_입니다(ExtractableResponse<Response> deleted) {
        return assertThat(deleted.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private AbstractStringAssert<?> 응답_결과의_BODY_가_빈_문자열입니다(ExtractableResponse<Response> deleted) {
        return assertThat(deleted.body().asString()).isBlank();
    }

    private ExtractableResponse<Response> 생성한_노선을_삭제합니다(ExtractableResponse<Response> saved) {
        return deleteLineResult(saved.jsonPath().getLong("id"));
    }

    private Map<String, Object> requestBodyOf(Long upStationId, Long downStationId, String name,
        String color, Long distance) {
        return Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
    }

    private ExtractableResponse<Response> createLineResult(Map<String, Object> requestBody) {
        return RestAssuredUtil.createWithCreated("/lines", requestBody);
    }

    private ExtractableResponse<Response> selectAll() {
        return RestAssuredUtil.findAllWithOk("/lines");
    }

    private ExtractableResponse<Response> selectLine(Long id) {
        return RestAssuredUtil.findByIdWithOk("/lines/{id}", id);
    }

    private ExtractableResponse<Response> updateLine(Long id, Map<String, Object> putRequestBody) {
        return RestAssuredUtil.updateWithOk("/lines/{id}", id, putRequestBody);
    }

    private ExtractableResponse<Response> deleteLineResult(Long id) {
        return RestAssuredUtil.deleteWithNoContent("/lines/{id}", id);
    }

}
