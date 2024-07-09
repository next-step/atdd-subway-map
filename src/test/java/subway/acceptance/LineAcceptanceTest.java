package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.controller.dto.CreateLineRequest;
import subway.controller.dto.UpdateLineRequest;
import subway.fixtures.LineFixture;
import subway.internal.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends BaseTestSetup {
    /**
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        // given
        CreateLineRequest request = LineFixture.prepareLineOneCreateRequest(
                StationApiResponseExtractor.Single.extractId(StationTestApi.createSeoulStation()),
                StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation())
        );

        // when
        ExtractableResponse<Response> response = LineTestApi.createLine(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = LineApiResponseExtractor.extractNames(LineTestApi.showLines());
        assertThat(lineNames).containsAnyOf(request.getName());
    }

    /**
     * Given: 2개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 2개의 지하철 노선 목록이 반환된다.
     */
    @DisplayName("노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        LineTestApi.createLine(LineFixture.prepareLineOneCreateRequest(
                StationApiResponseExtractor.Single.extractId(StationTestApi.createSeoulStation()),
                StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation())
        ));

        LineTestApi.createLine(LineFixture.prepareLineTwoCreateRequest(
                StationApiResponseExtractor.Single.extractId(StationTestApi.createYeoksamStation()),
                StationApiResponseExtractor.Single.extractId(StationTestApi.createJamsilStation())
        ));

        // when
        ExtractableResponse<Response> response = LineTestApi.showLines();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> lineNames = LineApiResponseExtractor.extractNames(response);
        assertThat(lineNames).containsExactly("1호선", "2호선");

        List<String> lineOneStationNames = LineApiResponseExtractor.extractUpDownStationNames(response, "1호선");
        assertThat(lineOneStationNames).containsExactly("서울역", "시청역");

        List<String> lineTwoStationNames = LineApiResponseExtractor.extractUpDownStationNames(response, "2호선");
        assertThat(lineTwoStationNames).containsExactly("역삼역", "잠실역");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("노선을 조회한다.")
    @Test
    void showLine() {
        // given
        ExtractableResponse<Response> createdLine = LineTestApi.createLine(LineFixture.prepareLineOneCreateRequest(
                StationApiResponseExtractor.Single.extractId(StationTestApi.createSeoulStation()),
                StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation())
        ));
        Long id = LineApiResponseExtractor.Single.extractId(createdLine);

        // when
        ExtractableResponse<Response> response = LineTestApi.showLine(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        assertThat(LineApiResponseExtractor.Single.extractName(response)).isEqualTo("1호선");
        assertThat(LineApiResponseExtractor.Single.extractUpDownStationNames(response)).containsExactly("서울역", "시청역");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdLine = LineTestApi.createLine(LineFixture.prepareLineOneCreateRequest(
                StationApiResponseExtractor.Single.extractId(StationTestApi.createSeoulStation()),
                StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation())
        ));
        Long id = LineApiResponseExtractor.Single.extractId(createdLine);
        UpdateLineRequest request = new UpdateLineRequest(
                "2호선",
                "#00A84D",
                StationApiResponseExtractor.Single.extractId(StationTestApi.createYeoksamStation()),
                StationApiResponseExtractor.Single.extractId(StationTestApi.createJamsilStation()),
                10L
        );

        // when
        ExtractableResponse<Response> response = LineTestApi.updateLine(id, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> updatedLine = LineTestApi.showLine(id);
        assertThat(LineApiResponseExtractor.Single.extractName(updatedLine)).isEqualTo("2호선");
        assertThat(LineApiResponseExtractor.Single.extractColor(updatedLine)).isEqualTo("#00A84D");
        assertThat(LineApiResponseExtractor.Single.extractUpDownStationNames(response)).containsExactly("역삼역", "잠실역");
    }

    /**
     * 지하철 노선 삭제
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdLine = LineTestApi.createLine(LineFixture.prepareLineOneCreateRequest(
                StationApiResponseExtractor.Single.extractId(StationTestApi.createSeoulStation()),
                StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation())
        ));
        Long id = LineApiResponseExtractor.Single.extractId(createdLine);

        // when
        ExtractableResponse<Response> response = LineTestApi.deleteLine(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> lineNames = LineApiResponseExtractor.extractNames(LineTestApi.showLines());
        assertThat(lineNames).doesNotContain("1호선");
    }
}
