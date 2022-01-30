package nextstep.subway.domain.entity;

import nextstep.subway.domain.service.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class SectionsTest {

    private final static Validator<Station> DUMMY_VALIDATOR = _station -> {};

    private static Stream<Arguments> provideSectionsAndLineWhenAdd() {
        final Line line = new Line();
        final Station station1 = new Station(1L, "강남역", DUMMY_VALIDATOR);
        final Station station2 = new Station(2L,"역삼역", DUMMY_VALIDATOR);
        final Station station3 = new Station(3L,"잠실역", DUMMY_VALIDATOR);

        final List<Section> _sections = new ArrayList<>();
        _sections.add(new Section(1L, line, station1, station2, 1));
        final Sections sections = new Sections(_sections);

        return Stream.of(Arguments.of(sections, line, station1, station2, station3));
    }

    private static Stream<Arguments> provideSectionsAndLineWhenRemove() {
        final Line line = new Line();
        final Station station1 = new Station(1L, "강남역", DUMMY_VALIDATOR);
        final Station station2 = new Station(2L,"역삼역", DUMMY_VALIDATOR);
        final Station station3 = new Station(3L,"잠실역", DUMMY_VALIDATOR);

        final List<Section> _sections = new ArrayList<>();
        _sections.add(new Section(1L, line, station1, station2, 1));
        _sections.add(new Section(2L, line, station2, station3, 1));
        final Sections sections = new Sections(_sections);

        return Stream.of(Arguments.of(sections, line, station1, station2, station3));
    }

    @DisplayName("지하철 구간 등록")
    @ParameterizedTest
    @MethodSource("provideSectionsAndLineWhenAdd")
    void add(final Sections sections, final Line line, final Station station1, final Station station2, final Station station3) {
        // given
        // when
        sections.add(new Section(2L, line, station2, station3, 1));

        // then
        assertThat(sections.getStations()).isEqualTo(Arrays.asList(station1, station2, station3));
    }

    @DisplayName("노선에 등록된 하행 종점 역이 아닌 역을 상행 역 으로 지하철 구간 등록")
    @ParameterizedTest
    @MethodSource("provideSectionsAndLineWhenAdd")
    void addWithUpStationNoContains(final Sections sections, final Line line, final Station station1, final Station station2, final Station station3) {
        // given
        // when
        final Section section = new Section(2L, line, station1, station3, 1);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.add(section))
                .withMessage("노선에 등록된 하행 종점 역이 아닌 역을 상행 역으로 설정할 수 없습니다.");
    }

    @DisplayName("이미 노선에 구간 으로 등록된 역을 하행 역으로 지하철 구간 등록")
    @ParameterizedTest
    @MethodSource("provideSectionsAndLineWhenAdd")
    void addWithDownStationContains(final Sections sections, final Line line, final Station station1, final Station station2, final Station station3) {
        // given
        // when
        final Section section = new Section(2L, line, station2, station1, 1);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.add(section))
                .withMessage("이미 노선에 구간 으로 등록된 역을 하행 역으로 설정할 수 없습니다.");
    }

    @DisplayName("지하철 구간 제거")
    @ParameterizedTest
    @MethodSource("provideSectionsAndLineWhenRemove")
    void remove(final Sections sections, final Line line, final Station station1, final Station station2, final Station station3) {
        // given
        // when
        sections.remove(station3);

        // then
        assertThat(sections.getStations()).isEqualTo(Arrays.asList(station1, station2));
    }

    @DisplayName("하행 종점 역이 아닌 지하철 구간 제거")
    @ParameterizedTest
    @MethodSource("provideSectionsAndLineWhenRemove")
    void removeWithoutEndingStation(final Sections sections, final Line line, final Station station1, final Station station2, final Station station3) {
        // given
        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.remove(station2))
                .withMessage("하행 종점 역이 아니면 제거할 수 없습니다.");
    }

    @DisplayName("구간이 한 개인 지하철 구간 제거")
    @ParameterizedTest
    @MethodSource("provideSectionsAndLineWhenRemove")
    void removeOnlyOneSection(final Sections sections, final Line line, final Station station1, final Station station2, final Station station3) {
        // given
        // when
        sections.remove(station3);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.remove(station2))
                .withMessage("구간이 한 개 이하면 제거할 수 없습니다.");
    }
}
