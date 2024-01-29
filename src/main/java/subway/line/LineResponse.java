package subway.line;

import subway.Stations;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Stations stations;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
