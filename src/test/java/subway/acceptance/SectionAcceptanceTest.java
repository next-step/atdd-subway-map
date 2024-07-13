package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.controller.dto.AddSectionRequest;
import subway.fixtures.LineFixture;
import subway.internal.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends BaseTestSetup {
    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 상행역이 노선의 하행종창역이 아니도록 구간을 추가하면
     * Then: 상행역이 잘못되었다는 오류가 발생한다.
     */
    @DisplayName("상행역을 잘못입력했다면 오류가 발생한다.")
    @Test
    void addSection_error_upStation_invalid() {
        // given
        Long cityHallId = StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation());
        Long yongsanId = StationApiResponseExtractor.Single.extractId(StationTestApi.createYongsanStation());
        Long guroId = StationApiResponseExtractor.Single.extractId(StationTestApi.createGuroStation());
        Long lineOneId = LineApiResponseExtractor.Single.extractId(
                LineTestApi.createLine(LineFixture.prepareLineOneCreateRequest(cityHallId, yongsanId))
        );

        // when
        ExtractableResponse<Response> response = LineTestApi.addSection(new AddSectionRequest(cityHallId, guroId, 10L), lineOneId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 하행역이 노선에 이미 포함된 역이라면
     * Then: 하행역이 잘못되었다는 오류가 발생한다.
     */
    @DisplayName("하행역을 잘못입력했다면 오류가 발생한다.")
    @Test
    void addSection_error_downStation_invalid() {
        // given
        Long cityHallId = StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation());
        Long yongsanId = StationApiResponseExtractor.Single.extractId(StationTestApi.createYongsanStation());
        Long lineOneId = LineApiResponseExtractor.Single.extractId(
                LineTestApi.createLine(LineFixture.prepareLineOneCreateRequest(cityHallId, yongsanId))
        );

        // when
        ExtractableResponse<Response> response = LineTestApi.addSection(new AddSectionRequest(yongsanId, cityHallId, 10L), lineOneId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 상행역이 노선의 하행종창역이 되도록 구간을 추가하면
     * Then: 해당 지하철 구간이 추가된다.
     */
    @DisplayName("새로운 구간을 추가한다.")
    @Test
    void addSection() {
        // given
        Long cityHallId = StationApiResponseExtractor.Single.extractId(StationTestApi.createCityHallStation());
        Long yongsanId = StationApiResponseExtractor.Single.extractId(StationTestApi.createYongsanStation());
        Long guroId = StationApiResponseExtractor.Single.extractId(StationTestApi.createGuroStation());
        Long lineOneId = LineApiResponseExtractor.Single.extractId(
                LineTestApi.createLine(LineFixture.prepareLineOneCreateRequest(cityHallId, yongsanId))
        );

        // when
        ExtractableResponse<Response> response = LineTestApi.addSection(new AddSectionRequest(yongsanId, guroId, 10L), lineOneId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = LineApiResponseExtractor.Single.extractUpDownStationNames(LineTestApi.showLine(lineOneId));
        assertThat(stationNames).containsAnyOf("구로역");
    }
}
