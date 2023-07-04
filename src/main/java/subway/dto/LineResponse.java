package subway.dto;

public class LineResponse {

    private int id;
    private String name;

    public LineResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
