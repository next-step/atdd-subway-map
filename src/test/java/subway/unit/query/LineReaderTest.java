package subway.unit.query;

import autoparams.AutoSource;
import autoparams.Repeat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;
import subway.domain.view.LineView;
import subway.domain.entity.Line;
import subway.domain.query.LineReader;
import subway.domain.repository.LineRepository;
import subway.internal.BaseTestSetup;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class LineReaderTest extends BaseTestSetup {

    @Autowired
    private LineReader sut;

    @Autowired
    private LineRepository lineRepository;

    @Nested
    @DisplayName("getLine")
    class GetLineTest {
        @ParameterizedTest
        @AutoSource
        @Repeat(5)
        public void sut_returns_line(Line line) {
            // given
            lineRepository.save(line);

            // when
            LineView.Main actual = sut.getOneById(line.getId());

            // then
            assertThat(line.getName()).isEqualTo(actual.getName());
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_error_if_not_found_line(Long id) {
            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.getOneById(id));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_LINE);
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
            lines.forEach((line) -> {

            });
            lineRepository.saveAll(lines);

            // when
            List<LineView.Main> actual = sut.getAllLines();

            // then
            List<String> lineNames = actual.stream().map(LineView.Main::getName).collect(Collectors.toList());
            assertThat(lineNames).isEqualTo(lines.stream().map(Line::getName).collect(Collectors.toList()));
        }
    }
}
