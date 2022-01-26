package nextstep.subway.application.dto;

public class LineRequestBuilder {
    public static final String DEFAULT_LINE_NAME = "신분당선";
    public static final String DEFAULT_LINE_COLOR = "bg-red-600";
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private LineRequestBuilder() {
    }

    public static LineRequestBuilder ofDefault() {
        return new LineRequestBuilder()
                .withName(DEFAULT_LINE_NAME)
                .withColor(DEFAULT_LINE_COLOR)
                .withUpStationId(-1L)
                .withDownStationId(-1L)
                .withDistance(0);
    }

    public LineRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LineRequestBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public LineRequestBuilder withUpStationId(Long upStationId) {
        this.upStationId = upStationId;
        return this;
    }

    public LineRequestBuilder withDownStationId(Long downStationId) {
        this.downStationId = downStationId;
        return this;
    }

    public LineRequestBuilder withDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public LineRequest build() {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

}
