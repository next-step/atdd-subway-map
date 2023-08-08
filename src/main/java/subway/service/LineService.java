package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.*;
import subway.entity.Line;
import subway.entity.Section;
import subway.entity.Station;
import subway.exception.SubwayException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;

    @Transactional
    public LineResponse saveLine(LineRequest req) {
        Station upStation = stationRepository.findById(req.getUpStationId()).orElseThrow(() -> new SubwayException("Resource not found"));
        Station downStation = stationRepository.findById(req.getDownStationId()).orElseThrow(() -> new SubwayException("Resource not found"));

        Line line = lineRepository.save(new Line(req.getName(), req.getColor()));
        line.getSections().add(new Section(line, upStation, downStation, req.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id)  {
        Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException("Resource not found"));
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse modifyLine(Long id, LineModifyRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException("Resource not found"));
        line.modifyLine(lineRequest.getName(), lineRequest.getColor());
        return createLineResponse(lineRepository.save(line));
    }
    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations().stream().map(stationService::createStationResponse).collect(Collectors.toList())
        );
    }

    @Transactional
    public void addSection(Long id, SectionAddRequest req) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException("Resource not found"));

        Station upStation = stationRepository.findById(req.getUpStationId()).orElseThrow(() -> new SubwayException("Resource not found"));
        Station downStation = stationRepository.findById(req.getDownStationId()).orElseThrow(() -> new SubwayException("Resource not found"));

        line.addSection(upStation, downStation, req.getDistance());
    }

    public List<SectionResponse> getSections(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException("Resource not found"));

        return line.getSections().stream().map(section -> new SectionResponse(
                        stationService.createStationResponse(section.getUpStation()),
                        stationService.createStationResponse(section.getDownStation()),
                        section.getDistance()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSections(Long id, SectionDeleteRequest req) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException("Resource not found"));
        Station station = stationRepository.findById(req.getStationId()).orElseThrow(() -> new SubwayException("Resource not found"));

        line.deleteSection(station);

    }
}
