package nextstep.subway.line.domain.model;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.model.Station;

class SectionsTest {
    private static final Line DUMMY_LINE = new Line();
    private static final Distance DUMMY_DISTANCE = new Distance(100);

    private Sections sections;
    private Station upStationInFirstSection;
    private Station downStationInFirstSection;

    @BeforeEach
    void setUp() {
        setUpSections();
    }

    private void setUpSections() {
        upStationInFirstSection = new Station(1, "1역");
        downStationInFirstSection = new Station(2, "2역");

        Sections sections = new Sections();
        sections.add(dummySection(1, upStationInFirstSection, downStationInFirstSection));
        this.sections = sections;
    }

    private Section dummySection(long id, Station upStation, Station downStation) {
        return Section.builder()
            .id(id)
            .upStation(upStation)
            .downStation(downStation)
            .line(DUMMY_LINE)
            .distance(DUMMY_DISTANCE)
            .build();
    }

    @DisplayName("Section 추가 테스트")
    @Test
    void add() {
        Station newDownStation = new Station(3, "새로운 하행");
        sections.add(dummySection(1, downStationInFirstSection, newDownStation));
        assertThat(sections.size())
            .isEqualTo(2);
    }

    @DisplayName("Section 추가 실패 테스트 - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void addThatFailing1() {
        Station newDownStation = new Station(3, "오류가 발생할 하행");

        assertThatThrownBy(() -> sections.add(dummySection(1, upStationInFirstSection, newDownStation)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Section 추가 실패 테스트 - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void addThatFailing2() {
        assertThatThrownBy(() -> sections.add(dummySection(1, downStationInFirstSection, upStationInFirstSection)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Section 삭제 테스트")
    @Test
    void delete() {
        Station newDownStation = new Station(3, "새로운 하행");
        sections.add(dummySection(2, downStationInFirstSection, newDownStation));
        sections.delete(2);

        assertThat(sections.size())
            .isEqualTo(1);
    }

    @DisplayName("Section 삭제 실패 테스트 - 종점역 삭제가 아닐 경우")
    @Test
    void deleteThatFailing1() {
        Station newDownStation = new Station(3, "새로운 하행");
        sections.add(dummySection(2, downStationInFirstSection, newDownStation));

        assertThatThrownBy(() -> sections.delete(1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Section 삭제 실패 테스트 - 1개만 있을 경우")
    @Test
    void deleteThatFailing2() {
        assertThatThrownBy(() -> sections.delete(1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("toStations이 올바른 순서로 지하철역을 반환 하는지 테스트")
    @Test
    void toStations() {
        Station station3 = new Station(5, "3역");
        Station station4 = new Station(6, "4역");
        Station station5 = new Station(7, "5역");
        Station station6 = new Station(8, "6역");
        sections.add(dummySection(2, downStationInFirstSection, station3));
        sections.add(dummySection(3, station3, station4));
        sections.add(dummySection(4, station4, station5));
        sections.add(dummySection(5, station5, station6));

        List<String> actual =
            sections.toStations().stream()
                    .map(Station::getName)
                    .collect(Collectors.toList());
        List<String> expert = Arrays.asList("1역", "2역", "3역", "4역", "5역", "6역");
        assertThat(actual)
            .containsExactly(expert.toArray(new String[0]));
    }
}
