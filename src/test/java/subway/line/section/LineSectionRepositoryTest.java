package subway.line.section;

import common.JpaRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.Line;
import subway.line.LineDataSet;
import subway.line.LineRepository;
import subway.line.station.StationDataSet;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

@DisplayName("지하철 노선과 구간 관계 관련 LineSectionRepository 단위 테스트")
public class LineSectionRepositoryTest extends JpaRepositoryTest<LineSection, Long> {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineSectionRepository lineSectionRepository;

    @Override
    protected JpaRepository<LineSection, Long> repository() {
        return lineSectionRepository;
    }

    @Override
    protected LineSection createTestInstance() {
        Line line = LineDataSet.testData("line", "color", 10);

        Station downStation = StationDataSet.testData("downStation");

        Station upStation = StationDataSet.testData("upStation");

        Section section = SectionDataSet.testData(downStation, upStation, 10);

        lineRepository.save(line);

        stationRepository.save(upStation);

        stationRepository.save(downStation);

        sectionRepository.save(section);

        return LineSectionDataSet.testData(line, section);
    }
}
