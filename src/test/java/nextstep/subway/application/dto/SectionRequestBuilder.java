package nextstep.subway.application.dto;

public class SectionRequestBuilder {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private SectionRequestBuilder() {
    }

    public static SectionRequestBuilder ofDefault() {
        return new SectionRequestBuilder()
                .withUpStationId(-1L)
                .withDownStationId(-1L)
                .withDistance(0);
    }

    public SectionRequestBuilder withUpStationId(Long upStationId) {
        this.upStationId = upStationId;
        return this;
    }

    public SectionRequestBuilder withDownStationId(Long downStationId) {
        this.downStationId = downStationId;
        return this;
    }

    public SectionRequestBuilder withDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public SectionRequest build() {
        return new SectionRequest(upStationId, downStationId, distance);
    }

}
