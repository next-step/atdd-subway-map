package subway.section;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "downStationId", "upStationId", "distance"})
public class SectionResponse {

    private Long id;

    private Long downStationId;

    private Long upStationId;

    private int distance;
}
