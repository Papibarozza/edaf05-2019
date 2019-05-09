package test;

import java.util.ArrayDeque;

public class Jakob {

	public static void main(String[] args) {

		String test = "+ 10 10";
		String[] split = test.split("\\s+");

		ArrayDeque<String> tokenStack = new ArrayDeque<>();
		for (String c : split) {
			tokenStack.add(c);
		}
		System.out.println(parse(tokenStack));

	}

	public static int parse(ArrayDeque<String> tokens) {
		String currentToken = tokens.poll();
		switch (currentToken) {
		case "+":
			return parse(tokens) + parse(tokens);
		case "-":
			return parse(tokens) - parse(tokens);
		case "*":
			return parse(tokens) * parse(tokens);

		default:
			return Integer.valueOf(currentToken);
		}

	}

}
