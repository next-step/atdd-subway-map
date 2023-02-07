package subway.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.api.dto.LineRequest;
import subway.api.dto.LineResponse;
import subway.api.dto.SectionRequest;
import subway.domain.entity.Line;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.repository.LineRepository;
import subway.global.error.exception.EntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static subway.global.error.exception.ErrorCode.LINE_NOT_EXISTS;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                createStations(line)
        );
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return lineRepository.findById(id).stream()
                .map(this::createLineResponse)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(LINE_NOT_EXISTS));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line findedLine = lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(LINE_NOT_EXISTS));
        findedLine.updateLineIfPresent(lineRequest.toLineEntity(lineRequest));
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private List createStations(Line line) {
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

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException(LINE_NOT_EXISTS));
        line.addSection(sectionRequest, upStation, downStation);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException(LINE_NOT_EXISTS));
        Station station = stationService.findById(stationId);
        line.removeSection(station);
    }
}
