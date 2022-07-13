package nextstep.subway.domain.entity;

import lombok.RequiredArgsConstructor;
import nextstep.subway.BaseSpringBootTest;
import nextstep.subway.infra.LineRepository;
import nextstep.subway.infra.SectionRepository;
import nextstep.subway.infra.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class LineTest extends BaseSpringBootTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    EntityManager entityManager;


    @Test
    @DisplayName("Line 등록시 이름은 필수 값이어야 한다.")
    void lineRequiredNameTest() {
        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("name"));
    }

    @Test
    @DisplayName("Line 등록시 색상은 필수 값이어야 한다.")
    void lineRequiredColorTest() {
        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("color"));
    }

    @Test
    @DisplayName("Line 등록시 거리는 필수 값이어야 한다.")
    void lineRequiredDistanceTest() {
        assertThatNullPointerException()
                .isThrownBy(() -> createMockLine("distance"));
    }


    @Test
    @DisplayName("stations가 정상적으로 조회된다.")
    @Transactional
    void getStationsTest() {
        Line line = lineRepository.save(createMockLine());
        Station station1 = stationRepository.save(Station.builder().name("남태령역").build());
        Station station2 = stationRepository.save(Station.builder().name("사당역").build());

        sectionRepository.saveAndFlush(Section.builder()
                .line(line)
                .upStation(station1)
                .downStation(station2)
                .build());

        entityManager.clear();

        Line getLine = lineRepository.getById(line.getId());
        assertThat(getLine.getStations()).hasSize(2);
    }

    @Test
    @DisplayName("구간이 2개 이하일경우에는 삭제 유효성 체크시 false 가 외에는 true가 나타난다.")
    @Transactional
    void inValidSectionDeleteTest() {
        Line line = lineRepository.save(createMockLine());
        Station station1 = stationRepository.save(Station.builder().name("남태령역").build());
        Station station2 = stationRepository.save(Station.builder().name("사당역").build());

        sectionRepository.saveAndFlush(Section.builder()
                .line(line)
                .upStation(station1)
                .downStation(station2)
                .build());

        Line invalidSectionDeleteLine = lineRepository.getById(line.getId());
        assertThat(invalidSectionDeleteLine.inValidSectionDelete()).isTrue();


        Station station3 = stationRepository.save(Station.builder().name("총신대입구역").build());
        sectionRepository.save(Section.builder()
                .line(line)
                .upStation(station2)
                .downStation(station3)
                .build());

        entityManager.clear();
        Line validSectionDeleteLine = lineRepository.getById(line.getId());
        assertThat(validSectionDeleteLine.inValidSectionDelete()).isFalse();

    }

    private Line createMockLine() {
        return createMockLine(null);
    }

    private Line createMockLine(String ignoreFieldName) {
        Line line = Line.builder()
                .name("name")
                .color("color")
                .distance(10)
                .build();

        if (ignoreFieldName == null) {
            return line;
        }


        switch (ignoreFieldName) {
            case "name": {
                return line.toBuilder()
                        .name(null)
                        .build();
            }
            case "color": {
                return line.toBuilder()
                        .color(null)
                        .build();
            }
            case "distance": {
                return line.toBuilder()
                        .distance(null)
                        .build();
            }
        }
        return line;
    }

}