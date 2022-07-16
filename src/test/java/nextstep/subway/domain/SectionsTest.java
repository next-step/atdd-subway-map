package nextstep.subway.domain;

import nextstep.subway.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SectionsTest {

    private Sections sections;

    private Line line;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        line = LineFactory.getMockLine(1L, "4호선", "blue");

        upStation = StationTest.getMockStation(1L, "사당역");
        downStation = StationTest.getMockStation(2L, "신논현역");

        sections = new Sections();
        sections.add(new Section(line, upStation, downStation, 10));
    }

    @Test
    @DisplayName("역 추가가 정상적으로 된다.")
    void addTest() {

        Station newStation = StationTest.getMockStation(3L, "금정역");
        sections.add(new Section(line, downStation, newStation, 10));
        assertThat(sections.getStations()).hasSize(3);
    }

    @Test
    @DisplayName("신규 상행선이 기존 하행선과 연결되어 있지 않으면 에러가 발생한다.")
    void addExceptionTest() {
        Station newUpStation = StationTest.getMockStation(3L, "강남역");
        Station newDownStation = StationTest.getMockStation(4L, "금정역");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> sections.add(new Section(line, newUpStation, newDownStation, 10)));
    }

    @Test
    @DisplayName("신규 상행선이 다른 하행선과 연결되어 있는 선이면 에러가 발생한다.")
    void addException2Test() {
        Station newUpStation = StationTest.getMockStation(3L, "강남역");
        sections.add(new Section(line, downStation, newUpStation, 10));

        Station newDownStation = StationTest.getMockStation(4L, "금정역");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> sections.add(new Section(line, downStation, newDownStation, 10)));
    }

    @Test
    @DisplayName("신규 하행선이 이미 존재하는 하행선이면 에러가 발생한다.")
    void addException3Test() {
        Station newDownStation = StationTest.getMockStation(3L, "강남역");
        sections.add(new Section(line, downStation, newDownStation, 10));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> sections.add(new Section(line, downStation, newDownStation, 10)));
    }


    @Test
    @DisplayName("Stations 의 역이 정상적으로 반환된다.")
    void getStationsTest() {
        assertThat(sections.getStations()).containsAnyOf(upStation, downStation);

    }

    @Test
    @DisplayName("역이 3개 이상인 경우, 삭제할 수 있다.")
    void deleteTest() {
        Station deleteStation = StationTest.getMockStation(3L, "강남역");
        sections.add(new Section(line, downStation, deleteStation, 10));

        sections.delete(deleteStation);
        assertThat(sections.getStations()).hasSize(2);
    }

    @Test
    @DisplayName("역이 2개 인경우 삭제할 수 없다.")
    void deleteExceptionTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> sections.delete(downStation));
    }
}