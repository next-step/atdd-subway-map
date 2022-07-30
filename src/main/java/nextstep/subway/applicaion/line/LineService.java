package nextstep.subway.applicaion.line;

import nextstep.subway.applicaion.common.LineNotFoundException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.domain.LineRepository;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.applicaion.line.dto.LineUpdateRequest;
import nextstep.subway.applicaion.station.StationService;
import nextstep.subway.applicaion.station.domain.Station;
import nextstep.subway.applicaion.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.getStationThrowExceptionIfNotExists(lineRequest.getUpStationId());
        Station downStation = stationService.getStationThrowExceptionIfNotExists(lineRequest.getDownStationId());

        Line line = lineRepository.save(lineRequest.toLine());

        return createLineResponse(line, upStation, downStation);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);

        return createLineResponse(line);
    }

    @Transactional
    public void updateLineById(Long id, LineUpdateRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.updateLineInfo(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = stationService.getStationThrowExceptionIfNotExists(line.getUpStationId());
        Station downStation = stationService.getStationThrowExceptionIfNotExists(line.getDownStationId());

        return createLineResponse(line, upStation,downStation);
    }

    private LineResponse createLineResponse(Line line, Station upStation, Station downStation) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(new StationResponse(upStation.getId(), upStation.getName()), new StationResponse(downStation.getId(), downStation.getName()))
        );
    }
}
