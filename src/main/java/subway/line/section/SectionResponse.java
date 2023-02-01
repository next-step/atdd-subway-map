package subway.line.section;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "downStationId", "upStationId", "distance"})
@ToString
public class SectionResponse {

    private Long id;

    private Long downStationId;

    private Long upStationId;

    private int distance;

    private int number;

    public static SectionResponse of(Section section) {
        return new SectionResponse(
            section.getId(),
            section.getDownStationId(),
            section.getUpStationId(),
            section.getDistance(),
            section.getNumber()
        );
    }
}
