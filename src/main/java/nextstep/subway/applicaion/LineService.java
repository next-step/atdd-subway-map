package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.ChangeLineRequest;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRequest;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.LineNameDuplicationException;
import nextstep.subway.exception.NotFoundLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new LineNameDuplicationException(request.getName());
        }
        Station upStation = stationService.findStation(request.getUpStationId());
        Station downStation = stationService.findStation(request.getDownStationId());

        Section section = new Section(upStation, downStation, request.getDistance());

        Line line = lineRepository.save(
            new Line(request.getName(), request.getColor(), new Sections(section)));

        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundLineException(id));

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.from(line);
    }

    public LineResponse changeLine(Long id, ChangeLineRequest lineRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundLineException(id));

        line.update(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.from(line);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse registerSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundLineException(id));

        Station upStation = stationService.findStation(sectionRequest.getUpStationId());
        Station downStation = stationService.findStation(sectionRequest.getDownStationId());

        line.registerSection(upStation, downStation, sectionRequest.getDistance());

        return LineResponse.from(line);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new NotFoundLineException(lineId));

        Station station = stationService.findStation(stationId);

        line.deleteSection(station);
    }
}
