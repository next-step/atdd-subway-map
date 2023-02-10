package subway.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.CreateLineSectionException;
import subway.exception.DeleteLineSectionException;
import subway.model.Stations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.unit.UnitTestFixture.*;

class StationsTest {

    @DisplayName("지하철 상행 / 하행 확인")
    @Test
    void getUpStation() {
        // given
        Stations stations = 지하철역_리스트_생성(강남역, 역삼역);

        // when
        Long upStationId = stations.getUpStationId();
        Long downStationId = stations.getDownStationId();

        // then
        assertThat(upStationId).isEqualTo(강남역.getId());
        assertThat(downStationId).isEqualTo(역삼역.getId());
    }

    @DisplayName("지하철 구간 등록 성공")
    @Test
    void createLineSection() {
        // when
        강남역_역삼역.createLineSection(역삼역, 선릉역);

        // then
        assertThat(강남역_역삼역.getDownStationId()).isEqualTo(선릉역.getId());
    }

    @DisplayName("지하철 구간 등록 실패 : 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void createLineSection_fail1() {
        // when & then
        assertThatThrownBy(() -> 강남역_역삼역.createLineSection(삼성역, 선릉역))
                .isInstanceOf(CreateLineSectionException.class);
    }

    @DisplayName("지하철 구간 등록 실패 : 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void createLineSection_fail2() {
        // when & then
        assertThatThrownBy(() -> 강남역_역삼역.createLineSection(역삼역, 강남역))
                .isInstanceOf(CreateLineSectionException.class);
    }

    @DisplayName("지하철 구간 삭제 성공")
    @Test
    void deleteLineSection() {
        // when
        강남역_역삼역_선릉역.deleteLineSection(선릉역);

        // then
        assertThat(강남역_역삼역_선릉역.getDownStationId()).isEqualTo(역삼역.getId());
    }

    @DisplayName("지하철 구간 삭제 실패 : 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.")
    @Test
    void deleteLineSection_fail1() {
        // when & then
        assertThatThrownBy(() -> 강남역_역삼역_선릉역.deleteLineSection(역삼역))
                .isInstanceOf(DeleteLineSectionException.class);
    }

    @DisplayName("지하철 구간 삭제 실패 : 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    @Test
    void deleteLineSection_fail2() {
        // when & then
        assertThatThrownBy(() -> 강남역_역삼역.deleteLineSection(선릉역))
                .isInstanceOf(DeleteLineSectionException.class);
    }

}
