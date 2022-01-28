package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.common.exception.ColumnName;
import nextstep.subway.common.exception.DuplicateColumnException;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.dto.LineResponse;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Line;
import nextstep.subway.line.domain.repository.LineRepository;
import nextstep.subway.station.applicaion.StationService;
import nextstep.subway.station.domain.model.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository,
        StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new DuplicateColumnException(ColumnName.LINE_NAME);
        }
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        Line line = Line.builder()
            .name(request.getName())
            .color(request.getColor())
            .distance(request.getDistance())
            .upStation(upStation)
            .downStation(downStation)
            .build();

        lineRepository.save(line);
        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::from)
                             .collect(Collectors.toList());
    }

    public LineResponse findById(long id) {
        return lineRepository.findByIdWithStations(id)
                             .map(LineResponse::withStationsFrom)
                             .orElseThrow(EntityNotFoundException::new);
    }

    public void edit(long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        line.edit(lineRequest.getName(), lineRequest.getColor());
    }

    public void delete(long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(long lineId, SectionRequest request) {
        Line line = lineRepository.findByIdWithStations(lineId)
                                  .orElseThrow(EntityNotFoundException::new);
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        line.addSection(upStation, downStation, request.getDistance());
    }

    public void deleteSection(long lineId, long sectionId) {
        lineRepository.findByIdWithStations(lineId)
                      .ifPresent(line -> line.deleteSection(sectionId));
    }
}
