package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.ShowLineResponse;
import nextstep.subway.applicaion.dto.UpdateLineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.DuplicateLineException;
import nextstep.subway.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
            throw new DuplicateLineException(request.getName());
        }

        Station upStation = stationService.findStationsById(request.getUpStationId());
        Station downStation = stationService.findStationsById(request.getDownStationId());

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

    @Transactional(readOnly = true)
    public List<ShowLineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createShowLineResponse)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public ShowLineResponse findLine(Long id) {
        Line line = findLineById(id);

        return createShowLineResponse(line);
    }

    public void updateLine(Long id, UpdateLineRequest request) {
        Line line = findLineById(id);
        line.updateInfo(request.getName(), request.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException());
    }

    private ShowLineResponse createShowLineResponse(Line line) {
        return ShowLineResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

}
