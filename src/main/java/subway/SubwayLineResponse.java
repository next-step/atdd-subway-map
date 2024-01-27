package subway;

public class SubwayLineResponse {
    private Long id;
    private String name;

    public SubwayLineResponse(Long id, String name) {
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
