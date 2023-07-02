package subway.line;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LineResponse {
    private Long id;
    private String name;
    private String color;

    public LineResponse(@JsonProperty("id")Long id, @JsonProperty("name") String name, @JsonProperty("color") String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return this.color;
    }
}
