package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.line.domain.exception.SectionAddFailException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionsTest {

    private final Line line = new Line("2호선", "초록색");
    private final StationValidator stationValidator = new FakeStationValidator();
    private final long lastDownStationId = 2L;
    private final Sections sections = new Sections(
        new Section(
            line,
            1L,
            lastDownStationId,
            10,
            stationValidator
        )
    );

    @Test
    void 새로운_구간의_하행역은_해당_노선에_등록되어있는_역일_수_없다() {
        final long 상행역Id = 1L;
        final Sections sections = new Sections(new Section(line, 상행역Id, 2L, 10, stationValidator));
        final Section newSection = new Section(line, 10L, 상행역Id, 10, stationValidator);

        assertThrows(SectionAddFailException.class, () -> sections.add(newSection));
    }

    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다")
    @Nested
    class 새로운_구간의_상행역은_해당_노선에_등록되어있는_하행_종점역이어야_한다 {
        @DisplayName("동일하다면 등록 가능하다")
        @Test
        void 동일하다면_등록_가능하다() {
            final Section newSection = new Section(line, lastDownStationId, 10L, 1, stationValidator);

            assertDoesNotThrow(() -> sections.add(newSection));
        }

        @DisplayName("동일하지 않다면 등록 불가능하다")
        @Test
        void 동일하지_않다면_등록_불가능하다() {
            final Section newSection = new Section(line, 3L, 10L, 1, stationValidator);

            assertThrows(SectionAddFailException.class, () -> sections.add(newSection));
        }
    }
}
