package subway;

public class BuildLineResponse {
    private Long id;
    private String name;

    public BuildLineResponse(Long id, String name) {
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
