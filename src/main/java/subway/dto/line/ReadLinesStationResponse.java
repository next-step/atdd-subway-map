package subway.dto.line;

import lombok.Getter;

@Getter
public class ReadLinesStationResponse {
    private final Long id;
    private final String name;

    public ReadLinesStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
