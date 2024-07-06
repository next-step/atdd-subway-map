package subway.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import subway.CreateLineRequest;
import subway.Line;
import subway.LineRepository;
import subway.LineService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LineServiceTest {

    @Autowired
    private LineService sut;

    @Autowired
    private LineRepository lineRepository;

    @Nested
    @DisplayName("CreateLine Test")
    class CreateLine {
        @DisplayName("노선을 생성한다.")
        @Test
        public void sut_create_line_entity() {
            // given
            CreateLineRequest request = new CreateLineRequest("1호선", "남색", 1L, 2L, 5L);

            // when
            Long id = sut.createLine(request);

            // then
            Optional<Line> actual = lineRepository.findById(id);
            assertThat(actual.get().getName()).isEqualTo("1호선");
            assertThat(actual.get().getColor()).isEqualTo("남색");
            assertThat(actual.get().getUpStationId()).isEqualTo(1L);
            assertThat(actual.get().getDownStationId()).isEqualTo(2L);
            assertThat(actual.get().getDistance()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("GetLines Test")
    class GetLines {
        @DisplayName("노선을 조회한다.")
        @Test
        public void sut_create_line_entity() {
            // given
            Line firstLine = new Line("1호선", "남색", 1L, 2L, 2L);
            Line secondLine = new Line("2호선", "초록색", 3L, 4L, 2L);
            lineRepository.save(firstLine);
            lineRepository.save(secondLine);

            // when
            List<Line> actual = sut.getLines();

            // then
            List<String> lineNames = actual.stream().map(Line::getName).collect(Collectors.toList());
            assertThat(lineNames).containsExactly("1호선", "2호선");
        }
    }
}
