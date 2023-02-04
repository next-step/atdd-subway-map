package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.request.LineCreateRequest;
import subway.line.application.dto.request.LineUpdateRequest;
import subway.line.domain.Line;
import subway.line.domain.LineCommandRepository;
import subway.line.domain.LineQueryRepository;
import subway.line.exception.LineNotFoundException;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineQueryRepository lineQueryRepository;
    private final LineCommandRepository lineCommandRepository;
    private final StationService stationService;

    public LineService(final LineQueryRepository lineQueryRepository,
                       final LineCommandRepository lineCommandRepository,
                       final StationService stationService) {
        this.lineQueryRepository = lineQueryRepository;
        this.lineCommandRepository = lineCommandRepository;
        this.stationService = stationService;
    }


    public List<Line> findAllLines() {
        return lineQueryRepository.findAll();
    }

    public Line findLineById(final Long lineId) {
        return lineQueryRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public Long saveLine(final LineCreateRequest lineCreateRequest) {
        Line line = createLine(lineCreateRequest);
        lineCommandRepository.save(line);

        return line.getId();
    }

    @Transactional
    public void updateLine(final Long lineId, final LineUpdateRequest lineUpdateRequest) {
        Line line = findLineById(lineId);

        line.updateLine(lineUpdateRequest.toEntity());
    }

    @Transactional
    public void deleteLine(final Long lineId) {
        lineCommandRepository.deleteById(lineId);
    }


    private Line createLine(final LineCreateRequest lineCreateRequest) {
        Station upStation = stationService.findStationById(lineCreateRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineCreateRequest.getDownStationId());

        return Line.createLine(lineCreateRequest.getName(), lineCreateRequest.getColor(),
                lineCreateRequest.getDistance(), upStation, downStation);
    }
}
