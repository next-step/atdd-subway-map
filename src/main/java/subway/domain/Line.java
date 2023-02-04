package subway.domain;

public class Line {

    private Long id;
    private String name;
    private String color;

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static Line withoutId(String name, String color) {
        return new Line(null, name, color);
    }

    public static Line withId(Long id, String name, String color) {
        return new Line(id, name, color);
    }

    public void updateLine(String name, String color) {
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
        return color;
    }

}
