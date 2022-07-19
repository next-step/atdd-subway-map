package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.Line.LineRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.utils.ObjectMapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(() -> new EntityNotFoundException("station.not.found"));
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(() -> new EntityNotFoundException("station.not.found"));
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
        LineResponse lineResponse = ObjectMapUtils.looseMap(lineRepository.save(line), LineResponse.class);
        lineResponse.sideStation(upStation, downStation);
        return lineResponse;
    }

    public List<LineResponse> findAllLines() {
        return ObjectMapUtils.mapLooseAll(lineRepository.findAll(), LineResponse.class);
    }

    public LineResponse findLine(Long id) {
        return ObjectMapUtils.map(lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("station.not.found")), LineResponse.class);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("station.not.found")).update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

}
