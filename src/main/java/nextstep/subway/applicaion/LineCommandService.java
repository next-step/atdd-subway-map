package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class LineCommandService {

    private static final String LINE_NOTFOUND_MESSAGE = "해당 id의 지하철 노선이 존재하지 않습니다.";

    private final StationQueryService stationQueryService;

    private final LineRepository lineRepository;

    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationQueryService.findById(lineRequest.getUpStationId());
        Station downStation = stationQueryService.findById(lineRequest.getDownStationId());

        Section section = new Section(upStation, downStation, lineRequest.getDistance());
        Line line = lineRepository.save(new Line(lineRequest, section));

        return createLineResponse(line);
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findLineOrElseThrow(id);
        Station upStation = stationQueryService.findById(lineRequest.getUpStationId());
        Station downStation = stationQueryService.findById(lineRequest.getDownStationId());

        line.update(lineRequest, upStation, downStation);
    }

    public void deleteLineById(Long id) {
        findLineOrElseThrow(id);
        lineRepository.deleteById(id);
    }

    public void addSection(Long id, SectionRequest sectionRequest) {
        Line line = findLineOrElseThrow(id);
        Station upStation = stationQueryService.findById(sectionRequest.getUpStationId());
        Station downStation = stationQueryService.findById(sectionRequest.getDownStationId());

        line.addSection(new Section(upStation, downStation, sectionRequest.getDistance()));
    }

    public void deleteLastSection(Long id, Long stationId) {
        Line line = findLineOrElseThrow(id);
        line.removeSection(stationId);
    }

    private Line findLineOrElseThrow(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(LINE_NOTFOUND_MESSAGE));
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }
}
