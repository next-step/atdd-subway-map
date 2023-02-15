package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.acceptanceSetting.SectionAcceptanceTestSetting;
import subway.station.global.error.exception.ErrorCode;

public class SectionAcceptanceTest extends SectionAcceptanceTestSetting {

    /**
     * given 새로운 역을 생성하고
     * when 지하철 구간을 등록하면
     * then 지하철 구간을 조회 시 등록한 구간을 찾을 수 있다
     */
    @Test
    void 지하철_노선에_구간을_등록하는_기능을_구현한다() {
        //given
        Long fourthStationId = stationRestAssured.save("교대역");

        //when
        sectionRestAssured.save(firstLineId, 테스트용_지하철_구간_생성(thirdStationId, fourthStationId, DISTANCE_FIVE));

        //then
        ExtractableResponse<Response> line = lineRestAssured.findById(firstLineId);
        Assertions.assertThat(line.jsonPath().getList("stations.id", Long.class)).contains(secondStationId, thirdStationId);
    }

    /**
     * given 노선의 하행 종점역이 아닌 새로운 역을 생성하고
     * when 지하철 구간의 상행역으로 등록하면
     * then INVALID_VALUE_EXCEPTION_ERROR -
     * then MISMATCHED_BETWEEN_UP_STATION_OF_NEW_SECTION_AND_DOWN_STATION_OF_LINE 에러가 발생한다.
     */
    @Test
    void 예외_처리_새로운_구간의_상행역은_노선의_하행_종점역이여야_한다() {
        //given
        Long fourthStationId = stationRestAssured.save("교대역");
        Long fifthStationId = stationRestAssured.save("신반포역");

        //when
        ExtractableResponse<Response> saveResponse = sectionRestAssured.save(firstLineId, 테스트용_지하철_구간_생성(fourthStationId, fifthStationId, DISTANCE_FIVE));

        //then
        Assertions.assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(saveResponse.jsonPath().getList("errorMessages", String.class)).containsExactly(ErrorCode.MISMATCHED_DOWN_STATION_OF_LINE.getErrorMessage());
    }

    /**
     * given 노선에 포함된 새로운 역을 생성하고(이미 생성되어 있음)
     * when 지하철 구간의 하행역으로 등록하면
     * then INVALID_VALUE_EXCEPTION -
     * ALREADY_REGISTERED_IN_LINE 에러가 발생한다.
     */
    @Test
    void 예외_처리_새로운_구간의_하행역은_해당_노선에_등록되어있는_역일_수_없다() {
        //when
        ExtractableResponse<Response> saveResponse = sectionRestAssured.save(firstLineId, 테스트용_지하철_구간_생성(thirdStationId, firstStationId, DISTANCE_FIVE));

        //then
        Assertions.assertThat(saveResponse.statusCode()).isEqualTo(ErrorCode.ALREADY_REGISTERED_IN_LINE.getStatus());
        Assertions.assertThat(saveResponse.jsonPath().getList("errorMessages", String.class)).containsExactly(ErrorCode.ALREADY_REGISTERED_IN_LINE.getErrorMessage());
    }

    /**
     * given 이미 노선 및 구간에 포함되고 마지막 구간에 포함된 지하철 역의 id 값을 받아(이미 생성되어 있음)
     * when 지하철 노선에서 지하철 역의 id 값이 포함되어 있는 구간을 삭제하면
     * then 지하철 구간을 조회 시 삭제한 구간을 찾을 수 없다.
     */
    @Test
    void 지하철_노선에_구간을_삭제하는_기능을_구현한다() {
        //when
        sectionRestAssured.delete(firstLineId, thirdStationId);

        //then
        ExtractableResponse<Response> line = lineRestAssured.findById(firstLineId);
        Assertions.assertThat(line.jsonPath().getList("stations.id", Long.class)).doesNotContain(thirdStationId);
    }

    /**
     * given 마지막 구간이 아닌 지하철 역의 id 값을 받아(이미 생성되어 있음)
     * when 지하철 노선에서 지하철 역의 id 값이 포함되어 있는 구간을 삭제하면
     * then INVALID_VALUE_EXCEPTION -
     * SECTION_IS_NOT_END_OF_LINE 에러가 발생한다.
     */
    @Test
    void 예외_처리_지하철_노선에_등록된_역_하행_종점역_만_제거할_수_있다() {
        //when
        ExtractableResponse<Response> deleteResponse = sectionRestAssured.delete(firstLineId, firstStationId);

        //then
        Assertions.assertThat(deleteResponse.statusCode()).isEqualTo(ErrorCode.SECTION_IS_NOT_END_OF_LINE.getStatus());
        Assertions.assertThat(deleteResponse.jsonPath().getList("errorMessages", String.class)).containsExactly(ErrorCode.SECTION_IS_NOT_END_OF_LINE.getErrorMessage());
    }

    /**
     * given 지하철 노선의 구간이 한 개이고
     * when 남은 한 개의 구간을 삭제하면
     * then INVALID_VALUE_EXCEPTION -
     * LINE_HAS_ONLY_ONE_SECTION 에러가 발생한다.
     */
    @Test
    void 예외_처리_지하철_노선에_상행_종점역과_하행_종점역만_있는_경우_구간이_1개인_경우_역을_삭제할_수_없다() {
        //given
        sectionRestAssured.delete(firstLineId, thirdStationId);

        //when
        ExtractableResponse<Response> deleteResponse = sectionRestAssured.delete(firstLineId, secondStationId);

        //then
        Assertions.assertThat(deleteResponse.statusCode()).isEqualTo(ErrorCode.LINE_HAS_ONLY_ONE_SECTION.getStatus());
        Assertions.assertThat(deleteResponse.jsonPath().getList("errorMessages", String.class)).containsExactly(ErrorCode.LINE_HAS_ONLY_ONE_SECTION.getErrorMessage());
    }
}
