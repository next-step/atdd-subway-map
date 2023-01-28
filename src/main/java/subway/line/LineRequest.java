package subway.line;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineRequest {
    private String name;
    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    @JsonIgnore
    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }
}
