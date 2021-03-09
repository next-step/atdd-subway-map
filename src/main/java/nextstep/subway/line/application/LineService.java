package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineAlreadyExistException;
import nextstep.subway.line.exception.LineNonExistException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.NonExistStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private static final String EXCEPTION_MESSAGE_EXIST_LINE_NAME = "존재하는 지하철 노선 입니다.";
    private static final String EXCEPTION_MESSAGE_NON_EXIST_LINE_NAME = "존재하지 않는 지하철 노선입니다.";
    private static final String EXCEPTION_MESSAGE_NON_EXIST_STATION = "존재하지 않는 지하철 역입니다.";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        validateLineName(request.getName());

        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());

        Line savedLine = lineRepository.save(request.toLine());
        savedLine.addSection(upStation, downStation, request.getDistance());

        return LineResponse.of(savedLine);
    }

    private Station findStationById(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new NonExistStationException(EXCEPTION_MESSAGE_NON_EXIST_STATION));
    }

    private void validateLineName(String lineName) {
        if (lineRepository.existsByName(lineName)) {
            throw new LineAlreadyExistException(EXCEPTION_MESSAGE_EXIST_LINE_NAME);
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = findLineById(id);
        return LineResponse.of(line);
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);

        line.update(lineRequest.toLine());

        return LineResponse.of(line);
    }

    public void deleteLine(Long id) {
        Line line = findLineById(id);

        lineRepository.delete(line);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNonExistException(EXCEPTION_MESSAGE_NON_EXIST_LINE_NAME));
    }
}
