package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.dto.request.CreateLineRequest;
import subway.line.dto.response.LineResponse;
import subway.line.repository.LineRepository;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse creteLine(CreateLineRequest request) {
        Line line = Line.create(
                request.getName(),
                request.getColor(),
                stationRepository.getById(request.getUpStationId()),
                stationRepository.getById(request.getDownStationId()),
                request.getDistance()
        );

        Line savedLine = lineRepository.save(line);
        return LineResponse.from(savedLine);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }
}
