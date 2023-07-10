package subway.dto;

public class StationRequest {
    private String name;

    private StationRequest() {

    }

    public String getName() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public StationRequest build() {
            StationRequest stationRequest = new StationRequest();
            stationRequest.name = this.name;
            return stationRequest;
        }
    }
}
