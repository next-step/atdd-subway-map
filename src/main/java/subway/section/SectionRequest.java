package subway.section;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"downStationId", "upStationId", "distance"})
public class SectionRequest {
    private Long downStationId;

    private Long upStationId;

    private int distance;
}
