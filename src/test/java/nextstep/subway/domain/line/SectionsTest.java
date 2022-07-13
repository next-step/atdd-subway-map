package nextstep.subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.domain.Fixture.강남역;
import static nextstep.subway.domain.Fixture.분당역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {
    @Test
    @DisplayName("역들 안에 들어가는 역이 두 개 이상이 아니면 IllegalArgumentException 예외를 발생한다.")
    void test2() {
        // given
        Section 구간 = new Section(null, 강남역.getId(), 분당역.getId(), 10);
        List<Section> stationList = new ArrayList<>();

        // when & then
        assertThatThrownBy(
            () -> new Sections(stationList)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("역들 안에는 상행 종점역과 하행 종점역이 포함된다.")
    void test3() {
        // given
        Section 구간 = new Section(null, 강남역.getId(), 분당역.getId(), 10);

        // when
        Sections stations = new Sections(Arrays.asList(구간));

        // then
        assertAll(
            () -> assertThat(stations.getSections()).hasSize(1),
            () -> assertThat(stations.getSections()).contains(구간)
        );
    }
}
