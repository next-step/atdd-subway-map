package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public static LineResponse convertedByEntity(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

}
