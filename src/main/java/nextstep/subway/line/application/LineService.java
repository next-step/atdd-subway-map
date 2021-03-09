package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.domain.Line.createLine;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(lineRequestToLine(request));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllStations() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findStationById(Long id) {
        Line line = lineRepository.getOne(id);
        return LineResponse.of(line);
    }

    public void modifyLine(LineRequest request, Long id) {
        Line line = lineRepository.getOne(id);
        line.update(request.getName(), request.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    // TODO 예외처리..?
    public Line lineRequestToLine(LineRequest lineRequest){
        Station upStation = stationRepository.getOne(lineRequest.getUpStationId());
        Station downStation = stationRepository.getOne(lineRequest.getDownStationId());
        return createLine(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
    }
}
