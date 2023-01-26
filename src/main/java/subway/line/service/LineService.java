package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;
import subway.line.dto.LineResponse;
import subway.line.repository.LineRepository;
import subway.line.dto.LineRequest;
import subway.line.entity.Line;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Line save(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();
        return lineRepository.save(lineRequest.toEntity(upStation, downStation));
    }

    public List<Line> findAll() {
        return null;
    }

    public List<LineResponse> findAllLineList() {
        // TODO projection 사용하도록 변경
        return lineRepository.findAllLine()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }
}
