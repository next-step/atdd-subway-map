package subway.line.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineModifyRequest {

    private String name;
    private String color;

    @Builder
    public LineModifyRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

}