package subway.line;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import subway.*;

import java.util.*;
import java.util.stream.*;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationFindEntityService stationFindEntityService;
    private final LineFindEntityService lineFindEntityService;

    public LineService(
            final LineRepository lineRepository,
            final StationFindEntityService stationFindEntityService,
            final LineFindEntityService lineFindEntityService
    ) {

        this.lineRepository = lineRepository;
        this.stationFindEntityService = stationFindEntityService;
        this.lineFindEntityService = lineFindEntityService;
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

        return LineResponse.from(lineFindEntityService.getById(lineId));
    }

    public List<LineResponse> getAll() {

        return lineRepository.findAll()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public void editLine(final Long lineId, final LineEditRequest lineEditRequest) {

        final var line = lineFindEntityService.getById(lineId)
                .change(lineEditRequest.getName(), lineEditRequest.getColor());
        lineRepository.save(line);
    }

    public void deleteById(final Long lineId){

        lineRepository.deleteById(lineId);
    }
}
