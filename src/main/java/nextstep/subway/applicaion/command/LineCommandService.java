package nextstep.subway.applicaion.command;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.applicaion.query.LineQueryService;
import nextstep.subway.applicaion.query.StationQueryService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.line.DuplicateLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineCommandService {

    private final LineRepository lineRepository;
    private final LineQueryService lineQueryService;
    private final StationQueryService stationQueryService;

    public LineCommandService(LineRepository lineRepository,
                              LineQueryService lineQueryService,
                              StationQueryService stationQueryService) {
        this.lineRepository = lineRepository;
        this.lineQueryService = lineQueryService;
        this.stationQueryService = stationQueryService;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new DuplicateLineException(request.getName());
        }

        Station upStation = stationQueryService.findStationsById(request.getUpStationId());
        Station downStation = stationQueryService.findStationsById(request.getDownStationId());

        Line line = lineRepository.save(
                Line.of(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public ShowLineResponse addSection(Long lineId, SectionRequest request) {
        Line line = lineQueryService.findLineById(lineId);
        Station upStation = stationQueryService.findStationsById(request.getUpStationId());
        Station downStation = stationQueryService.findStationsById(request.getDownStationId());

        line.addSection(upStation, downStation, request.getDistance());

        return lineQueryService.createShowLineResponse(line);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineQueryService.findLineById(lineId);
        Station deleteStation = stationQueryService.findStationsById(stationId);
        line.deleteStation(deleteStation);
    }

    public void updateLine(Long id, UpdateLineRequest request) {
        Line line = lineQueryService.findLineById(id);
        line.updateInfo(request.getName(), request.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

}
