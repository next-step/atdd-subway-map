package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final LineService lineService;
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public SectionService(LineService lineService, SectionRepository sectionRepository, StationService stationService) {
        this.lineService = lineService;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    @Transactional
    public SectionResponse createSection(long line_id, SectionCreateRequest request) {
        Line line = lineService.findOneById(line_id);
        List<Station> stations = line.getStations();
        Station lastStation = stations.get(stations.size() - 1);
        if (request.getUpStationId() != lastStation.getId()) {
            throw new IllegalArgumentException();
        }
        if (stations.stream().anyMatch(station ->
                request.getDownStationId() == station.getId())) {
            throw new IllegalArgumentException();
        }

        Station downStation = stationService.findStation(request.getDownStationId());
        line.addStation(downStation);
        line.plusDistance(request.getDistance());

        Section section = new Section(request.getUpStationId(), request.getDownStationId(), line.getDistance());
        section = sectionRepository.save(section);
        return new SectionResponse(section.getId(), section.getUpStationId(), section.getDownStationId(), section.getDistance());
    }
}
