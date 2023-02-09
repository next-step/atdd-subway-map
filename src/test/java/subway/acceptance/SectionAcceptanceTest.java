package subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptanceSetting.SectionAcceptanceTestSetting;
import subway.dto.SectionRequestTestDto;

public class SectionAcceptanceTest extends SectionAcceptanceTestSetting {

    /**
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @Test
    void 지하철_노선에_구간을_등록하는_기능을_구현() {
        saveSuccess();
        saveFailCaseOne();
        saveFailCaseTwo();
    }

    /**
     * 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
     * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
     * 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @Test
    void 지하철_노선에_구간을_제거하는_기능_구현() {
        deleteFailCaseTwo();
        saveSuccess();
        deleteSuccess();
        lineRestAssured.findAll();
    }

    @DisplayName("지하철 구간 삭제 성공 케이스")
    private void deleteSuccess() {
        sectionRestAssured.deleteSuccess(firstLineId, secondStationId);
    }

    @DisplayName("실패 케이스1 - 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.")
    private void deleteFailCaseOne() {
        sectionRestAssured.deleteFailCaseOne(firstLineId, secondStationId);
    }

    @DisplayName("실패 케이스2 - 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다")
    private void deleteFailCaseTwo() {
        sectionRestAssured.deleteFailCaseTwo(firstLineId, secondStationId);
    }

    @DisplayName("지하철 구간 생성 성공 케이스")
    private void saveSuccess() {
        SectionRequestTestDto successNewTestSectionDto = 테스트용_지하철_구간_생성(firstStationId, secondStationId, DISTANCE_THREE);
        sectionRestAssured.saveSuccess(firstLineId, successNewTestSectionDto);
    }

    @DisplayName("실패 케이스1 - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다")
    private void saveFailCaseOne() {
        SectionRequestTestDto failNewTestSectionDto = 테스트용_지하철_구간_생성(thirdStationId, fourthStationId, DISTANCE_FIVE);
        sectionRestAssured.saveFailCaseOne(firstLineId, failNewTestSectionDto);
    }

    @DisplayName("실패 케이스2 - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다")
    private void saveFailCaseTwo() {
        SectionRequestTestDto failNewTestSectionDto = 테스트용_지하철_구간_생성(secondStationId, fifthStationId, DISTANCE_THREE);
        sectionRestAssured.saveFailCaseTwo(firstLineId, failNewTestSectionDto);
    }

    private SectionRequestTestDto 테스트용_지하철_구간_생성(Long upStationId, Long downStationId, Long distance) {
        return SectionRequestTestDto.builder()
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
