package subway.section;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import subway.line.section.LineSection;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "downStationId", "upStationId", "distance"})
@ToString
public class SectionResponse {

    private Long id;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    public static SectionResponse of(LineSection lineSection) {
        return new SectionResponse(
            lineSection.getId(),
            lineSection.getUpStationId(),
            lineSection.getDownStationId(),
            lineSection.getDistance()
        );
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(
            section.getId(),
            section.getUpStationId(),
            section.getDownStationId(),
            section.getDistance()
        );
    }
}
