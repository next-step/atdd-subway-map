package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.SubwayLine;
import subway.line.SubwayLineService;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;

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
        List<SubwaySection> sections = line.getSections();
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        SubwaySection section = new SubwaySection(upStation, downStation, line);

        if (sections.size() == 0) {
            subwayLineService.updateSections(lineId, section);
        }

        /**
         * FIX - 42 line
         * section 객체에 담기는 distance 거리 값이 정상적이지 않은 상태
         */
        section.calculateAndInsertDistance(sectionRequest.getDistance());
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

        if (savedUpSection.getId() != sectionRequest.getUpStationId()) {
            System.out.println("기존 하행 종점 역과 요청 상행 종점역이 맞지 않습니다.");
        }

        SubwayLine line = savedUpSection.getLine();
        subwayLineService.updateSections(line.getId(), newSection);
        return createSectionResponse(newSection);
    }

    @Transactional
    public SubwaySectionResponse deleteSection(Long lineId) {
        SubwayLine line = subwayLineService.findSubwayLineEntity(lineId);

        List<SubwaySection> sections = line.getSections();
        if (sections.size() < 2) {
            System.out.println("구간의 개수가 1개이기에 삭제할 수 없습니다.");
            System.out.println("size : " + sections.size());
        }

        sections.remove(sections.size() - 1);
        return createSectionResponse(sections.get(sections.size() - 1));
    }

    private SubwaySectionResponse createSectionResponse(SubwaySection section) {
        return new SubwaySectionResponse(
                section.getId()
        );
    }
}