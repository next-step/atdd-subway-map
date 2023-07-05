package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.StationCloseUsecase;
import subway.subway.application.out.StationClosePort;

@Service
@Transactional
class StationCloseService implements StationCloseUsecase {
    private final StationClosePort stationCloseport;

    StationCloseService(StationClosePort stationCloseport) {
        this.stationCloseport = stationCloseport;
    }

    @Override
    public void closeStation(StationCloseUsecase.Command command) {
        stationCloseport.closeStation(command.getId());
    }

}
