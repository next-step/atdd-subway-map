package nextstep.subway.applicaion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station downStation = getStation(lineRequest.getDownStationId());
        Station upStation = getStation(lineRequest.getUpStationId());

        Section section = Section.createSection(downStation, upStation, lineRequest.getDistance());
        sectionRepository.save(section);

        Line line = Line.createLine(lineRequest.getName(), lineRequest.getColor(), section);
        return createLineResponse(lineRepository.save(line));
    }

    public LineResponse findLine(Long lineId) {
        return createLineResponse(getLine(lineId));
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = getLine(lineId);
        line.updateNameAndColor(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLineById(Long lineId) {
        Line line = getLine(lineId);
        lineRepository.delete(line);
    }

    private Line getLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("해당 노선이 없습니다."));
    }

    private Station getStation(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 역이 없습니다."));
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stationResponses =
                createStationResponses(line.getDownStationTerminal(), line.getUpStationTerminal());

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }

    private StationResponse createStationResponse(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }

    private List<StationResponse> createStationResponses(Station downStation, Station upStation) {
        return Arrays.asList(
                createStationResponse(downStation),
                createStationResponse(upStation)
        );
    }
}
