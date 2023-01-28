package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundLineException;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Line line = createLine(request);

        Line savedLine = lineRepository.save(line);

        return LineResponse.of(savedLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAllWithStation().stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Optional<LineResponse> findById(Long id) {
        return lineRepository.findById(id)
            .map(LineResponse::of);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void modifyLine(Long id, LineModifyRequest lineModifyRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NotFoundLineException::new);
        line.modify(lineModifyRequest);
    }

    @Transactional()
    @Modifying(clearAutomatically = true)
    public void deleteLine(Long id) {
        Line findLine = lineRepository.findById(id).orElseThrow(NotFoundLineException::new);

        List<Long> stationIds = findLine.getStationIds();

        stationRepository.updateLineByIds(stationIds, null);

        lineRepository.delete(findLine);
    }

    private Line createLine(LineRequest request) {
        List<Station> stations = stationRepository.findByIdIn(request.getStationIds());

        return new Line(request.getName(), request.getColor(), stations);
    }
}
