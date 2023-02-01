package subway.line.station;


import common.JpaRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.Line;
import subway.line.LineDataSet;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@DisplayName("LineStationRepository 단위 테스트")
class LineStationRepositoryTest extends JpaRepositoryTest<LineStation, Long> {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineStationRepository lineStationRepository;

    @BeforeEach
    void beforeEach() {

    }

    @Override
    protected JpaRepository<LineStation, Long> repository() {
        return lineStationRepository;
    }

    @Override
    protected LineStation createTestInstance() {
        Line line = lineRepository.save(LineDataSet.testData());
        Station station = stationRepository.save(StationDataSet.testData());
        return LineStationDataSet.testData(line, station);
    }
}
