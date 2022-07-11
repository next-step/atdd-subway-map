package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        validationStations(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        Station upStation = findStation(lineRequest.getUpStationId());
        Station downStation = findStation(lineRequest.getDownStationId());

        final Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance(), upStation, downStation);
        final Line newLine = lineRepository.save(line);

        return LineResponse.of(newLine);
    }

    @Transactional
    public LineResponse addSection(Long id, SectionRequest sectionRequest) {
        validationStations(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());

        Line line = findLind(id);
        Station upStation = findStation(sectionRequest.getUpStationId());
        Station downStation = findStation(sectionRequest.getDownStationId());
        Section section = new Section(sectionRequest.getDistance(), upStation, downStation);
        line.addSection(section);

        return LineResponse.of(line);
    }

    private Station findStation(Long upStationId) {
        return stationRepository.findById(upStationId).orElseThrow(() ->
                new IllegalArgumentException("역이 없습니다.")
        );
    }

    private void validationStations(Long upStationId, Long downStationId) {
        if (Objects.equals(upStationId, downStationId)) {
            throw new IllegalArgumentException("상행종점역과 하행종점역의 아이디는 같을 수 없습니다.");
        }
    }

    public LineResponse findLine(Long id) {
        Line line = findLind(id);
        return LineResponse.of(line);
    }

    private Line findLind(Long id) {
        return lineRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("노선이 존재하지 않습니다")
        );
    }

    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = findLind(id);
        line.changeInfo(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

}