package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.AddSectionRequest;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.application.dto.UpdateLineRequest;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.SectionValidator;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationService stationService;
    private final SectionValidator sectionValidator;
    private final LineRepository lineRepository;

    public LineService(final StationService stationService, final SectionValidator sectionValidator, final LineRepository lineRepository) {
        this.stationService = stationService;
        this.sectionValidator = sectionValidator;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest lineRequest) {
        final Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        final Station downStation = stationService.findStationById(lineRequest.getDownStationId());

        final Line line = Line.of(
                lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation,
                lineRequest.getDistance()
        );
        lineRepository.save(line);
        return new LineResponse(line);
    }

    public List<LineResponse> findAllLine() {
        final List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(final long lineId) {
        final Line line = getLine(lineId);
        return new LineResponse(line);
    }

    @Transactional
    public LineResponse updateLine(final long lineId, final UpdateLineRequest updateLineRequest) {
        final Line line = getLine(lineId);
        line.update(updateLineRequest.getName(), updateLineRequest.getColor());
        return new LineResponse(line);
    }

    @Transactional
    public void deleteLine(final long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public LineResponse addSection(final long lineId, final AddSectionRequest addSectionRequest) {
        final Line line = getLine(lineId);
        final Station upStation = stationService.findStationById(Long.parseLong(addSectionRequest.getUpStationId()));
        final Station downStation = stationService.findStationById(Long.parseLong(addSectionRequest.getDownStationId()));

        line.addSection(sectionValidator, upStation, downStation, addSectionRequest.getDistance());
        return new LineResponse(line);
    }

    @Transactional
    public void removeSection(final long lineId, final long stationId) {
        final Line line = getLine(lineId);
        final Station station = stationService.findStationById(stationId);
        line.removeSection(sectionValidator, station);
    }

    private Line getLine(final long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
