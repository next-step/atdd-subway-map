package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.UpdateLineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private static final String NOT_EXIST_STATION = "존재하지 않는 지하철 역입니다.";
    private static final String NOT_EXIST_LINE = "존재하지 않는 지하철 노선입니다.";

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = findStationById(lineRequest.getUpStationId());
        Station dowStation = findStationById(lineRequest.getDownStationId());

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance(), upStation, dowStation));
        return LineResponse.from(line);
    }

    public void updateLine(Long id, UpdateLineRequest updateLineRequest) {
        Line line = findLineById(id);
        line.update(updateLineRequest.getName(), updateLineRequest.getColor());
    }

    public void deleteLineById(Long id) {
        Line line = findLineById(id);
        lineRepository.delete(line);
    }

    public void addSection(Long id, SectionRequest sectionRequest) {
        Line line = findLineById(id);
        Station upStation = findStationById(sectionRequest.getUpStationId());
        Station downStation = findStationById(sectionRequest.getDownStationId());

        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }

    public void removeSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);

        line.removeSection(station);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_STATION));
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_LINE));
    }
}
