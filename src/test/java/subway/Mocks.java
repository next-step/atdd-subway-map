package subway;

import subway.line.LineCreateRequest;
import subway.line.LineTestDTO.LineStationCreateDTO;
import subway.section.SectionCreateRequest;

public class Mocks {

  private static final Long 서울2호선_거리 = 10l;
  private static final Long 신분당선_거리 = 20l;

  public static class MockStation {
    public static String 서울대입구역 = "서울대입구역";
    public static String 봉천역 = "봉천역";
    public static String 신림역 = "신림역";
    public static String 강남역 = "강남역";
    public static String 신사역 = "신사역";
  }

  public static class Color {
    public static String 초록 = "초록";
    public static String 노랑 = "노랑";
  }

  public static class MockLine {
    public static String 서울2호선 = "서울2호선";
    public static String 신분당선 = "신분당선";
  }

  public static class LineCreateRequestDTO {
    public static LineStationCreateDTO 서울2호선_노선_생성요청 = new LineStationCreateDTO(
        MockLine.서울2호선,
        Color.초록,
        MockStation.서울대입구역,
        MockStation.봉천역,
        서울2호선_거리
    );

    public static LineStationCreateDTO 신분당선_노선_생성요청 = new LineStationCreateDTO(
        MockLine.신분당선,
        Color.노랑,
        MockStation.강남역,
        MockStation.신사역,
        신분당선_거리
    );
  }
}
