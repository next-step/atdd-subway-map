package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.station.StationRepository;
import subway.station.web.dto.LineRequest;
import subway.station.web.dto.LineResponse;

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
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance(),
                List.of(stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요.")),
                        stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요.")))
                ));
        return createLineResponse(line);
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("검색된 노선이 없습니다. id를 바꿔주세요."));
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse update(Long id, String color) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("검색된 노선이 없습니다. id를 바꿔주세요."));
        line.changeColor(color);
        return createLineResponse(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                        stationRepository.findById(line.getUpStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요.")),
                        stationRepository.findById(line.getDownStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요."))
                ));
    }
}
