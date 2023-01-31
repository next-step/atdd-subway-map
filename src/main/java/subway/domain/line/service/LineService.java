package subway.domain.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.global.error.ErrorCode;
import subway.global.error.exception.NotFoundException;
import subway.infrastructure.line.LineRepository;
import subway.presentation.line.dto.request.LineRequest;
import subway.presentation.line.dto.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    @Transactional
    public LineResponse createLine(LineRequest request, Station upStation, Station downStation) {
        Stations stations = Stations.of(List.of(upStation, downStation));

        Line initLine = request.toEntity(stations);
        Line line = lineRepository.save(initLine);

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }
}