package nextstep.subway.line.application;

import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.applicaion.StationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static nextstep.subway.line.LineTestSource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService target;

    @Mock
    private StationService stationService;
    @Mock
    private LineRepository lineRepository;

    @Test
    void 노선생성() {
        final LineRequest lineRequest = lineRequest();
        final Line line = lineWithSection();
        
        doReturn(line)
                .when(lineRepository)
                .save(any(Line.class));
        doReturn(Collections.emptyList())
                .when(stationService)
                .findStations(line.getFirstAndLastStationId());

        final LineResponse response = target.createLine(lineRequest);

        assertThat(response).isNotNull();
        verify(lineRepository, times(1)).save(any(Line.class));
    }

    @Test
    void 노선조회() {
        final Line line = lineWithSection();

        doReturn(Optional.of(line))
                .when(lineRepository)
                .findById(line.getId());

        final LineResponse result = target.findLine(line.getId());

        assertThat(result).isNotNull();
    }

    @Test
    void 노선목록조회() {
        final Line line = lineWithSection();

        doReturn(List.of(line))
                .when(lineRepository)
                .findAll();
        doReturn(Collections.emptyList())
                .when(stationService)
                .findStations(line.getFirstAndLastStationId());

        final List<LineResponse> result = target.findAllLines();

        assertThat(result).hasSize(1);
    }

    @Test
    void 노선수정_노선이없으면에러반환() {
        final Line line = lineWithSection();
        final NoSuchElementException result = assertThrows(NoSuchElementException.class, () -> target.modifyLine(line.getId(), lineRequest()));

        assertThat(result).hasMessageContaining("해당 노선이 존재하지 않습니다.");
    }

    @Test
    void 노선수정() {
        final Line line = line();

        doReturn(Optional.of(line))
                .when(lineRepository)
                .findById(line.getId());

        target.modifyLine(line.getId(), lineRequest());
    }

    @Test
    void 노선삭제_노선이없으면에러반환() {
        final Line line = line();
        final NoSuchElementException result = assertThrows(NoSuchElementException.class, () -> target.deleteLine(line.getId()));

        assertThat(result).hasMessageContaining("해당 노선이 존재하지 않습니다.");
    }

    @Test
    void 노선삭제() {
        final Line line = line();

        doReturn(Optional.of(line))
                .when(lineRepository)
                .findById(line.getId());

        target.deleteLine(line.getId());

        verify(lineRepository, times(1)).deleteById(line.getId());
    }

}