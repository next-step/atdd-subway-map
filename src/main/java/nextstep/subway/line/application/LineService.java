package nextstep.subway.line.application;

import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.LineSectionRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.applicaion.StationService;
import nextstep.subway.station.applicaion.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public LineResponse saveLine(final LineRequest request) {
        return createLineResponse(lineRepository.save(request.toLine()));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(final Long id) {
        return lineRepository.findById(id)
                .map(this::createLineResponse)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest request) {
        final Line line = findLineById(id);
        line.setUpdate(request.toLine());
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addSection(final Long lineId, final LineSectionRequest sectionRequest) {
        final Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        final Station downStation = stationService.findStationById(sectionRequest.getDownStationId());

        final Line line = findLineById(lineId);
        line.addSection(new Section(line, upStation.getId(), downStation.getId(), sectionRequest.getDistance()));

        return createLineResponse(line);
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = stationService.findStationById(stationId);
        line.deleteStation(station);
    }

    private Line findLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private LineResponse createLineResponse(final Line line) {
        final List<Long> stationIds = new ArrayList<>();
        for (final Section section : line.getSections()) {
            stationIds.add(section.getDownStationId());
            stationIds.add(section.getUpStationId());
        }
        final List<StationResponse> stations = stationIds.stream()
                .distinct()
                .map((Long id) -> {
                    final Station station = stationService.findStationById(id);
                    return StationResponse.of(station);
                })
                .collect(Collectors.toList());
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations
        );
    }
}
