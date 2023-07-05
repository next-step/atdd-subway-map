package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.StationRegisterUsecase;
import subway.subway.application.in.command.StationRegisterCommand;
import subway.subway.application.out.StationRegisterPort;
import subway.subway.application.query.StationResponse;
import subway.subway.domain.Station;

@Service
@Transactional
class StationRegisterService implements StationRegisterUsecase {
    private final StationRegisterPort stationRegisterPort;

    public StationRegisterService(StationRegisterPort stationRegisterPort) {
        this.stationRegisterPort = stationRegisterPort;
    }

    @Override
    public StationResponse saveStation(StationRegisterCommand command) {

        Station station = Station.register(command.getName());
        return stationRegisterPort.save(station);
    }
}
