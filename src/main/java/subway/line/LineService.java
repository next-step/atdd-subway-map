package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.*;
import subway.station.Station;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final SectionService sectionService;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, SectionService sectionService, StationService stationService) {
        this.sectionService = sectionService;
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName()
                , lineRequest.getColor()));
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        Section section = new Section(upStation, downStation, lineRequest.getDistance(), line);
        Section savedSection = sectionService.saveSectionByEntity(section);

        line.addSection(savedSection);
        return createLineResponse(line);
    }

    public List<LineResponse> findAllSubwayLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findSubwayLine(Long id) {
        Line line = findSubwayLineEntity(id);
        return createLineResponse(line);
    }

    public Line findSubwayLineEntity(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional
    public void updateSubwayLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = findSubwayLineEntity(id);
        line.updateName(lineUpdateRequest.getName());
        line.updateColor(lineUpdateRequest.getColor());
    }

    @Transactional
    public void delete(Long id) { lineRepository.deleteById(id); }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 lineId를 다시 확인해주세요."));
        SectionResponse sectionResponse = sectionService.saveSection(sectionRequest);
        Section section = sectionService.findById(sectionResponse.getId());
        line.setLineInSection(section);
        line.beforeAddSection(section);
        line.addSection(section);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findSubwayLineEntity(lineId);
        List<Section> sections = line.getSections();
        if (sections.size() < 2) {
            throw new IllegalArgumentException("해당 노선의 구간은 2개 미만입니다.");
        }

        Station station = stationService.findStationById(stationId);
        boolean result = line.deleteSectionByStation(station);
        if (!result) {
            throw new IllegalArgumentException("요청한 역이 하행 종점역이 맞는지 다시 확인해주세요.");
        }
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                createStationResponses(line)
        );
    }

    private List<StationResponse> createStationResponses(Line line) {
        if (line.getSections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = line.getSections().stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        stations.add(0, line.getSections().get(0).getUpStation());

        return stations.stream()
                .map(it -> stationService.createStationResponse(it))
                .collect(Collectors.toList());
    }
}