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

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
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
    void 노선을생성한다() {
        final LineRequest lineRequest = lineRequest();
        doReturn(savedLine())
                .when(lineRepository)
                .save(any(Line.class));
        doReturn(Collections.emptyList())
                .when(stationService)
                .findStations(Arrays.asList(lineRequest.getUpStationId(), lineRequest.getDownStationId()));

        final LineResponse response = target.createLine(lineRequest);

        assertThat(response).isNotNull();
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