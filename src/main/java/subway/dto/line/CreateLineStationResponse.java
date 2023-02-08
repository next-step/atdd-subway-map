package subway.dto.line;

import lombok.Getter;

@Getter
public class CreateLineStationResponse {
    private final Long id;
    private final String name;

    public CreateLineStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
