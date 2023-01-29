package subway.station;

import common.JpaRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

class StationRepositoryTest extends JpaRepositoryTest<Station, Long> {

    @Autowired
    private StationRepository stationRepository;

    @Override
    protected JpaRepository<Station, Long> repository() {
        return stationRepository;
    }

    @Override
    protected Station createTestInstance() {
        return new Station("station");
    }
}
