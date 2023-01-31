package subway.station;

import common.JpaRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

@DisplayName("지하철 역 관련 Repository 단위 테스트")
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
