package subway.station.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LineUpdateResponse {
    private String name;
    private String color;

    @Builder
    public LineUpdateResponse(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
