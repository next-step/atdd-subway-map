package nextstep.subway.lines.application;

import nextstep.subway.cmmn.exception.EntityNotExistException;
import nextstep.subway.lines.application.dto.LineResponse;
import nextstep.subway.lines.application.dto.LineSaveRequest;
import nextstep.subway.lines.application.dto.LineUpdateRequest;
import nextstep.subway.lines.domain.Line;
import nextstep.subway.lines.domain.LineRepository;
import nextstep.subway.stations.applicaion.StationService;
import nextstep.subway.stations.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    //TODO : open session in view 'false'일 경우 고민
    public Line saveLine(LineSaveRequest lineSaveRequest) {

        //상행역 조회
        Station upStation = stationService.findById(lineSaveRequest.getUpStationId());

        //하행역 조회
        Station downStation = stationService.findById(lineSaveRequest.getDownStationId());

        Line line = Line.makeLine(lineSaveRequest.getName(), lineSaveRequest.getColor(), upStation, downStation, lineSaveRequest.getDistance());
        return lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    public List<Line> getLines() {
        return lineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Line getLine(Long lineId) {
        return findById(lineId);
    }

    public void updateLine(Long lineId, LineUpdateRequest lineUpdateRequest) {
        Line line = findById(lineId);
        line.changeName(lineUpdateRequest.getName());
        line.changeColor(lineUpdateRequest.getColor());
    }

    public void deleteLine(Long lineId) {
        Line line = findById(lineId);
        lineRepository.delete(line);
    }

    @Transactional(readOnly = true)
    public Line findById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotExistException("지하철 노선을 찾을 수 없습니다. id = " + lineId));
    }
}
