package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.*;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

import static subway.service.StationService.createStationResponse;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationService stationService;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(StationService stationService, LineRepository lineRepository,
                       SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Section section = createSection(lineRequest);

        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                section
                )
        );

        return createLineResponse(line);
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionRequest request) {
        Section section = createSection(request);
        Line line = getLineById(lineId);
        line.addSection(section);
        return new SectionResponse(
                section.getId(),
                List.of(createStationResponse(section.getDownStation()),
                        createStationResponse(section.getUpStation()))
        );
    }

    public Section createSection(SectionCreateReader request) {
        Station downStation = stationService.findById(request.getDownStationId());
        Station upStation = stationService.findById(request.getUpStationId());

        Section section = sectionRepository.save(new Section(
                downStation,
                upStation,
                request.getDistance()
                )
        );
        return section;
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        return createLineResponse(getLineById(id));
    }

    public Line getLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLineById(lineId);
        Section section = line.deleteSection(stationId);
        sectionRepository.delete(section);
    }

    private LineResponse createLineResponse(Line line) {

        Station downStation = line.getDownStation();
        Station upStation = line.getUpStation();

        StationResponse downStationResponse = createStationResponse(downStation);
        StationResponse upStationResponse = createStationResponse(upStation);

        return new LineResponse(line, List.of(downStationResponse, upStationResponse));
    }
}
