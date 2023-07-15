package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.SubwayLine;
import subway.line.SubwayLineService;
import subway.station.Station;
import subway.station.StationService;

@Service
@Transactional(readOnly = true)
public class SubwaySectionService {

    private final SubwaySectionRepository subwaySectionRepository;
    private final StationService stationService;
    private final SubwayLineService subwayLineService;

    public SubwaySectionService(SubwaySectionRepository subwaySectionRepository,
                                StationService stationService, SubwayLineService subwayLineService) {
        this.subwaySectionRepository = subwaySectionRepository;
        this.stationService = stationService;
        this.subwayLineService = subwayLineService;
    }

    @Transactional
    public SubwaySectionResponse createSubwaySection(Long lineId, SubwaySectionRequest sectionRequest) {
        SubwayLine line = subwayLineService.findSubwayLineEntity(lineId);
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());

        SubwaySection section = new SubwaySection(upStation, downStation, line);
        subwaySectionRepository.save(section);

        return createSectionResponse(section);
    }

    @Transactional
    public SubwaySectionResponse registerSubwaySection(SubwaySectionRequest sectionRequest) {
        Station requestUpStation = stationService.findStationById(sectionRequest.getUpStationId());
        SubwaySection savedUpSection = subwaySectionRepository.findByUpStation(requestUpStation);
        Long lineId = savedUpSection.getLine().getId();

        SubwaySectionResponse createNewSectionResponse = createSubwaySection(lineId, sectionRequest);
        SubwaySection newSection = subwaySectionRepository.findById(createNewSectionResponse.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        System.out.println("savedSectionId : " + savedUpSection.getId());
        System.out.println("sectionRequest.getUpStationId() : " + sectionRequest.getUpStationId());

        if (savedUpSection.getId() != sectionRequest.getUpStationId()) {
            System.out.println("기존 하행 종점 역과 요청 상행 종점역이 맞지 않습니다.");
        }

        SubwayLine line = savedUpSection.getLine();
        subwayLineService.updateSections(line.getId(), newSection);
        return createSectionResponse(newSection);
    }

    private SubwaySectionResponse createSectionResponse(SubwaySection section) {
        return new SubwaySectionResponse(
                section.getId()
        );
    }
}