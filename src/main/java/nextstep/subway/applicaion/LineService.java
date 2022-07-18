package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.Line.LineRepository;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(() -> new EntityNotFoundException("station.not.found"));
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(() -> new EntityNotFoundException("station.not.found"));
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
        return createLineResponse(lineRepository.save(line));
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public LineResponse findLine(Long id) {
        return createLineResponse(lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("station.not.found")));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("station.not.found")).update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.upStation(), line.downStation(), line.distance());
    }

}
