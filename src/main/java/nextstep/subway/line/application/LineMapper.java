package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.application.SectionMapper;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineMapper {

    private final StationRepository stationRepository;
    private final SectionMapper sectionMapper;

    public LineMapper(StationRepository stationRepository, SectionMapper sectionMapper) {
        this.stationRepository = stationRepository;
        this.sectionMapper = sectionMapper;
    }

    public Line toLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());
        return new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
    }

    public LineResponse toLineResponse(Line line) {
        List<Section> sections = getSortedSections(line);
        List<StationResponse> stationResponses = makeStationResponsesBySections(sections);

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses, line.getCreatedDate(), line.getModifiedDate());
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

    private List<Section> getSortedSections(Line line) {
        return line.getSections()
                .stream()
                .sorted(Comparator.comparing(Section::getId))
                .collect(Collectors.toList());
    }

    private List<StationResponse> makeStationResponsesBySections(List<Section> sections) {
        List<StationResponse> stations = sections.stream()
                .map(sectionMapper::toStationResponseWithUpStation)
                .collect(Collectors.toList());

        if (!stations.isEmpty()) {
            stations.add(sectionMapper.toStationResponseWithDownStation(sections.get(sections.size() - 1)));
        }

        return stations;
    }
}
