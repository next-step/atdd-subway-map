package subway.line.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import subway.line.section.domain.Section;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@DisplayName("Line 도메인 테스트")
@ExtendWith(SpringExtension.class)
@DataJpaTest
class LineTest {
    private final Long 기존_노선_거리 = 10L;
    private final Station 덕소역 = new Station("덕소역");
    private final Station 도심역 = new Station("도심역");
    private final Station 양정역 = new Station("양정역");
    private final Line 경의중앙선 = new Line(
        "경의중앙선",
        "bg-red-600",
        덕소역,
        도심역,
        기존_노선_거리);
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository.save(덕소역);
        stationRepository.save(도심역);
        stationRepository.save(양정역);
    }

    @DisplayName("노선이 주어지고 구간을 추가하면 거리가 추가된 구간의 거리만큼 증가한다.")
    @Test
    void createSection() {
        // Given
        lineRepository.save(경의중앙선);
        Section 추가_구간 = new Section(도심역, 양정역, 경의중앙선, 5L);

        // When
        경의중앙선.createSection(추가_구간);

        // Then
        assertThat(경의중앙선.getDistance()).isEqualTo(기존_노선_거리 + 추가_구간.getDistance());
    }
}
