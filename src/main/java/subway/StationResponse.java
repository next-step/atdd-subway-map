package subway;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(@JsonProperty("id")Long id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
