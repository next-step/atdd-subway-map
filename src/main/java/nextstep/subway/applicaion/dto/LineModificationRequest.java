package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class LineModificationRequest {
    private final String name;
    private final String color;

    public LineModificationRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
