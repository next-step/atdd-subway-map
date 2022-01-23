package nextstep.subway.line.domain.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.station.domain.model.Station;

class LineTest {
    @CsvSource({
        "UP,DOWN,1",
        "UP,DOWN,2",
        "UP,DOWN,100"
    })
    @DisplayName("지하철역과 지하철역을 잇는 Section 생성 테스트")
    @ParameterizedTest
    void createSection(String upStationName, String downStationName, int distanceValue) {
        Distance distance = new Distance(distanceValue);
        Line line = new Line();
        Station upStation = new Station(upStationName);
        Station downStation = new Station(downStationName);
        Section section = line.createSection(upStation, downStation, distance);

        assertThat(line.matchSectionsSize(1)).isTrue();
        assertThat(section.getUpStation().getName()).isEqualTo(upStationName);
        assertThat(section.getDownStation().getName()).isEqualTo(downStationName);
        assertThat(section.getDistance()).isEqualTo(distance);
    }
}
