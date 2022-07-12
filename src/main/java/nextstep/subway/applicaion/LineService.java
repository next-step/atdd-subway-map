package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineChangeRequest;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.LineNotFoundException;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line createLine = lineRepository.save(lineRequest.toLine());
        return createLineResponse(createLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> findLines = lineRepository.findAll();
        return findLines.stream()
                .map(this::createLineResponse)
                .collect(toList());
    }

    public LineResponse getLineById(Long id) {
        Line findLine = findLineById(id);
        return createLineResponse(findLine);
    }

    @Transactional
    public void changeLineById(Long lineId, LineChangeRequest lineChangeRequest) {
        Line findLine = findLineById(lineId);
        findLine.change(lineChangeRequest.getName(), lineChangeRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    private LineResponse createLineResponse(Line line) {
        List<Station> findStations = findUpAndDownStation(line.getUpStationId(), line.getDownStationId());
        return LineResponse.from(line, findStations);
    }

    private List<Station> findUpAndDownStation(Long upStationId, Long downStationId) {
        List<Station> findStations = stationRepository.findAllById(Arrays.asList(upStationId, downStationId));

        if (findStations.isEmpty()) {
            throw new StationNotFoundException("존재하지 않는 지하철역입니다.");
        }

        return findStations;
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다."));
    }
}
