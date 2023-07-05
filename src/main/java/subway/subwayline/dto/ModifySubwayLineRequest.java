package subway.subwayline.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ModifySubwayLineRequest {
    private String name;
    private String color;

    @Builder
    public ModifySubwayLineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
