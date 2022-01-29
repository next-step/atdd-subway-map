package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("구간 관리")
public class SectionTest extends SectionFixData{
    private static int DEFAULT_DISTANCE = 5;

    @DisplayName("구간 생성")
    @Test
    void createSection() {
        // given
        createLine();
        Station upStation = createStation(FIRST_STATION_NAME);
        Station downStation = createStation(SECOND_STATION_NAME);

        // when
        Section section = createSection(upStation, downStation);

        // then
        assertThat(section).isNotNull();
        assertThat(section.getDownStation()).isEqualTo(downStation);
        assertThat(section.getUpStation()).isEqualTo(upStation);
    }
}
