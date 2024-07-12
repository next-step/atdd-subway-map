package subway.unit.entity;

import autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import subway.domain.command.LineCommand;
import subway.domain.entity.line.Line;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    @Nested
    class Init {
        @DisplayName("init")
        @ParameterizedTest
        @AutoSource
        public void sut_returns_new_line(LineCommand.CreateLine command) {
            // when
            Line actual = Line.init(command);

            // then
            assertThat(actual.getName()).isEqualTo(command.getName());
            assertThat(actual.getColor()).isEqualTo(command.getColor());

            assertThat(actual.getSections().size()).isEqualTo(1);
            assertThat(actual.getSections().get(0).getUpStationId()).isEqualTo(command.getUpStationId());
            assertThat(actual.getSections().get(0).getDownStationId()).isEqualTo(command.getDownStationId());
            assertThat(actual.getSections().get(0).getDistance()).isEqualTo(command.getDistance());
        }
    }

    @Nested
    class Update {
        @DisplayName("update")
        @ParameterizedTest
        @AutoSource
        public void sut_updated(
                Line sut,
                LineCommand.UpdateLine command
        ) {
            // when
            sut.update(command);


            // then
            assertThat(sut.getName()).isEqualTo(command.getName());
            assertThat(sut.getColor()).isEqualTo(command.getColor());
        }
    }
}
