package subway.domain;

public class LineUpdateDomain {

    private final Long lineId;
    private final String name;
    private final String color;

    public LineUpdateDomain(Long lineId, String name, String color) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
