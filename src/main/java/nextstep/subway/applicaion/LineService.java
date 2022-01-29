package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.service.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final Validator<Line> lineValidator;

    public LineService(final LineRepository lineRepository,
                       final StationRepository stationRepository,
                       final Validator<Line> lineValidator) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineValidator = lineValidator;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(NoSuchElementException::new);
        final Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(NoSuchElementException::new);
        final Line line = lineRepository.save(new Line(
                request.getName(),
                request.getColor(),
                upStation,
                downStation,
                request.getDistance(),
                lineValidator
        ));

        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(final Long id) {
        final Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return createLineResponse(line);
    }

    public void updateLine(final Long id, final LineRequest lineRequest) {
        final Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.change(lineRequest.getName(), lineRequest.getColor(), lineValidator);
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(final Long id, final LineRequest request) {
        final Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        final Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(NoSuchElementException::new);
        final Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(NoSuchElementException::new);

        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
    }

    public void removeSection(final Long id, final Long stationId) {
        final Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        final Station station = stationRepository.findById(stationId).orElseThrow(NoSuchElementException::new);

        line.removeSection(station);
    }

    private LineResponse createLineResponse(final Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations().stream()
                        .map(station -> new StationResponse(
                                station.getId(),
                                station.getName(),
                                station.getCreatedDate(),
                                station.getModifiedDate()
                        ))
                        .collect(Collectors.toList()),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
