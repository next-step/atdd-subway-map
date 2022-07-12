package nextstep.subway.section.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.application.dto.SectionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.subway.line.LineTestSource.line;
import static nextstep.subway.line.LineTestSource.lineId;
import static nextstep.subway.section.SectionTestSource.sectionRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    private SectionService target;

    @Mock
    private LineService lineService;

    @Test
    void 구간추가실패_신규상행역이기존의하행역이아님() {
        // given
        doReturn(line())
                .when(lineService)
                .findLineById(lineId);

        // when
        final IllegalArgumentException result = assertThrows(
                IllegalArgumentException.class,
                () -> target.addSection(lineId, sectionRequest()));

        // then
        assertThat(result.getMessage()).isEqualTo("신규상행역이 기존의 하행역이 아닙니다.");
    }

}