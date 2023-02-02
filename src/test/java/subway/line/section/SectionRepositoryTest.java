package subway.line.section;

import common.JpaRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.station.StationDataSet;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

@DisplayName("SectionRepository 단위 테스트")
public class SectionRepositoryTest extends JpaRepositoryTest<Section, Long> {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    protected JpaRepository<Section, Long> repository() {
        return sectionRepository;
    }

    @Override
    protected Section createTestInstance() {
        Station downStation = stationRepository.save(StationDataSet.testData("downStation"));

        Station upStation = stationRepository.save(StationDataSet.testData("upStation"));

        return SectionDataSet.testData(downStation, upStation, 20);
    }
}
