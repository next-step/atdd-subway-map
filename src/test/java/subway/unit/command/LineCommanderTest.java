package subway.unit.command;

import autoparams.AutoSource;
import autoparams.Repeat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.command.LineCommand;
import subway.domain.command.LineCommander;
import subway.domain.entity.Line;
import subway.domain.repository.LineRepository;
import subway.internal.BaseTestSetup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LineCommanderTest extends BaseTestSetup {

    @Autowired
    private LineCommander sut;

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
}
