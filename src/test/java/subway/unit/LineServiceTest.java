package subway.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.Station;
import subway.domain.entity.Line;
import subway.domain.request.SectionRequest;
import subway.repository.StationRepository;
import subway.repository.LineRepository;
import subway.service.LineService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LineServiceTest {
    @Autowired
    LineRepository lineRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    LineService lineService;

    @Test
    void addSection() {

        // given
        // 초기 세팅
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));
        Station 선릉역 = stationRepository.save(new Station("선릉역"));

        Line line = new Line("이호선", "초록색");
        line.addSection(강남역, 역삼역, 10);
        lineRepository.save(line);

        // when
        // line.addSection() 호출
        lineService.addSection(line.getId(), new SectionRequest(역삼역.getId(), 선릉역.getId(), 10));

        // then
        // 검증
        assertThat(line.getSections()).hasSize(2);

    }
}
