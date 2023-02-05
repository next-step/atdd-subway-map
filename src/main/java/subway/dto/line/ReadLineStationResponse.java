package subway.dto.line;

import lombok.Getter;

@Getter
public class ReadLineStationResponse {
    private final Long id;
    private final String name;

    public ReadLineStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
