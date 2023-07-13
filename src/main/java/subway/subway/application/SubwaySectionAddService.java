package subway.subway.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.SubwaySectionAddUsecase;
import subway.subway.application.out.StationListLoadByIdInPort;
import subway.subway.application.out.SubwayLineLoadPort;
import subway.subway.application.out.SubwaySectionAddPort;
import subway.subway.domain.SectionOperateManager;
import subway.subway.domain.Station;
import subway.subway.domain.SubwayLine;
import subway.subway.domain.SubwaySection;

import java.util.List;

@Transactional
@Service
public class SubwaySectionAddService implements SubwaySectionAddUsecase {

    private final StationListLoadByIdInPort stationListLoadByIdInPort;
    private final SubwayLineLoadPort subwayLineLoadPort;
    private final SectionOperateManager sectionOperateManager;
    private final SubwaySectionAddPort subwaySectionAddPort;

    @Autowired
    public SubwaySectionAddService(StationListLoadByIdInPort stationListLoadByIdInPort, SubwayLineLoadPort subwayLineLoadPort, SectionOperateManager sectionOperateManager, SubwaySectionAddPort subwaySectionAddPort) {
        this.stationListLoadByIdInPort = stationListLoadByIdInPort;
        this.subwayLineLoadPort = subwayLineLoadPort;
        this.sectionOperateManager = sectionOperateManager;
        this.subwaySectionAddPort = subwaySectionAddPort;
    }

    @Override
    public void addSubwaySection(Command command) {
        List<Station> stations = stationListLoadByIdInPort.findAllIn(List.of(command.getUpStationId(), command.getDownStationId()));
        Station upStation = this.getStationBy(stations, command.getUpStationId());
        Station downStation = this.getStationBy(stations, command.getDownStationId());

        SubwaySection subwaySection = SubwaySection.register(upStation, downStation, command.getDistance());
        SubwayLine subwayLine = subwayLineLoadPort.findOne(command.getSubwayLineId());
        subwayLine.addSection(subwaySection, sectionOperateManager);
        subwaySectionAddPort.addSubwaySection(subwayLine);
    }

    private Station getStationBy(List<Station> stations, Station.Id id) {
        return stations
                .stream()
                .filter(station -> station.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }
}
