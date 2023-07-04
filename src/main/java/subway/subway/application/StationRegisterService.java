package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.StationRegisterUsecase;
import subway.subway.application.in.command.StationRegisterCommand;
import subway.subway.application.out.StationRegisterPort;
import subway.subway.application.query.StationResponse;
import subway.rds_module.entity.StationJpa;
import subway.subway.domain.Station;

@Service
@Transactional
class StationRegisterService implements StationRegisterUsecase {
    private final StationRegisterPort stationRegisterPort;
    private final StationMapper stationMapper;

    public StationRegisterService(StationRegisterPort stationRegisterPort, StationMapper stationMapper) {
        this.stationRegisterPort = stationRegisterPort;
        this.stationMapper = stationMapper;
    }

    @Override
    public StationResponse saveStation(StationRegisterCommand command) {

        Station station = Station.register(stationMapper.toMap(command));
        return stationRegisterPort.save(station);
    }
}
