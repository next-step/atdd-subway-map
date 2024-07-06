package subway.acceptance;

import autoparams.AutoSource;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.HttpStatus;
import subway.controller.dto.CreateLineRequest;
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
    @ParameterizedTest
    @AutoSource
    void createLine(CreateLineRequest request) {
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
    @DisplayName("노션 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        Long seoulId = StationApiResponseExtractor.extractId(StationTestApi.createStation("서울역"));
        Long cityHallId = StationApiResponseExtractor.extractId(StationTestApi.createStation("시청역"));
        LineTestApi.createLine(LineFixture.lineOneCreateRequest(seoulId, cityHallId));

        Long yeoksamId = StationApiResponseExtractor.extractId(StationTestApi.createStation("역삼역"));
        Long jamsilId = StationApiResponseExtractor.extractId(StationTestApi.createStation("잠실역"));
        LineTestApi.createLine(LineFixture.lineTwoCreateRequest(yeoksamId, jamsilId));

        // when
        ExtractableResponse<Response> response = LineTestApi.showLines();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> lineNames = LineApiResponseExtractor.extractNames(response);
        assertThat(lineNames).containsExactly("1호선", "2호선");

        // then
        List<String> lineOneStationNames = LineApiResponseExtractor.extractUpDownStationNames(response, "1호선");
        assertThat(lineOneStationNames).containsExactly("서울역", "시청역");

        List<String> lineTwoStationNames = LineApiResponseExtractor.extractUpDownStationNames(response, "2호선");
        assertThat(lineTwoStationNames).containsExactly("역삼역", "잠실역");
    }
}
