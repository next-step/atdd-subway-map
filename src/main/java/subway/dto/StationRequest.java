package subway.dto;

import lombok.Getter;

@Getter
public class StationRequest {

    private Long id;
    private String name;

    public StationDto toDto() {
        return new StationDto(
                name
        );
    }
}
