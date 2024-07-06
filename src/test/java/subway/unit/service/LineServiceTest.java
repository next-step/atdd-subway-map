package subway.unit.service;

import autoparams.AutoSource;
import autoparams.Repeat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.contract.LineCommand;
import subway.domain.entity.Line;
import subway.domain.repository.LineRepository;
import subway.domain.service.LineService;
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
    @DisplayName("createLine")
    class CreateLineTest {
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_creates_line(LineCommand.CreateLine command) {
            // when
            Long id = sut.createLine(command);

            // then
            Optional<Line> actual = lineRepository.findById(id);
            assertThat(actual.get().getName()).isEqualTo(command.getName());
            assertThat(actual.get().getColor()).isEqualTo(command.getColor());
        }
    }

    @Nested
    @DisplayName("getLines")
    class GetLinesTest {
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_returns_lines(List<Line> lines) {
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
