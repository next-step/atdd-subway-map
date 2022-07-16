package nextstep.subway.domain;

import nextstep.subway.BaseSpringBootTest;
import nextstep.subway.infra.LineRepository;
import nextstep.subway.infra.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class LineTest extends BaseSpringBootTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;


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
    @DisplayName("stations가 정상적으로 조회된다.")
    @Transactional
    void getStationsTest() {
        Line line = lineRepository.save(createMockLine());
        Station station1 = stationRepository.save(Station.builder().name("남태령역").build());
        Station station2 = stationRepository.save(Station.builder().name("사당역").build());

        line.addSection(station1, station2, 10);
        assertThat(line.getStations()).hasSize(2);
    }


    private Line createMockLine() {
        return createMockLine(null);
    }

    private Line createMockLine(String ignoreFieldName) {
        Line line = Line.builder()
                .name("name")
                .color("color")
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
        }
        return line;
    }

}