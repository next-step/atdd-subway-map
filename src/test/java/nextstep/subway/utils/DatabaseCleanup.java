package nextstep.subway.utils;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    @Transactional
    public void execute() {
        this.stationRepository.deleteAll();
        this.lineRepository.deleteAll();
    }
}