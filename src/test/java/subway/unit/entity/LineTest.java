package subway.unit.entity;

import autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import subway.domain.command.LineCommand;
import subway.domain.entity.Line;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    @Nested
    class Init {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_new_line(LineCommand.CreateLine command) {
            // when
            Line actual = Line.init(command);

            // then
            assertThat(actual.getName()).isEqualTo(command.getName());
            assertThat(actual.getColor()).isEqualTo(command.getColor());
            assertThat(actual.getUpStationId()).isEqualTo(command.getUpStationId());
            assertThat(actual.getDownStationId()).isEqualTo(command.getDownStationId());
            assertThat(actual.getDistance()).isEqualTo(command.getDistance());
        }
    }
}
