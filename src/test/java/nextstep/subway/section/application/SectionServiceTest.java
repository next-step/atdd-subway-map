package nextstep.subway.section.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.application.dto.SectionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.subway.line.LineTestSource.lineId;
import static nextstep.subway.line.LineTestSource.lineWithSection;
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
        doReturn(lineWithSection())
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
        final Line line = lineWithSection();
        final SectionRequest sectionRequest = sectionRequest(line.getLastDownStationId());

        doReturn(line)
                .when(lineService)
                .findLineById(lineId);

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
        final Line line = lineWithSection();
        final SectionRequest sectionRequest = sectionRequest(line.getLastDownStationId(), 2022L);

        doReturn(line)
                .when(lineService)
                .findLineById(lineId);

        // when
        final SectionResponse result = target.addSection(lineId, sectionRequest);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void 구간삭제실패_신규하행역이이미등록되어있음() {
        // given
        final Line line = lineWithSection();
        final Long stationId = 101L;

        doReturn(line)
                .when(lineService)
                .findLineById(lineId);

        // when
        final IllegalStateException result = assertThrows(
                IllegalStateException.class,
                () -> target.deleteSection(lineId, stationId));

        // then
        assertThat(result.getMessage()).isEqualTo("해당 역을 삭제할 수 없습니다.");
    }

}