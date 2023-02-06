package subway.station.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateLineResponse {
    private String name;
    private String color;

    @Builder
    public UpdateLineResponse(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
