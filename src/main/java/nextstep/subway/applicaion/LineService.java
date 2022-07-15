package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineSaveRequest;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineContent;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLines(final LineSaveRequest request) {

        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        Section section = new Section(upStation, downStation, new Distance(request.getDistance()));

        Line savedLine = lineRepository.save(new Line(new LineContent(request.getName(), request.getColor()), section));

        return new LineResponse(savedLine.getId(), savedLine.name(), savedLine.color(), savedLine.stations());
    }

    public List<LineResponse> getLines() {
        final List<Line> linesList = lineRepository.findAll();

        return linesList.stream()
            .map(line -> new LineResponse(
                line.getId(), line.name(), line.color(),
                line.stations())).collect(Collectors.toList());
    }

    public LineResponse getOneLine(final Long id) {
        final Line line = findById(id);

        return new LineResponse(line.getId(), line.name(), line.color(), line.stations());
    }

    @Transactional
    public void updateLine(final Long id, final LineUpdateRequest request) {
        final Line line = findById(id);

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    private Line findById(final Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당하는 노선을 찾을 수 없습니다."));
    }

    @Transactional
    public void removeLine(final Long id) {
        final Line line = findById(id);
        lineRepository.delete(line);
    }

}
