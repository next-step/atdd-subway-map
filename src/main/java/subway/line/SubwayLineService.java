package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.SubwaySection;
import subway.section.SubwaySectionService;
import subway.station.Station;
import subway.station.StationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private SubwayLineRepository subwayLineRepository;
    private StationService stationService;
    private SubwaySectionService subwaySectionService;

    public SubwayLineService(SubwayLineRepository subwayLineRepository,
            StationService stationService, SubwaySectionService subwaySectionService) {
        this.subwayLineRepository = subwayLineRepository;
        this.stationService = stationService;
        this.subwaySectionService = subwaySectionService;
    }

    @Transactional
    public SubwayLineResponse createLine(SubwayLineRequest subwayLineRequest) {
        Station upStation = stationService.findStationById(subwayLineRequest.getUpStationId());
        Station downStation = stationService.findStationById(subwayLineRequest.getDownStationId());
        SubwaySection section = subwaySectionService.saveSubwaySection(upStation, downStation);

        List<SubwaySection> sections = new ArrayList<>();
        sections.add(section);

        SubwayLine subwayLine = subwayLineRepository.save(new SubwayLine(subwayLineRequest.getName()
                , subwayLineRequest.getColor(), sections));

        return createLineResponse(subwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findSubwayLine(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow();
        return createLineResponse(subwayLine);
    }

    @Transactional
    public void updateSubwayLine(Long id, SubwayLineUpdateRequest subwayLineUpdateRequest) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow();

        subwayLine.updateName(subwayLineUpdateRequest.getName());
        subwayLine.updateColor(subwayLineUpdateRequest.getColor());
    }


    @Transactional
    public void delete(Long id) { subwayLineRepository.deleteById(id); }

    private SubwayLineResponse createLineResponse(SubwayLine subwayLine) {
        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                subwayLine.getSections()
        );
    }
}