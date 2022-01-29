package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineCreateRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineStationResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest request) {
        boolean existLineName = lineRepository.existsByName(request.getName());
        if (existLineName) {
            throw new IllegalArgumentException();
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), request.getUpStationId(), request.getDownStationId(), request.getDistance()));
        return new LineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> searchAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineStationResponse searchLine(final Long lineId) {
        Line line = getLine(lineId);
        List<Station> stations = stationRepository.findAllById(line.getAllStations());

        return new LineStationResponse(line, stations);
    }

    @Transactional
    public void updateLine(final LineUpdateRequest request) {
        boolean existLineName = lineRepository.existsByName(request.getName());
        Line line = getLine(request.getId());

        line.update(existLineName, request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(final Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional(readOnly = true)
    public Line getLine(final Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
    }
}
