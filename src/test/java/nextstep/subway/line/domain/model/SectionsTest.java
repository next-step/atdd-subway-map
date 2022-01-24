package nextstep.subway.line.domain.model;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.station.domain.model.Station;

class SectionsTest {
    private static final Distance DUMMY_DISTANCE = new Distance(100);

    @DisplayName("toStations이 올바른 순서로 반환 하는지 테스트")
    @ValueSource(strings = {
        "0역,3역,1역,5역,2역,4역",
        "0역,3역,1역,5역,2역,4역",
        "0역,3역,1역,5역,2역,4역"
    })
    @ParameterizedTest
    void toStations(String strNames) {
        Line line = new Line();
        List<String> names = Arrays.stream(strNames.split(","))
                  .collect(Collectors.toList());
        for (int sectionIndex = 1; sectionIndex < names.size(); sectionIndex++) {
            Station upStation = new Station((long) sectionIndex - 1, names.get(sectionIndex - 1));
            Station downStation = new Station((long) sectionIndex, names.get(sectionIndex));
            line.addSection(upStation, downStation, DUMMY_DISTANCE);
        }

        List<String> stationNames = line.stations().stream()
                                              .map(Station::getName)
                                              .collect(Collectors.toList());

        assertThat(stationNames)
            .withFailMessage("결과값이 다름 : " + String.join(",", stationNames))
            .containsExactly(names.toArray(new String[0]));
    }
}
