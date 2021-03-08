package nextstep.subway.line.application;

import nextstep.subway.exceptions.AlreadyExistsEntityException;
import nextstep.subway.exceptions.NotFoundLineException;
import nextstep.subway.exceptions.NotFoundStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    public static final String LINE_EXCEPTION = "%s 노선";
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        String requestName = request.getName();
        if (lineRepository.existsByName(requestName)) {
            throw new AlreadyExistsEntityException(String.format(LINE_EXCEPTION, requestName));
        }
        Section section =  createSection(request.toSectionRequest());
        Line persistLine = lineRepository.save(request.toLine(section));

        return LineResponse.of(persistLine);
    }

    private Section createSection(SectionRequest request) {
        return Section.Builder()
                .upStation(findStationById(request.getUpStationId()))
                .downStation(findStationById(request.getDownStationId()))
                .distance(request.getDistance())
                .build();
    }

    public List<LineResponse> readLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse readLine(Long id) {
        Line line = findLineById(id);
        return LineResponse.of(line);
    }

    @Modifying
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);
        line.update(lineRequest.toLine());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public Line addLineStation(Long lineId, SectionRequest sectionRequest) {
        Line line = findLineById(lineId);
        Section section = createSection(sectionRequest);

        line.addSection(section);

        return line;
    }

    @Modifying
    public void deleteLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        line.deleteLastSection(stationId);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(NotFoundStationException::new);
    }
}
