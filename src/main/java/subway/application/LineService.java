package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.util.Finder;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.LineCreateRequest;
import subway.dto.LineEditRequest;
import subway.dto.LineResponse;
import subway.dto.SectionResponse;
import subway.exception.LineNotFoundException;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final Finder finder;

    public LineService(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final SectionRepository sectionRepository,
            final Finder finder
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.finder = finder;
    }

    public LineResponse getBy(final Long lineId) {
        Line line = lineRepository.findByIdWithStation(lineId)
                .orElseThrow(LineNotFoundException::new);

        List<Section> sections = sectionRepository.findAllByStation(line.getStations());

        return LineResponse.by(line, SectionResponse.by(sections));
    }

    public List<LineResponse> getList() {
        return lineRepository.findAllWithStation().stream()
                .map(line -> LineResponse.by(line, SectionResponse.by(sectionRepository.findAllByLine(line))))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Long save(final LineCreateRequest lineCreateRequest) {
        Line line = convertToLineBy(lineCreateRequest);
        lineRepository.save(line);

        Section upStationSection = new Section(null, line.getUpStation(), line.getDownStation(), line);
        Section downStationSection = new Section(line.getUpStation(), line.getDownStation(), null, line);

        sectionRepository.saveAll(List.of(upStationSection, downStationSection));

        return line.getId();
    }

    private Line convertToLineBy(final LineCreateRequest lineCreateRequest) {
        List<Station> stations = stationRepository
                .findAllById(List.of(lineCreateRequest.getUpStationId(), lineCreateRequest.getDownStationId()));

        return new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                finder.findStationById(stations, lineCreateRequest.getUpStationId()),
                finder.findStationById(stations, lineCreateRequest.getDownStationId()),
                lineCreateRequest.getDistance()
        );
    }

    @Transactional
    public void edit(final Long lineId, final LineEditRequest lineEditRequest) {
        Line line = findLineBy(lineId);

        line.modify(lineEditRequest.getName(), lineEditRequest.getColor(), lineEditRequest.getDistance());
    }

    @Transactional
    public void delete(final Long lineId) {
        Line line = findLineBy(lineId);

        sectionRepository.findAllByLine(line)
                .forEach(Section::detachLine);

        lineRepository.delete(line);
    }

    private Line findLineBy(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);
    }
}
