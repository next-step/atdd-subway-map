package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<SectionResponse> sections;
}
