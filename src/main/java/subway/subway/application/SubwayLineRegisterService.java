package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.SubwayLineRegisterUsecase;
import subway.subway.application.out.StationListLoadByIdInPort;
import subway.subway.application.out.SubwayLineRegisterPort;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.Station;
import subway.subway.domain.SubwayLine;
import subway.subway.domain.SubwaySection;

import java.util.List;

@Service
@Transactional
class SubwayLineRegisterService implements SubwayLineRegisterUsecase {

    private final SubwayLineRegisterPort subwayLineRegisterPort;
    private final StationListLoadByIdInPort stationListLoadByIdInPort;

    public SubwayLineRegisterService(SubwayLineRegisterPort subwayLineRegisterPort, StationListLoadByIdInPort stationListLoadByIdInPort) {
        this.subwayLineRegisterPort = subwayLineRegisterPort;
        this.stationListLoadByIdInPort = stationListLoadByIdInPort;
    }

    @Override
    public SubwayLineResponse registerSubwayLine(Command command) {
        List<Station> stations = stationListLoadByIdInPort.findAllIn(List.of(command.getUpStationId(), command.getDownStationId()));
        Station upStation = this.getStationBy(stations, command.getUpStationId());
        Station downStation = this.getStationBy(stations, command.getDownStationId());

        SubwaySection subwaySection = SubwaySection.register(upStation, downStation, command.getDistance());
        SubwayLine subwayLine = SubwayLine.register(command.getName(), command.getColor(), subwaySection);

        return subwayLineRegisterPort.register(subwayLine);
    }

    private Station getStationBy(List<Station> stations, Station.Id id) {
        return stations
                .stream()
                .filter(station -> station.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }
}
