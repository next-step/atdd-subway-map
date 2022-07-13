package nextstep.subway.domain.line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.Fixture.강남역;
import static nextstep.subway.domain.Fixture.분당역;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SectionTest {

    @Test
    @DisplayName("노선역을 생성한다.")
    void test1() {
        //when & then
        assertDoesNotThrow(
            () -> new Section(null, 강남역.getId(), 분당역.getId(), 10)
        );
    }

    @Test
    @DisplayName("구간을 통해 노선 내 역이 상행 종점역인지 하행 종점역인지 알 수 있다.")
    void test2() {
        // given
        Section 구간 = new Section(null, 강남역.getId(), 분당역.getId(), 10);

        //when & then
        assertThat(구간.getUpStationId()).isEqualTo(강남역.getId());
        assertThat(구간.getDownStationId()).isEqualTo(분당역.getId());
    }


    @Test
    @DisplayName("거리는 1 이상이어야 합니다")
    void test3() {
        //given
        int 거리 = -1;
        //when & then
        Assertions.assertThatThrownBy(
            () -> new Section(null, 강남역.getId(), 분당역.getId(), 거리)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
