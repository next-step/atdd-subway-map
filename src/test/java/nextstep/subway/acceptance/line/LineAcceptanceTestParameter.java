package nextstep.subway.acceptance.line;

public class LineAcceptanceTestParameter {

	String 노선이름;
	String 노선색상;
	String 상행_종점역;
	String 하행_종점역;
	int 종점역_간_거리;

	LineAcceptanceTestParameter(String 노선이름, String 노선색상, String 상행_종점역, String 하행_종점역, int 종점역_간_거리) {
		this.노선이름 = 노선이름;
		this.노선색상 = 노선색상;
		this.상행_종점역 = 상행_종점역;
		this.하행_종점역 = 하행_종점역;
		this.종점역_간_거리 = 종점역_간_거리;
	}

	LineAcceptanceTestParameter(String 노선이름, String 노선색상) {
		this.노선이름 = 노선이름;
		this.노선색상 = 노선색상;
	}
}
