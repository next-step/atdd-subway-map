package nextstep.subway.domain.entity;

import nextstep.subway.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.entity.StationTest.getMockStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SectionTest {

    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        upStation = getMockStation(1L, "사당역");
        downStation = getMockStation(2L, "신논현역");
    }

    @Test
    @DisplayName("upStation과 downStation이 동일하면 역을 생성할 수 없다.")
    void createTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> new Section(null, upStation, upStation));
    }

    @Test
    @DisplayName("UpStation 또는 DownStation과 동일하면 true 를 반환한다.")
    void existAnyStationTest() {

        Section section = new Section(null, upStation, downStation);

        assertThat(section.existAnyStation(upStation)).isTrue();
        assertThat(section.existAnyStation(downStation)).isTrue();
        assertThat(section.existAnyStation(getMockStation(3L, "금정역"))).isFalse();
    }

    @Test
    @DisplayName("UpStation과 동일하면 true 를 반환한다.")
    void equalsUpStationTest() {
        Section section = new Section(null, upStation, downStation);

        assertThat(section.equalsUpStation(upStation)).isTrue();
        assertThat(section.equalsUpStation(downStation)).isFalse();
    }

    @Test
    @DisplayName("DownStation과 동일하면 true 를 반환한다.")
    void equalsDownStationTest() {
        Section section = new Section(null, upStation, downStation);

        assertThat(section.equalsDownStation(upStation)).isFalse();
        assertThat(section.equalsDownStation(downStation)).isTrue();
    }
}