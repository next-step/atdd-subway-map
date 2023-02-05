package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.request.LineRequest;
import subway.line.dto.request.SectionRequest;
import subway.line.dto.response.LineResponse;
import subway.line.dto.response.StationResponse;
import subway.line.entity.Line;
import subway.line.entity.Section;
import subway.line.entity.Station;
import subway.line.repository.LineRepository;
import subway.line.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(StationService stationService, LineRepository lineRepository, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        String name = lineRequest.getName();
        String color = lineRequest.getColor();
        Integer distance = lineRequest.getDistance();
        Station upStation = stationService.findOne(lineRequest.getUpStationId());
        Station downStation = stationService.findOne(lineRequest.getDownStationId());

        Line line = new Line(name, color, distance, upStation, downStation);
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
        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getDownStationId();
        Integer distance = sectionRequest.getDistance();

        Line line = findOne(id);

        if (!line.validateSectionCreation(upStationId, downStationId)) {
            throw new IllegalArgumentException("등록할 수 없는 구간입니다");
        }

        Station upStation = this.stationService.findOne(upStationId);
        Station downStation = this.stationService.findOne(downStationId);

        Section section = new Section(distance, upStation, downStation);
        this.sectionRepository.save(section);

        line.appendSection(section);
        this.lineRepository.save(line);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findOne(lineId);

        if(!line.validateSectionDeletion(stationId)) {
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
