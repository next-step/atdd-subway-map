package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    public static final String EMPTY_LINE_MSG = "존재하지 않는 노선 입니다.";
    public static final String EMPTY_UP_STATION_MSG = "존재 하지 않는 상행종점역 입니다.";
    public static final String EMPTY_DOWN_STATION_MSG = "존재 하지 않는 하행종점역 입니다.";
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest lineRequest) {
        final Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(() -> new IllegalArgumentException(EMPTY_UP_STATION_MSG));
        final Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(() -> new IllegalArgumentException(EMPTY_DOWN_STATION_MSG));

        final Section section = new Section(upStation, downStation);
        final Section savedSection = sectionRepository.save(section);

        final Line line = new Line(lineRequest.getName(), lineRequest.getColor());
        final Line savedLine = lineRepository.save(line);
        savedSection.setLine(savedLine);
        return new LineResponse(savedLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAllFetchJoin().stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(final Long id) {
        final Line line = lineRepository.findByIdFetchJoin(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EMPTY_LINE_MSG));

        return new LineResponse(line);
    }

    @Transactional
    public void modifyLine(final LineRequest lineRequest) {
        final Line line = lineRepository.findById(lineRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EMPTY_LINE_MSG));

        line.changeName(lineRequest.getName());
        line.changeColor(lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(final Long id) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EMPTY_LINE_MSG));

        lineRepository.delete(line);
    }
}
