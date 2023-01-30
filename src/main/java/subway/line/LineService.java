package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse createLine(LineRequest request) {
        Station downStation = getStationById(request.getDownStationId());
        Station upStation = getStationById(request.getUpStationId());

        Line line = lineRepository.save(Line.of(request, downStation, upStation));

        return LineResponse.of(line);
    }

    @Transactional
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    private Station getStationById(long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Exists Station"));
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Exists Station"));
        return LineResponse.of(line);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Exists Station"));

        Station downStation = getStationById(request.getDownStationId());
        Station upStation = getStationById(request.getUpStationId());

        line.modify(request, downStation, upStation);
        return LineResponse.of(line);
    }
}
