package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.SectionUpStationNotMatchException;
import subway.line.Line;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final LineService lineService;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

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
            throw new SectionUpStationNotMatchException();
        }
        if (stations.stream().anyMatch(station ->
                request.getDownStationId() == station.getId())) {
            throw new IllegalArgumentException();
        }
        Station downStation = stationService.findStation(request.getDownStationId());
        Section section = new Section(lastStation, downStation, line.getDistance(), line);

        line.addSection(section);
        line.plusDistance(request.getDistance());

        section = sectionRepository.save(section);
        return new SectionResponse(section.getId(), section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
    }

    @Transactional
    public void deleteSection(long lineId, long stationId) {
        Line line = lineService.findOneById(lineId);
        Station requestStation = stationService.findStation(stationId);
        Station lineDownStation = line.getDownStation();
        if (requestStation != lineDownStation || line.hasMinimumStations()) {
            throw new IllegalArgumentException();
        }
        Section section = line.removeLastSection();
        sectionRepository.delete(section);
    }
}
