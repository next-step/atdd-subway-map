package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();
        Line createLine = new Line(lineRequest, Arrays.asList(upStation, downStation));
        Line createdLine = lineRepository.save(createLine);
        return new LineResponse(createdLine);
    }

    public LineResponse findOneLine(Long id) {
        return null;
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(
            line -> new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStations().getStations())
        ).collect(Collectors.toList());
    }

    public void deleteLine(Long id) {
    }

}
