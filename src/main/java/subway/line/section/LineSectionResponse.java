package subway.line.section;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LineSectionResponse {

  private Long sectionId;
  private Long lineId;
  private Long upStationId;
  private Long downStationId;

  public static LineSectionResponse fromEntity(LineSection entity) {
    var response = new LineSectionResponse();
    response.sectionId = entity.getSectionId();
    response.lineId = entity.getLineId();
    response.upStationId = entity.getUpStationId();
    response.downStationId = entity.getDownStationId();
    return response;
  }
}
