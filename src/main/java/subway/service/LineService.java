package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.StationResponse;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

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
        Station downStation = stationService.findById(lineRequest.getDownStationId());
        Station upStation = stationService.findById(lineRequest.getUpStationId());

        Section section = sectionRepository.save(new Section(
                downStation,
                upStation,
                lineRequest.getDistance()
                )
        );

        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                section
                )
        );

        return createLineResponse(line);
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

    private LineResponse createLineResponse(Line line) {

        Station downStation = line.getDownStation();
        Station upStation = line.getUpStation();

        StationResponse downStationResponse = createStationResponse(downStation);
        StationResponse upStationResponse = createStationResponse(upStation);

        return new LineResponse(line, List.of(downStationResponse, upStationResponse));
    }
}
