package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LineUpdateRequest;
import subway.controller.dto.LinesResponse;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository,
        StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createdLineBy(LineCreateRequest request) {
        return LineResponse.from(lineRepository.save(new Line.Builder()
            .name(request.getName())
            .color(request.getColor())
            .distance(request.getDistance())
            .sections(Sections.of(
                stationOfId(request.getUpStationId()),
                List.of(new Section(
                    stationOfId(request.getDownStationId()),
                    request.getDistance()))))
            .build()));
    }

    public LineResponse lineResponseFoundById(Long id) {
        return LineResponse.from(lineOfId(id));
    }

    public LinesResponse allLines() {
        return LinesResponse.from(lineRepository.findAll());
    }

    @Transactional
    public void updatedLineBy(Long id, LineUpdateRequest request) {
        Line line = lineOfId(id);

        line.modifyTheLine(request.getName()
            , request.getColor()
            , request.getDistance()
            , Sections.of(
                stationOfId(request.getUpStationId()),
                List.of(new Section(
                    stationOfId(request.getDownStationId()),
                    request.getDistance()))));

        lineRepository.save(line);
    }

    public void deleteLineBy(Long id) {
        lineRepository.delete(lineOfId(id));
    }

    private Line lineOfId(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new LineNotFoundException("일치하는 지하철 노선 정보가 존재하지 않습니다: " + id));
    }

    private Station stationOfId(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new StationNotFoundException("일치하는 지하철 역 정보가 존재하지 않습니다: " + id));
    }
}
