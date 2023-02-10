package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineResponse;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional
    public LineResponse createSection(Long lindId, SectionRequest request) {
        Line line = lineService.findById(lindId);
        Station upStation = stationService.findStationByStationId(request.getUpStationId());
        Station downStation = stationService.findStationByStationId(request.getDownStationId());

        Section section = new Section(line, upStation, downStation, request.getDistance());

        line.createSection(section);

        return lineService.createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> getSection(Long id) {
        return lineService.findById(id).getSections().getSections().stream()
                .map(section -> createSectionResponse(section))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineService.findById(lineId);
        line.deleteSection(stationId);
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(
                section.getDownStation().getId(),
                section.getUpStation().getId(),
                section.getDistance());
    }
}
