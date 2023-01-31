package subway.web.request;

import subway.domain.LineUpdateDto;

public class LineUpdateRequest {

    private final String name;
    private final String color;

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LineUpdateDto toDomain(Long lineId) {
        return new LineUpdateDto(lineId, this.name, this.color);
    }

}
