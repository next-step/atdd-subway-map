package subway;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.model.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {

    protected static Station GANG_NAM_STATION;
    protected static Station YEOK_SAM_STATION;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @BeforeEach
    void save() {
        lineRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
        GANG_NAM_STATION = stationRepository.save(new Station("강남역"));
        YEOK_SAM_STATION = stationRepository.save(new Station("역삼역"));
    }

}
