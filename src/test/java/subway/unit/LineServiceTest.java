package subway.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.application.LineService;
import subway.application.dto.SectionCreateRequest;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LineServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        // given
        // stationRepository 와 LineRepository 를 사용하여 역과 노선을 초기 세팅한다.
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));
        Station 선릉역 = stationRepository.save(new Station("선릉역"));

        Line line = new Line("이호선", "green");
        line.addSection(강남역, 역삼역, 10);
        lineRepository.save(line);

        // when
        // LineService 의 addSection 을 호출했을 때
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(역삼역.getId(), 선릉역.getId(), 10);
        lineService.addSection(line.getId(), sectionCreateRequest);

        // then
        // line.getSections 메서드를 통해 검증한다.
        assertThat(line.getSections()).hasSize(2);
    }

    @Test
    void deleteSection() {
        // given
        // stationRepository 와 LineRepository 를 사용하여 역과 노선을 초기 세팅한다.
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));
        Station 선릉역 = stationRepository.save(new Station("선릉역"));

        Line line = new Line("이호선", "green");
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 선릉역, 10);
        lineRepository.save(line);

        // when
        // LineService 의 deleteSection 을 호출했을 때
        lineService.deleteSection(line.getId(), 선릉역.getId());

        // then
        // line.getSections 메서드를 통해 검증한다.
        assertThat(line.getSections()).hasSize(1);
    }

}