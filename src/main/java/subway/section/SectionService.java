package subway.section;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.exception.LineException;
import subway.line.service.LineDataService;
import subway.station.Station;
import subway.station.StationDataService;

import javax.transaction.Transactional;

@Transactional
@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    private final LineDataService lineDataService;

    private final StationDataService stationDataService;

    public SectionService(SectionRepository sectionRepository, LineDataService lineDataService, StationDataService stationDataService) {
        this.sectionRepository = sectionRepository;
        this.lineDataService = lineDataService;
        this.stationDataService = stationDataService;
    }

    public void saveSection(Long lineId, SectionCreateRequest request) {
        Station upStation = stationDataService.findStation(request.getUpStationId());
        Station downStation = stationDataService.findStation(request.getDownStationId());

        Line line = lineDataService.findLine(lineId);

        Section section = new Section(request.getDistance(), upStation, downStation, line);

        line.generateSection(section);

        sectionRepository.save(section);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineDataService.findLine(lineId);

        Section deleteSection = line.deleteSection(stationId);

        sectionRepository.delete(deleteSection);
    }
}
