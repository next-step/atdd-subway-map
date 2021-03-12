package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.LineNameDuplicatedException;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        if (lineRepository.existsByName(lineRequest.getName())) {
            throw new LineNameDuplicatedException(lineRequest.getName());
        }

        StationResponse upStationResponse = stationService.getStation(lineRequest.getUpStationId());
        StationResponse downStationResponse = stationService.getStation(lineRequest.getDownStationId());
        Line persistLine = lineRepository.save(lineRequest.toLine(upStationResponse, downStationResponse));

        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
        StationResponse upStationResponse = stationService.getStation(lineRequest.getUpStationId());
        StationResponse downStationResponse = stationService.getStation(lineRequest.getDownStationId());

        line.update(lineRequest.toLine(upStationResponse, downStationResponse));
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = getLineById(lineId);
        StationResponse upStationResponse = stationService.getStation(sectionRequest.getUpStationId());
        StationResponse downStationResponse = stationService.getStation(sectionRequest.getDownStationId());

        line.addSection(upStationResponse.toStation(), downStationResponse.toStation(), sectionRequest.getDistance());
        lineRepository.flush();
        return LineResponse.of(line);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLineById(lineId);
        line.removeByStationId(stationId);
    }

    private Line getLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }
}
