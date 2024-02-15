package subway.line;

public class SectionResponse {

  Long id;

  public Long getId() {
    return id;
  }

  public SectionResponse() {
  }

  public SectionResponse(Long id) {
    this.id = id;
  }

  public static SectionResponse from(Section section) {
    return new SectionResponse(section.getId());
  }
}
