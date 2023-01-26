package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
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
    public Line save(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();
        return lineRepository.save(lineRequest.toEntity(upStation, downStation));
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAllLine()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }
}
