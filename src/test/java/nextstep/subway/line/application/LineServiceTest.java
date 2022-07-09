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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

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
        final Line line = savedLine();
        doReturn(line)
                .when(lineRepository)
                .save(any(Line.class));
        doReturn(Collections.emptyList())
                .when(stationService)
                .findStations(Arrays.asList(line.getUpStationId(), line.getDownStationId()));

        final LineResponse response = target.createLine(lineRequest);

        assertThat(response).isNotNull();
        verify(lineRepository, times(1)).save(any(Line.class));
    }

    @Test
    void 노선목록조회() {
        final Line line = savedLine();

        doReturn(List.of(line))
                .when(lineRepository)
                .findAll();
        doReturn(Collections.emptyList())
                .when(stationService)
                .findStations(Arrays.asList(line.getUpStationId(), line.getDownStationId()));

        final List<LineResponse> result = target.findAllLines();

        assertThat(result).hasSize(1);
    }

    @Test
    void 노선수정_노선이없으면에러반환() {
        final Line line = savedLine();
        final NoSuchElementException result = assertThrows(NoSuchElementException.class, () -> target.modifyLine(line.getId(), lineRequest()));

        assertThat(result).hasMessageContaining("해당 노선이 존재하지 않습니다.");
    }

    @Test
    void 노선수정() {
        final Line line = savedLine();

        doReturn(Optional.of(line))
                .when(lineRepository)
                .findById(line.getId());

        target.modifyLine(line.getId(), lineRequest());
        verify(lineRepository, times(1)).save(any(Line.class));
    }


    private LineRequest lineRequest() {
        return LineRequest.builder()
                .name("lineName")
                .color("bg-red-600")
                .upStationId(1L)
                .downStationId(2L)
                .distance(10L)
                .build();
    }

    private Line savedLine() {
        final Line line = lineRequest().toLine();
        ReflectionTestUtils.setField(line, "id", 1L);
        return line;
    }

}