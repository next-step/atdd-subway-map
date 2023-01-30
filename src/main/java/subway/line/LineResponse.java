package subway.line;

public class LineResponse {

  private Long id;
  private String name;

  public LineResponse(Long id, String name) {
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
