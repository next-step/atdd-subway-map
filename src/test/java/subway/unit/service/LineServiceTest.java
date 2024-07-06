package subway.unit.service;

import autoparams.AutoSource;
import autoparams.Repeat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import subway.CreateLineRequest;
import subway.Line;
import subway.LineRepository;
import subway.LineService;
import subway.internal.BaseTestSetup;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineServiceTest extends BaseTestSetup {

    @Autowired
    private LineService sut;

    @Autowired
    private LineRepository lineRepository;

    @Nested
    @DisplayName("CreateLine Test")
    class CreateLine {
        @DisplayName("노선을 생성한다.")
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_create_line_entity(CreateLineRequest request) {
            // when
            Long id = sut.createLine(request);

            // then
            Optional<Line> actual = lineRepository.findById(id);
            assertThat(actual.get().getName()).isEqualTo(request.getName());
            assertThat(actual.get().getColor()).isEqualTo(request.getColor());
            assertThat(actual.get().getUpStationId()).isEqualTo(request.getUpStationId());
            assertThat(actual.get().getDownStationId()).isEqualTo(request.getDownStationId());
            assertThat(actual.get().getDistance()).isEqualTo(request.getDistance());
        }
    }

    @Nested
    @DisplayName("GetLines Test")
    class GetLines {
        @DisplayName("노선을 조회한다.")
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_create_line_entity(List<Line> lines) {
            // given
            lineRepository.saveAll(lines);

            // when
            List<Line> actual = sut.getLines();

            // then
            List<String> lineNames = actual.stream().map(Line::getName).collect(Collectors.toList());
            assertThat(lineNames).isEqualTo(lines.stream().map(Line::getName).collect(Collectors.toList()));
        }
    }
}
