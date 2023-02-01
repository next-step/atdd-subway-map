package subway.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LineRepositoryTest {

    @Autowired
    LineRepository lineRepository;

    @AfterEach
    public void cleanup() {
        lineRepository.deleteAll();
    }

    @DisplayName("생성한 지하철 노선을 불러온다.")
    @Test
    void findAllLines() {
        String name = "신분당선";
        String color = "bg-red-600";
        Station upStation = Station.builder()
                .name("강남역")
                .build();
        Station downStation = Station.builder()
                .name("광교역")
                .build();
        Integer distance = 10;

        lineRepository.save(Line.builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build());

        List<Line> lines = lineRepository.findAll();
        Line line = lines.get(0);

        assertThat(line.getName()).isEqualTo(name);
        assertThat(line.getColor()).isEqualTo(color);
        assertThat(line.getUpStation().getName()).isEqualTo(upStation.getName());
        assertThat(line.getDownStation().getName()).isEqualTo(downStation.getName());
        assertThat(line.getDistance()).isEqualTo(distance);
    }
}
