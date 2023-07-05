package subway.dto;

public class StationRequest {

    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    public StationDto toDto() {
        return new StationDto(
                name
        );
    }
}
