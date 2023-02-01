package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.station.StationRepository;
import subway.station.web.dto.SaveLineRequest;
import subway.station.web.dto.SaveLineResponse;

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
    public SaveLineResponse saveLine(SaveLineRequest saveLineRequest) {
        Line line = lineRepository.save(new Line(
                saveLineRequest.getName(),
                saveLineRequest.getColor(),
                saveLineRequest.getUpStationId(),
                saveLineRequest.getDownStationId(),
                saveLineRequest.getDistance(),
                List.of(stationRepository.findById(saveLineRequest.getUpStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요.")),
                        stationRepository.findById(saveLineRequest.getDownStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요.")))
                ));
        return createLineResponse(line);
    }

    public List<SaveLineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public SaveLineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("검색된 노선이 없습니다. id를 바꿔주세요."));
        return createLineResponse(line);
    }

    @Transactional
    public SaveLineResponse update(Long id, String color) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("검색된 노선이 없습니다. id를 바꿔주세요."));
        line.changeColor(color);
        return createLineResponse(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    private SaveLineResponse createLineResponse(Line line) {
        return new SaveLineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                        stationRepository.findById(line.getUpStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요.")),
                        stationRepository.findById(line.getDownStationId()).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요."))
                ));
    }
}
