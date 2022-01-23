package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineRequest {
    private String name;
    private String color;

    @Builder
    private LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
