package subway.subway.application.query;

import org.springframework.stereotype.Component;
import subway.subway.application.query.SubwayLineResponse;
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

    private List<SubwayLineResponse.StationResponse> mapFrom(List<SubwaySection> sections) {
        return sections.stream().flatMap(section -> mapFrom(section).stream()).collect(Collectors.toList());
    }

    private List<SubwayLineResponse.StationResponse> mapFrom(SubwaySection section) {
        return List.of(
                new SubwayLineResponse.StationResponse(
                        section.getStartStationId(),
                        section.getStartStationName()),
                new SubwayLineResponse.StationResponse(
                        section.getEndStationId(),
                        section.getEndStationName()));
    }
}
