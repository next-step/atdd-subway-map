package subway.line.addsection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.section.Sections;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineAddSectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineAddSectionService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineAddedSectionResponse addSection(Long lineId, LineAddSectionRequest request) {
        Line line = findLineByLineId(lineId);
        Sections sections = findSectionsByLine(line);
        Station upStation = findStationByStationId(request.getUpStationId());
        Station downStation = findStationByStationId(request.getDownStationId());
        Section section = new Section(line, upStation, downStation, request.getDistance());
        sections.addSection(section);
        sectionRepository.save(sections.getLastSection());
        return mapToResponse(line, sections);
    }

    private Line findLineByLineId(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다. lineId: " + lineId));
    }

    private Sections findSectionsByLine(Line line) {
        return new Sections(sectionRepository.findAllByLineIdOrderById(line.getId()));
    }

    private Station findStationByStationId(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다. stationId: " + stationId));
    }

    private LineAddedSectionResponse mapToResponse(Line line, Sections sections) {
        List<Station> stations = sections.getAllStations();
        return new LineAddedSectionResponse(line.getId(), line.getName(), line.getColor(), mapToStationResponses(stations));
    }

    private List<LineAddedSectionStationResponse> mapToStationResponses(List<Station> stations) {
        return stations.stream()
                .map(station -> new LineAddedSectionStationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
