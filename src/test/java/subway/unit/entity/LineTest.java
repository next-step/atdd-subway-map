package subway.unit.entity;

import autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import subway.domain.command.LineCommand;
import subway.domain.entity.line.Line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
            assertAll("assert init",
                    () -> assertThat(actual.getName()).isEqualTo(command.getName()),
                    () -> assertThat(actual.getColor()).isEqualTo(command.getColor()),

                    // section
                    () -> assertThat(actual.getSections().size()).isEqualTo(1),
                    () -> assertThat(actual.getSections().getLastSection().getUpStationId()).isEqualTo(command.getUpStationId()),
                    () -> assertThat(actual.getSections().getLastSection().getDownStationId()).isEqualTo(command.getDownStationId()),
                    () -> assertThat(actual.getSections().getLastSection().getDistance()).isEqualTo(command.getDistance())
            );
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
