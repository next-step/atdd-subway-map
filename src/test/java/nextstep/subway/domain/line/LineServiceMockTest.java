package nextstep.subway.domain.line;

import nextstep.subway.domain.line.dto.LineDetailResponse;
import nextstep.subway.domain.line.dto.LineRequest;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.section.dto.SectionDetailResponse;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.domain.factory.DtoFactory.createLineRequest;
import static nextstep.subway.domain.factory.EntityFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("노선 비즈니스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationRepository stationRepository;

    private LineService lineService;

    @BeforeEach
    void init() {
        lineService = new LineService(lineRepository, stationRepository, sectionRepository);
    }

    @DisplayName("노선 생성을 처리한다.")
    @Test
    void saveLine() {
        // given
        Station 강남역 = createStation("강남역");
        Long 강남역Id = 1L;
        Station 선릉역 = createStation("선릉역");
        Long 선릉역Id = 2L;
        int 강남_선릉_거리 = 10;

        LineRequest lineRequest = createLineRequest("2호선", "green", 강남역Id, 선릉역Id, 강남_선릉_거리);
        Line lineResult = createLine(lineRequest.getName(), lineRequest.getColor());

        // stubbing
        when(stationRepository.findById(강남역Id)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(선릉역Id)).thenReturn(Optional.of(선릉역));
        when(lineRepository.save(any())).thenReturn(lineResult);

        // when
        lineService.saveLine(lineRequest);

        // then
        assertThat(lineResult.getName()).isEqualTo(lineRequest.getName());
        assertThat(lineResult.getColor()).isEqualTo(lineRequest.getColor());
    }

    @DisplayName("노선 목록을 조회한다.")
    @Test
    void getLineList() {
        // given
        Line line = createLine("2호선", "green");

        // stubbing
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line));

        // when
        List<LineDetailResponse> lineList = lineService.getLineList();

        // then
        assertThat(lineList.get(0).getName()).isEqualTo(line.getName());
    }

    @DisplayName("노선 하나를 조회한다.")
    @Test
    void getLine() {
        // given
        Line line = createLine("2호선", "green");

        // stubbing
        when(lineRepository.findById(any())).thenReturn(Optional.of(line));

        // when
        LineDetailResponse lineResponse = lineService.getLine(1L);

        // then
        assertThat(lineResponse.getName()).isEqualTo(line.getName());
    }

    @DisplayName("노선을 수정한다.")
    @Test
    void patchLine() {
        // given
        Line line = createLine("2호선", "green");

        // stubbing
        when(lineRepository.findById(any())).thenReturn(Optional.of(line));

        // when
        lineService.patchLine(1L, "3호선", "orange");

        // then
        assertThat(line.getName()).isEqualTo("3호선");
        assertThat(line.getColor()).isEqualTo("orange");
    }

    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteLine() {
        // when
        lineService.deleteLine(1L);
    }

    @DisplayName("노선에 구간을 추가한다.")
    @Test
    void insertSection() {
        Long 강남역Id = 1L;
        Station 강남역 = createStation("강남역");
        Long 역삼역Id = 2L;
        Station 역삼역 = createStation("역삼역");
        int 강남_역삼_거리 = 10;
        Line line = createLine("2호선", "green");

        // stubbing
        when(lineRepository.findById(any())).thenReturn(Optional.of(line));
        when(stationRepository.findById(강남역Id)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(역삼역Id)).thenReturn(Optional.of(역삼역));

        // when
        lineService.insertSection(1L, 강남역Id, 역삼역Id, 강남_역삼_거리);
    }

    @DisplayName("노선의 구간 목록을 조회한다.")
    @Test
    void getSections() {
        // given
        Station 강남역 = createStation("강남역");
        Station 역삼역 = createStation("역삼역");
        Station 선릉역 = createStation("선릉역");
        int 강남_역삼_거리 = 10;
        int 역삼_선릉_거리 = 8;
        Line 이호선 = createCompleteLine("2호선", "green", 강남역, 역삼역, 강남_역삼_거리);
        이호선.addSection(createSection(역삼역, 선릉역, 역삼_선릉_거리));

        // stubbing
        when(lineRepository.findById(any())).thenReturn(Optional.of(이호선));

        // when
        List<SectionDetailResponse> sections = lineService.getSections(1L);

        // then
        assertThat(sections.get(0).getName()).isEqualTo("2호선");
        assertThat(sections.size()).isEqualTo(2);
    }
}