package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository,
        final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest lineRequest) {
        Line line = lineRepository.save(toLine(lineRequest));
        return new LineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::new)
            .collect(Collectors.toList());
    }

    public LineResponse findLine(final Long id) {
        return new LineResponse(findLineById(id));
    }

    @Transactional
    public void update(final Long id, final LineUpdateRequest lineUpdateRequest) {
        Line line = findLineById(id);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    private Line toLine(final LineRequest lineRequest) {
        Station upStation = findStationById(lineRequest.getUpStationId());
        Station downStation = findStationById(lineRequest.getDownStationId());

        return new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation,
            lineRequest.getDistance());
    }

    private Line findLineById(final Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(String.format("존재하지 않는 노선입니다. 요청한 노선 Id : ", id)));
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(String.format("존재하지 않는 역입니다. 요청한 역 Id : ", id)));
    }
}
