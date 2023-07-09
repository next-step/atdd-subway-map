package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.ui.LineCreateRequest;
import subway.ui.LineResponse;
import subway.ui.LineUpdateRequest;
import subway.ui.SectionRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createLine(LineCreateRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(EntityNotFoundException::new);
        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), request.getDistance()));
        line.addSection(new Section(line.getId(), upStation, downStation, line.getDistance()));
        return new LineResponse(line.getId(), line.getName());
    }

    public List<LineResponse> showLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::from).collect(Collectors.toList());
    }

    public LineResponse showLine(Long lineId) {
        return lineRepository.findById(lineId).map(LineResponse::from)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public LineResponse updateLine(Long lineId, LineUpdateRequest request) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        line.update(request);
        return LineResponse.from(line);
    }

    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(EntityNotFoundException::new);
        Section section = new Section(lineId, upStation, downStation, request.getDistance());
        line.addSection(section);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        line.deleteSection(stationId);
    }
}
