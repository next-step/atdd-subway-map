package subway.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.station.StationRepository;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.service.line.request.LineCreateRequest;
import subway.service.line.request.LineModifyRequest;
import subway.service.line.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {

        Line line = new Line(lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                stationRepository.findById(lineCreateRequest.getUpStationId()).orElseThrow(StationNotFoundException::new),
                stationRepository.findById(lineCreateRequest.getDownStationId()).orElseThrow(StationNotFoundException::new),
                lineCreateRequest.getDistance()
        );

        lineRepository.save(line);
        return LineResponse.of(line);
    }

    @Transactional
    public void modifyLine(Long id, LineModifyRequest lineModifyRequest) {
        lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new)
                .modify(lineModifyRequest.getName(), lineModifyRequest.getColor());
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

}
