package nextstep.subway.section.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.application.dto.SectionResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static nextstep.subway.line.LineTestSource.line;
import static nextstep.subway.line.LineTestSource.lineId;
import static nextstep.subway.section.SectionTestSource.section;
import static nextstep.subway.section.SectionTestSource.sectionRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    private SectionService target;

    @Mock
    private LineService lineService;
    @Mock
    private SectionRepository sectionRepository;

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

    @Test
    void 구간추가실패_신규하행역이이미등록되어있음() {
        // given
        final Line line = line();
        final SectionRequest sectionRequest = sectionRequest(line.getDownStationId());

        doReturn(line)
                .when(lineService)
                .findLineById(lineId);

        doReturn(Collections.singletonList(section(sectionRequest.getDownStationId())))
                .when(sectionRepository)
                .findAllByLineId(lineId);

        // when
        final IllegalArgumentException result = assertThrows(
                IllegalArgumentException.class,
                () -> target.addSection(lineId, sectionRequest));

        // then
        assertThat(result.getMessage()).isEqualTo("신규하행역이 이미 등록되어 있습니다.");
    }

    @Test
    void 구간추가성공() {
        // given
        final Line line = line();
        final SectionRequest sectionRequest = sectionRequest(line.getDownStationId());

        doReturn(line)
                .when(lineService)
                .findLineById(lineId);

        doReturn(Collections.emptyList())
                .when(sectionRepository)
                .findAllByLineId(lineId);

        doReturn(section())
                .when(sectionRepository)
                .save(any(Section.class));

        // when
        final SectionResponse result = target.addSection(lineId, sectionRequest);

        // then
        assertThat(result).isNotNull();
    }

}