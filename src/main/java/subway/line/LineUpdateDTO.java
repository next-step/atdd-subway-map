package subway.line;

public class LineUpdateDTO {
    private Long id;
    private String name;
    private String color;

    public LineUpdateDTO(Long id, LineUpdateRequest request) {
        this.id = id;
        this.name = request.getName();
        this.color = request.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
