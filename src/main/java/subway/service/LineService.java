package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.Line;
import subway.domain.entity.Station;
import subway.domain.request.LineRequest;
import subway.domain.request.SectionRequest;
import subway.domain.response.LineResponse;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station dwonStation = findStationById(request.getDownStationId());

        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, dwonStation, request.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).get();

        return createLineResponse(line);

    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).get();
        Line newLine = new Line(line.getId(), request.getName(), request.getColor(), line.getUpStation(), line.getDownStation(), line.getDistance(), null);

        Line updatedLine = lineRepository.save(newLine);
        return createLineResponse(updatedLine);

    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public Station findStationById(Long id) {
        return this.stationService.findById(id);
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = findStationById(line.getUpStation().getId());
        Station downStation = findStationById(line.getDownStation().getId());

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(upStation, downStation),
                line.getDistance()
        );
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).get();
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());

        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }
}
