package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.CreatedLineResponse;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.StationSteps;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("구간을 추가한다.")
    @Test
    void createSection() {
        // given
        // 지하철 노선
        CreatedLineResponse createdLineResponse  =
                LineSteps.registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10);

        final Map newDownStation = StationSteps.createStationInputHelper("방배역");
        StationResponse newDownStationResponse = StationSteps.createStationHelper(newDownStation).as(StationResponse.class);

        // when
        Map<String, String> newSection = LineSteps.createSectionMapHelper(
                createdLineResponse.getDownStationId(),
                newDownStationResponse.getId(),
                5);
        ExtractableResponse< Response > newSectionResponse = LineSteps.registerSectionHelper(createdLineResponse.getId(), newSection);

        // then
        assertAppendNewSectionSuccess(newSectionResponse);
    }

    @DisplayName("구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        // 지하철 노선
        // 구간 추가
        CreatedLineResponse createdLineResponse  =
                LineSteps.registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10);

        final Map newDownStation = StationSteps.createStationInputHelper("방배역");
        StationResponse newDownStationResponse = StationSteps.createStationHelper(newDownStation).as(StationResponse.class);
        Map<String, String> newSection = LineSteps.createSectionMapHelper(
                createdLineResponse.getDownStationId(),
                newDownStationResponse.getId(),
                5);
        LineSteps.registerSectionHelper(createdLineResponse.getId(), newSection).as(SectionResponse.class);

        // when
        // 구간 삭제
        ExtractableResponse<Response> deleteSectionResponse = LineSteps.deleteStationHelper(createdLineResponse.getId(), newDownStationResponse.getId());

        // then
        assertDeleteSuccess(deleteSectionResponse);
    }

    @DisplayName("하나가 남은 구간은 제거가 안된다.")
    @Test
    void deleteLastOneSection() {
        // given
        // 지하철 노선
        // 구간 추가
        CreatedLineResponse createdLineResponse  =
                LineSteps.registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10);

        final Map newDownStation = StationSteps.createStationInputHelper("방배역");
        StationResponse newDownStationResponse = StationSteps.createStationHelper(newDownStation).as(StationResponse.class);
        Map<String, String> newSection = LineSteps.createSectionMapHelper(
                createdLineResponse.getDownStationId(),
                newDownStationResponse.getId(),
                5);
        SectionResponse newSectionResponse = LineSteps.registerSectionHelper(createdLineResponse.getId(), newSection).as(SectionResponse.class);

        // when
        // 구간 삭제
        ExtractableResponse<Response> deleteSectionResponse = LineSteps.deleteStationHelper(createdLineResponse.getId(), newSectionResponse.getId());

        // then
        assertBadRequestFail(deleteSectionResponse);
    }

    @DisplayName("등록하려는 구간의 상행역이 종점역이 아닌경우 등록되지 않는다.")
    @Test
    void createSectionWithNotMatchedDownStation() {
        // given
        // 지하철 노선
        CreatedLineResponse createdLineResponse  =
                LineSteps.registerLineWithStationsHelper("KangName Line", "green", "강남역","서초역", 10);

        final Map newDownStation = StationSteps.createStationInputHelper("방배역");
        StationResponse newDownStationResponse = StationSteps.createStationHelper(newDownStation).as(StationResponse.class);

        // when
        Map<String, String> newSection = LineSteps.createSectionMapHelper(
                createdLineResponse.getUpStationId(),
                newDownStationResponse.getId(),
                5);
        ExtractableResponse<Response> newSectionResponse = LineSteps.registerSectionHelper(createdLineResponse.getId(), newSection);

        // then
        assertBadRequestFail(newSectionResponse);
    }

    private void assertAppendNewSectionSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void assertDeleteSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertBadRequestFail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
