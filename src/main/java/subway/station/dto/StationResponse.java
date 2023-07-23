package subway.station.dto;

import lombok.Builder;

@Builder
public record StationResponse(Long id, String name) {
}
