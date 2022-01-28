package nextstep.subway.station.domain.dto;

import java.time.LocalDateTime;

import nextstep.subway.station.domain.model.Station;

public class StationResponse {
    private final Long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    private StationResponse(long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static StationResponse from(Station station) {
        return builder()
            .id(station.getId())
            .name(station.getName())
            .createdDate(station.getCreatedDate())
            .modifiedDate(station.getModifiedDate())
            .build();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public static class Builder {
        private Long id;
        private String name;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public StationResponse build() {
            return new StationResponse(
                id, name, createdDate, modifiedDate
            );
        }
    }
}
