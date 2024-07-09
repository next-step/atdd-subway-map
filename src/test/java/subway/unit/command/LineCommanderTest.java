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
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;
import subway.fixtures.LineFixture;
import subway.internal.BaseTestSetup;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

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

    @Nested
    @DisplayName("updateLine")
    class UpdateLineTest {
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_updates_line(LineCommand.UpdateLine command) {
            // given
            Line line = LineFixture.prepareLineOne(1L, 5L);
            lineRepository.save(line);
            setId(command, line.getId());

            // when
            sut.updateLine(command);

            // then
            Optional<Line> actual = lineRepository.findById(command.getId());
            assertThat(actual.get().getName()).isEqualTo(command.getName());
            assertThat(actual.get().getColor()).isEqualTo(command.getColor());
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_error_if_not_found_line(LineCommand.UpdateLine command) {
            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.updateLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_LINE);
        }
    }

    @Nested
    @DisplayName("deleteLineById")
    class DeleteLineTest {
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_deletes_line() {
            // given
            Line line = LineFixture.prepareLineOne(1L, 5L);
            lineRepository.save(line);

            // when
            sut.deleteLineById(line.getId());

            // then
            Optional<Line> actual = lineRepository.findById(line.getId());
            assertThat(actual).isEmpty();
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_error_if_not_found_line(Long id) {
            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.deleteLineById(id));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_LINE);
        }
    }

    private static void setId(LineCommand.UpdateLine command, Long id) {
        try {
            Field idField = LineCommand.UpdateLine.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(command, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
