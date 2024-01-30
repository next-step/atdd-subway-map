package subway.location.enums;

import java.util.function.Function;

public enum Location {
	LINES("/lines", path -> "/lines/" + path),
	STATIONS("/stations", path -> "/stations/" + path);

	private final String uri;
	private final Function<String, String> expression;

	Location(String uri, Function<String, String> expression) {
		this.uri = uri;
		this.expression = expression;
	}

	public String path() {
		return uri;
	}

	public String addPath(Object path) {
		return expression.apply(String.valueOf(path));
	}
}
