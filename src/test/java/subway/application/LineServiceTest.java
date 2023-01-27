package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("지하철 노선 응용 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    @DisplayName("호선을 생성한다.")
    @Test
    void createLine() {

        final LineRequest 요청_호선 = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10);
        final Station 상행종점역 = new Station(1L, "강남역");
        final Station 하행종점역 = new Station(2L, "잠실역");
        final Line 호선 = new Line(1L, "2호선", "bg-red-600", 상행종점역, 하행종점역, 10);

        when(stationService.findById(anyLong())).thenReturn(상행종점역)
                .thenReturn(하행종점역);
        when(lineRepository.save(any())).thenReturn(호선);

        final LineResponse 응답_호선 = lineService.createLine(요청_호선);

        assertAll(
                () -> assertThat(응답_호선.getId()).isNotNull(),
                () -> assertThat(응답_호선.getName()).isEqualTo("2호선"),
                () -> assertThat(응답_호선.getColor()).isEqualTo("bg-red-600"),
                () -> assertThat(응답_호선.getStationResponses()).hasSize(2)
        );
    }

    @DisplayName("호선 목록을 조회한다.")
    @Test
    void showLines() {

        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 상행종점_강남역, 하행종점_잠실역, 10);

        final Station 하행종점_몽총토성역 = new Station(2L, "몽촌토성역");
        final Line 신분당선 = new Line(2L, "신분당선", "bg-green-600", 상행종점_강남역, 하행종점_몽총토성역, 10);

        when(lineRepository.findAll()).thenReturn(List.of(이호선, 신분당선));

        final List<LineResponse> 응답_호선_목록 = lineService.showLines();

        assertThat(응답_호선_목록).hasSize(2);
    }

    @DisplayName("특정 호선을 조회한다.")
    @Test
    void findLine() {

        final long 조회_호선 = 1L;
        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 상행종점_강남역, 하행종점_잠실역, 10);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));

        final LineResponse 응답_호선 = lineService.findLine(조회_호선);

        assertAll(
                () -> assertThat(응답_호선.getId()).isEqualTo(1L),
                () -> assertThat(응답_호선.getName()).isEqualTo("2호선"),
                () -> assertThat(응답_호선.getColor()).isEqualTo("bg-red-600"),
                () -> assertThat(응답_호선.getStationResponses()).hasSize(2)
        );
    }

    @DisplayName("호선을 수정한다.")
    @Test
    void updateLine() {

        final long 요청_조회_호선 = 1L;
        final LineRequest 요청_수정_호선 = new LineRequest("2호선", "bg-red-600");
        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 상행종점_강남역, 하행종점_잠실역, 10);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));

        lineService.updateLine(요청_조회_호선, 요청_수정_호선);

        assertAll(
                () -> assertThat(이호선.getName()).isEqualTo("2호선"),
                () -> assertThat(이호선.getColor()).isEqualTo("bg-red-600")
        );
    }

    @DisplayName("호선을 삭제한다.")
    @Test
    void deleteLine() {

        final long 요청_삭제_호선 = 1L;

        doNothing().when(lineRepository).deleteById(anyLong());

        lineService.deleteLine(요청_삭제_호선);

        final InOrder inOrder = inOrder(lineRepository);
        inOrder.verify(lineRepository, times(1)).deleteById(anyLong());
    }
}


