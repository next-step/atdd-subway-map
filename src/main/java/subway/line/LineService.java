package subway.line;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import subway.*;

import java.util.*;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationFindEntityService stationFindEntityService;

    public LineService(final LineRepository lineRepository, final StationFindEntityService stationFindEntityService) {
        this.lineRepository = lineRepository;
        this.stationFindEntityService = stationFindEntityService;
    }

    public void createLine(final LineCreateRequest lineCreateRequest) {
        final var upStation = stationFindEntityService.getById(lineCreateRequest.getUpStationId());
        final var downStation = stationFindEntityService.getById(lineCreateRequest.getDownStationId());

        final Line line = new Line(lineCreateRequest.getName(), lineCreateRequest.getColor());
        line.addLineStation(new LineStation(1, upStation, lineCreateRequest.getDistance()));
        line.addLineStation(new LineStation(2, downStation, lineCreateRequest.getDistance()));

        lineRepository.save(line);
    }

    public LineResponse getById(final Long lineId) {

        final Line line = lineRepository.findById(lineId)
                .orElseThrow(NoSuchElementException::new);
        return LineResponse.from(line);
    }
}
