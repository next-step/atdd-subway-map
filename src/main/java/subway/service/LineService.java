package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineModifyRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.Line;
import subway.entity.Station;
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


    @Transactional
    public LineResponse saveLine(LineRequest req) throws Exception{
        Station upStation = stationRepository.findById(req.getUpStationId()).orElseThrow(() -> new Exception("Resource not found"));
        Station downStation = stationRepository.findById(req.getDownStationId()).orElseThrow(() -> new Exception("Resource not found"));

        Line line = lineRepository.save(new Line(req.getName(), req.getColor(), upStation, downStation, req.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) throws Exception {
        Line line = lineRepository.findById(id).orElseThrow(() -> new Exception("Resource not found"));
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse modifyLine(Long id, LineModifyRequest lineRequest) throws Exception{
        Line line = lineRepository.findById(id).orElseThrow(() -> new Exception("Resource not found"));
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
                line.getStations(),
                line.getDistance()
        );
    }

}
