package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.LineUpdateRequest;
import subway.controller.dto.response.LineCreateResponse;
import subway.controller.dto.response.LineResponse;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
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
    public LineCreateResponse saveLine(LineCreateRequest lineCreateRequest) {
        Line line = lineRepository.save(createLine(lineCreateRequest));
        return LineCreateResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findById(Long id) {
        return LineResponse.from(lineRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line createLine(LineCreateRequest lineCreateRequest) {
        Station upStation = stationRepository.getReferenceById(lineCreateRequest.getUpStationId());
        Station downStation = stationRepository.getReferenceById(lineCreateRequest.getDownStationId());

        return Line.builder()
                .name(lineCreateRequest.getName())
                .color(lineCreateRequest.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineCreateRequest.getDistance())
                .build();
    }
}
