package subway.line;

import java.util.Objects;

public class LineResponse {

    private Long id;
    private String name;
    private Color color;

    public LineResponse(Long id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
