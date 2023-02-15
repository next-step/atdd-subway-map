package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.acceptanceSetting.LineAcceptanceTestSetting;
import subway.dto.LineRequestTestDto;
import subway.station.domain.line.Line;

import java.util.List;

public class LineAcceptanceTest extends LineAcceptanceTestSetting {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다 => 지하철 노선 목록의 이름 중에 1호선이 있다.
     */
    @Test
    void 지하철_노선_생성_후_목록_조회시_찾을_수_있다() {
        //when
        LineRequestTestDto shinBunDangLine = 테스트용_데이터_생성(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        lineRestAssured.save(shinBunDangLine);

        //then
        ExtractableResponse<Response> findAllResponse = lineRestAssured.findAll();

        Assertions.assertThat(findAllResponse.jsonPath().getList(PATH_NAME)).contains(shinBunDangLine.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다. => 지하철 노선 목록 조회시 응답받은 List<Response>의 길이가 2이다.
     */
    @Test
    void 두_개의_지하철_노선_생성_후_목록_조회시_응답_받은_Response의_길이는_2이다() {
        //given
        LineRequestTestDto shinBunDangLine = 테스트용_데이터_생성(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        lineRestAssured.save(shinBunDangLine);
        LineRequestTestDto bunDangLine = 테스트용_데이터_생성(BUNDANG_LINE, GREEN, firstStationId, thirdStationId, DISTANCE_FIFTEEN);
        lineRestAssured.save(bunDangLine);

        //when
        ExtractableResponse<Response> findAllResponse = lineRestAssured.findAll();

        //then
        List<Line> lines = findAllResponse.jsonPath().get(PATH_LINE);

        Assertions.assertThat(lines.size()).isEqualTo(LENGTH_TWO);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_생성_후_생성된_노선으로_조회시_노선의_정보를_알_수_있다() {
        //given
        LineRequestTestDto shinBunDangLine = new LineRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        Long id = lineRestAssured.save(shinBunDangLine);

        //when
        ExtractableResponse<Response> findByResponse = lineRestAssured.findById(id);

        //then
        String lineName = findByResponse.jsonPath().get(PATH_NAME);
        String lineColor = findByResponse.jsonPath().get(PATH_COLOR);

        Assertions.assertThat(lineName).isEqualTo(shinBunDangLine.getName());
        Assertions.assertThat(lineColor).isEqualTo(shinBunDangLine.getColor());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선을_생성하고_수정하면_노선_정보는_수정된다() {
        //given
        LineRequestTestDto shinBunDangLine = new LineRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        Long id = lineRestAssured.save(shinBunDangLine);

        //when
        ExtractableResponse<Response> updateResponse = lineRestAssured.update(id, ANOTHER_BUNDANG_LINE, YELLOW);

        //then
        String updatedName = updateResponse.body().jsonPath().get(PATH_NAME);
        String updatedColor = updateResponse.body().jsonPath().get(PATH_COLOR);

        Assertions.assertThat(updatedName).isEqualTo(ANOTHER_BUNDANG_LINE);
        Assertions.assertThat(updatedColor).isEqualTo(YELLOW);

        lineRestAssured.findAll();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선을_생성하고_삭제하면_노선_정보는_삭제_된다() {
        //given
        LineRequestTestDto shinBunDangLine = new LineRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        Long id = lineRestAssured.save(shinBunDangLine);

        //when
        lineRestAssured.delete(id);

        // then
        Assertions.assertThat(lineRestAssured.findAll().jsonPath().getList(PATH_NAME)).doesNotContain(shinBunDangLine.getName());
    }

    private LineRequestTestDto 테스트용_데이터_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        return LineRequestTestDto.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
