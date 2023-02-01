package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionRequest;
import subway.section.SectionService;
import subway.station.Station;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final StationService stationService;
    private final SectionService sectionService;
    private final LineRepository lineRepository;

    public LineService(StationService stationService, SectionService sectionService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findOne(lineRequest.getUpStationId());
        Station downStation = stationService.findOne(lineRequest.getDownStationId());

        Line line = new Line(lineRequest, upStation, downStation);
        Line savedLine = lineRepository.save(line);

        return createLineResponse(savedLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findOneLine(Long id) {
        Line line = findOne(id);
        return createLineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findOne(id);
        line.update(lineRequest.getName(), lineRequest.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findOne(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 노선 입니다"));
    }

    @Transactional
    public void appendSection(Long id, SectionRequest sectionRequest) {
        Line line = findOne(id);

        if (!sectionService.isAppendable(line, sectionRequest)) {
            throw new IllegalArgumentException("등록할 수 없는 구간입니다");
        }

        Section section = this.sectionService.createSection(sectionRequest);

        line.appendSection(section);
        this.lineRepository.save(line);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationID) {
        Line line = findOne(lineId);

        if(!this.sectionService.isDeletable(line, stationID)) {
            throw new IllegalArgumentException("제거할 수 없는 구간입니다");
        }

        line.removeSection();
        this.lineRepository.save(line);
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stationResponses = line.getSections().stream()
                .map((s) -> stationService.createStationResponse(s.getUpStation()))
                .collect(Collectors.toList());

        stationResponses.add(stationService.createStationResponse(line.getDownStation()));

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }
}
