package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        final Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        return LineResponse.of(findLineById(id));
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        findLineById(id).update(request.toLine());
        return LineResponse.of(findLineById(id));
    }

    public void deleteLine(Long id) {
        lineRepository.delete(findLineById(id));
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElse(null);
    }

    public LineResponse createSection(Long id, LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElse(null);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElse(null);
        Line line = lineRepository.findById(id).orElse(null);

        line.addSection(new Section(upStation, downStation, request.getDistance()));

        return LineResponse.of(line);
    }
}
