package subway.lane;

public class LaneResponse {

  private Long id;
  private String name;

  public LaneResponse(Long id, String name) {
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
