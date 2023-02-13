package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineApi.지하철노선_이름_목록_조회;
import static subway.line.LineFixture.LINE_UPDATE_REQUEST;

public class LineAssertion {

    public static void 생성한_지하철노선과_조회된_지하철노선은_같다(final LineResponse 생성한_지하철노선, final LineResponse 조회된_지하철노선) {
        assertThat(조회된_지하철노선).isEqualTo(생성한_지하철노선);
    }

    public static void 지하철노선_수정후_지하철노선의_이름과_색상이_바뀌어있다(final LineResponse 수정후_조회된_지하철노선) {
        assertThat(수정후_조회된_지하철노선.getName()).isEqualTo(LINE_UPDATE_REQUEST.getName());
        assertThat(수정후_조회된_지하철노선.getColor()).isEqualTo(LINE_UPDATE_REQUEST.getColor());
    }

    public static void 지하철노선_삭제후_목록조회하면_찾을_수_없다(final LineResponse 삭제한_지하철노선) {
        assertThat(지하철노선_이름_목록_조회()).doesNotContain(삭제한_지하철노선.getName());
    }
}
