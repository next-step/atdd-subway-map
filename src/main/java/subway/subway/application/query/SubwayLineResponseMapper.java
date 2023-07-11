package subway.subway.application.query;

import org.springframework.stereotype.Component;
import subway.subway.domain.SubwayLine;
import subway.subway.domain.SubwaySection;

import java.util.List;
import java.util.stream.Collectors;

@Component
class SubwayLineResponseMapper {

    public SubwayLineResponse mapFrom(SubwayLine subwayLine) {
        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                mapFrom(subwayLine.getSectionList()));
    }

    private List<SubwayLineResponse.StationInfo> mapFrom(List<SubwaySection> sections) {
        return sections.stream().flatMap(section -> mapFrom(section).stream()).collect(Collectors.toList());
    }

    private List<SubwayLineResponse.StationInfo> mapFrom(SubwaySection section) {
        return List.of(
                new SubwayLineResponse.StationInfo(
                        section.getStartStationId(),
                        section.getStartStationName()),
                new SubwayLineResponse.StationInfo(
                        section.getEndStationId(),
                        section.getEndStationName()));
    }
}
