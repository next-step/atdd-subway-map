package subway.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
        Long upStationId = Long.valueOf(1);
        Long downStationId = Long.valueOf(2);
        Integer distance = 10;

        lineRepository.save(Line.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build());

        List<Line> lines = lineRepository.findAll();
        Line line = lines.get(0);

        assertThat(line.getName()).isEqualTo(name);
        assertThat(line.getColor()).isEqualTo(color);
        assertThat(line.getUpStationId()).isEqualTo(upStationId);
        assertThat(line.getDownStationId()).isEqualTo(downStationId);
        assertThat(line.getDistance()).isEqualTo(distance);
    }
}
