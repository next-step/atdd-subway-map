package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import subway.station.Station;
import subway.station.StationService;

@Service
@Transactional(readOnly = true)
public class SubwaySectionService {

    private final SubwaySectionRepository subwaySectionRepository;
    private final StationService stationService;

    public SubwaySectionService(SubwaySectionRepository subwaySectionRepository, StationService stationService) {
        this.subwaySectionRepository = subwaySectionRepository;
        this.stationService = stationService;
    }

    @Transactional
    public SubwaySectionResponse createSubwaySection(@RequestBody SubwaySectionRequest subwaySectionRequest) {
        Station upStation = stationService.findStationById(subwaySectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(subwaySectionRequest.getDownStationId());

        SubwaySection section = new SubwaySection(upStation, downStation);
        subwaySectionRepository.save(section);

        return createSectionResponse(section);
    }

    private SubwaySectionResponse createSectionResponse(SubwaySection section) {
        return new SubwaySectionResponse(
                section.getId()
        );
    }
}