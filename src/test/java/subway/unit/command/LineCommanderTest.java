package subway.unit.command;

import autoparams.AutoSource;
import autoparams.Repeat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import subway.domain.command.LineCommand;
import subway.domain.command.LineCommander;
import subway.domain.entity.Line;
import subway.domain.entity.Station;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;
import subway.domain.repository.LineRepository;
import subway.domain.repository.StationRepository;
import subway.fixtures.LineFixture;
import subway.internal.BaseTestSetup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class LineCommanderTest extends BaseTestSetup {

    @Autowired
    private LineCommander sut;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    private Pair<Station, Station> addUpDownStation() {
        Station upStation = new Station("삼성역");
        Station downStation = new Station("잠실역");
        stationRepository.save(upStation);
        stationRepository.save(downStation);
        return Pair.of(upStation, downStation);
    }

    private Line addLine(Long upStationId, Long downStationId) {
        Line line = LineFixture.prepareLineOne(upStationId, downStationId);
        lineRepository.save(line);
        return line;
    }

    @Nested
    @DisplayName("createLine")
    class CreateLineTest {
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_creates_line(String lineName, String color, Long distance) {
            // given
            Pair<Station, Station> upDownStation = addUpDownStation();
            LineCommand.CreateLine command = new LineCommand.CreateLine(
                    lineName,
                    color,
                    upDownStation.getFirst().getId(),
                    upDownStation.getSecond().getId(),
                    distance
            );

            // when
            Long id = sut.createLine(command);

            // then
            Optional<Line> actual = lineRepository.findById(id);
            assertThat(actual.get().getName()).isEqualTo(command.getName());
            assertThat(actual.get().getColor()).isEqualTo(command.getColor());
        }

        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_throws_if_not_found_upStation(String lineName, String color, Long distance) {
            // given
            Pair<Station, Station> upDownStation = addUpDownStation();
            LineCommand.CreateLine command = new LineCommand.CreateLine(
                    lineName,
                    color,
                    123213L,
                    upDownStation.getSecond().getId(),
                    distance
            );

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.createLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_throws_if_not_found_downStation(String lineName, String color, Long distance) {
            // given
            Pair<Station, Station> upDownStation = addUpDownStation();
            LineCommand.CreateLine command = new LineCommand.CreateLine(
                    lineName,
                    color,
                    upDownStation.getFirst().getId(),
                    123213L,
                    distance
            );

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.createLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }
    }

    @Nested
    @DisplayName("updateLine")
    class UpdateLineTest {
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_updates_line(String lineName, String color, Long distance) {
            // given
            Line line = addLine(111L, 211L);
            Pair<Station, Station> upDownStation = addUpDownStation();

            LineCommand.UpdateLine command = new LineCommand.UpdateLine(
                    line.getId(),
                    lineName, color,
                    upDownStation.getFirst().getId(),
                    upDownStation.getSecond().getId(),
                    distance
            );

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

        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_throws_if_not_found_upStation(String lineName, String color, Long distance) {
            // given
            Line line = addLine(111L, 211L);
            Pair<Station, Station> upDownStation = addUpDownStation();

            LineCommand.UpdateLine command = new LineCommand.UpdateLine(
                    line.getId(),
                    lineName, color,
                    123123L,
                    upDownStation.getSecond().getId(),
                    distance
            );

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.updateLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_throws_if_not_found_downStation(String lineName, String color, Long distance) {
            // given
            Line line = addLine(111L, 211L);
            Pair<Station, Station> upDownStation = addUpDownStation();

            LineCommand.UpdateLine command = new LineCommand.UpdateLine(
                    line.getId(),
                    lineName, color,
                    upDownStation.getFirst().getId(),
                    123123L,
                    distance
            );

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.updateLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
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
            Line line = addLine(111L, 211L);

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
}
