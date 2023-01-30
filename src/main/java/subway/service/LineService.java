package subway.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

@Service
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public LineService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance()));

        return LineResponse.createLineResponse(line);
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
            .map(LineResponse::createLineResponse)
            .collect(toList());
    }

    public LineResponse findLine(Long lineId) {
        return lineRepository.findById(lineId)
            .map(LineResponse::createLineResponse)
            .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);

        Line updatedLine = line.updateLine(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.createLineResponse(lineRepository.save(updatedLine));
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void createSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        Station upStation = stationService.findStation(sectionRequest.getUpStationId());
        Station downStation = stationService.findStation(sectionRequest.getDownStationId());

        line.addSection(new Section(line, upStation, downStation, sectionRequest.getDistance()));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        line.removeSection(stationId);
    }
}
