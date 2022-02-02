package nextstep.subway.domain.line;

import nextstep.subway.domain.factory.EntityFactory;
import nextstep.subway.domain.line.dto.LineRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.domain.factory.DtoFactory.createLineRequest;
import static nextstep.subway.domain.factory.DtoFactory.createSectionRequest;
import static nextstep.subway.domain.factory.EntityFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LineServiceTest {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineService lineService;

    @DisplayName("노선을 생성한다.")
    @Test
    void saveLine() {
        // 역 2개가 존재할때, 노선을 생성하면
        // 노선의 최상행과 최하행역이 정해지고, 구간이 1개 생성되면서 노선이 생성된다.

        // given
        Station 강남역 = createStation("강남역");
        stationRepository.save(강남역);
        Station 역삼역 = createStation("역삼역");
        stationRepository.save(역삼역);
        int 강남_역삼_거리 = 10;

        LineRequest request = createLineRequest("2호선", "green", 강남역.getId(), 역삼역.getId(), 강남_역삼_거리);

        // when
        LineResponse lineResponse = lineService.saveLine(request);

        // then
        Line line = lineRepository.getById(1L);
        assertThat(line.getSectionList().size()).isEqualTo(1);
    }

    @DisplayName("노선에 구간을 추가한다.")
    @Test
    void insertSection() {
        // given
        Station 강남역 = createStation("강남역");
        stationRepository.save(강남역);
        Station 역삼역 = createStation("역삼역");
        stationRepository.save(역삼역);
        Station 선릉역 = createStation("선릉역");
        stationRepository.save(선릉역);
        int 강남_역삼_거리 = 10;
        int 역삼_선릉_거리 = 8;

        Line 이호선 = EntityFactory.createLine("2호선", "green", 강남역, 역삼역, 강남_역삼_거리);
        lineRepository.save(이호선);

        // when
        lineService.insertSection(이호선.getId(), createSectionRequest(역삼역.getId(), 선릉역.getId(), 역삼_선릉_거리));

        // then
        assertThat(이호선.getSectionList().get(1).getUpStationId()).isEqualTo(역삼역.getId());
    }
}
