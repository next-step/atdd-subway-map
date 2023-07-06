package subway.line.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.line.domain.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;
import subway.line.view.LineCreateRequest;
import subway.line.view.LineModifyRequest;
import subway.line.view.LineResponse;
import subway.station.domain.Station;
import subway.station.service.StationService;

@Service
@RequiredArgsConstructor
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    @Transactional
    public LineResponse createStation(LineCreateRequest request) {
        Station upStation = stationService.get(request.getUpStationId());
        Station downStation = stationService.get(request.getDownStationId());

        Line line = mapRequestToEntity(request, upStation, downStation);

        Line createdLine = lineRepository.save(line);

        return LineResponse.from(createdLine);
    }

    private Line mapRequestToEntity(LineCreateRequest request, Station upStation, Station downStation) {
        return Line.firstLine(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
    }

    @Transactional(readOnly = true)
    public Line getLine(Long id) {
        return lineRepository.findById(id)
                             .orElseThrow(LineNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getList() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::from)
                             .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void modifyLine(Long id, LineModifyRequest request) {
        Line line = lineRepository.findById(id).get();

        line.setName(request.getName());
        line.setColor(request.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
