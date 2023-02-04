package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.ErrorResponseCode;
import subway.exception.SubwayIllegalArgumentException;
import subway.exception.SubwayRestApiException;
import subway.section.SectionRequest;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse create(LineRequest request) {
        Station upStation = stationService.findOneById(request.getUpStationId());
        Station downStation = stationService.findOneById(request.getDownStationId());
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        try {
            line.addSection(upStation, downStation, request.getDistance());
        } catch (SubwayIllegalArgumentException e) {
            throw new SubwayRestApiException(e.getErrorResponseCode());
        }
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findOneById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(() -> new SubwayRestApiException(ErrorResponseCode.NOT_FOUND_LINE));
    }

    @Transactional
    public LineResponse updateById(Long id, LineRequest lineRequest) {
        Line line = getLine(id);
        return LineResponse.of(line.updateLine(lineRequest.getName(), lineRequest.getColor()));
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(final Long id, final SectionRequest sectionRequest) {
        final Line line = getLine(id);
        final Station upStation = stationService.findOneById(sectionRequest.getUpStationId());
        final Station downStation = stationService.findOneById(sectionRequest.getDownStationId());
        try {
            line.addSection(upStation, downStation, sectionRequest.getDistance());
        } catch (SubwayIllegalArgumentException e) {
            throw new SubwayRestApiException(e.getErrorResponseCode());
        }
    }

    @Transactional
    public void deleteSection(final Long id, final Long stationId) {
        final Line line = getLine(id);
        final Station station = stationService.findOneById(stationId);
        try {
            line.removeSection(station);
        } catch (SubwayIllegalArgumentException e) {
            throw new SubwayRestApiException(e.getErrorResponseCode());
        }
    }

    private Line getLine(final long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new SubwayRestApiException(ErrorResponseCode.NOT_FOUND_LINE));
    }
}
