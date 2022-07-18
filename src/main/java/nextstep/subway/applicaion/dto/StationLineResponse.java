package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<SectionResponse> sections;

    public static StationLineResponse form(Line line) {
        List<SectionResponse> sectionResponses = line.findSectionList()
                                            .stream().map(SectionResponse::form)
                                            .collect(Collectors.toList());
        return new StationLineResponse(line.getId(), line.getName(), line.getColor(), sectionResponses);
    }

}
