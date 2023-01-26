package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public Line save(LineCreateRequest lineCreateRequest) {
        Station upStation = stationRepository.findById(lineCreateRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineCreateRequest.getDownStationId()).orElseThrow();
        return lineRepository.save(lineCreateRequest.toEntity(upStation, downStation));
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAllLine()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("노선을 조회 할 수 없습니다. id : " + id));
    }

    @Transactional
    public void update(LineUpdateRequest request) {
        Line line = findByIdIfAbsenceThrwoException(request.getId());
        line.update(request.getName(), request.getColor());
    }

    private Line findByIdIfAbsenceThrwoException(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id로 노선을 조회 할 수 없습니다. id" + id));
    }
}
