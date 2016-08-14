import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Tokenizer {

	private static Tokenizer tokenizer;
	private Scanner in;
	private String currentToken;
	private TokenKind currentTokenKind;
	private String currentWord;

	private static final ArrayList<Character> singleCharTokens = new ArrayList<Character>(
			Arrays.asList(';', ',', '[', ']', '(', ')', '+', '-', '*'));
	private static final ArrayList<Character> equalityTokens = new ArrayList<Character>(
			Arrays.asList('!', '=', '<', '>'));
	private static final ArrayList<String> reservedWords = new ArrayList<String>(
			Arrays.asList("program", "begin", "end", "int", "if", "then", "else", "while", "loop", "read", "write"));

	private Tokenizer() {
	}

	public static Tokenizer create(Scanner in) {
		if (tokenizer == null) {
			tokenizer = new Tokenizer();
			tokenizer.in = in;
		}
		return tokenizer;
	}

	public static Tokenizer getInstance() {
		return tokenizer;
	}

	// Resets the current TokenKind
	public void skipToken() {
		currentTokenKind = null;
	}

	// Returns the actual current token
	public String getRawToken() {
		return this.currentToken;
	}

	// Updates the Tokenizer before returning the token kind
	private TokenKind updateTokenizer(String token, TokenKind tokenKind) {
		if (tokenKind == TokenKind.ERROR) {
			System.err.println("Error: Invalid token");
			System.exit(0);
		}

		currentToken = token;
		currentTokenKind = tokenKind;

		if (currentWord.length() > token.length()) {
			String updatedWord = currentWord.substring(token.length());
			currentWord = updatedWord;
		} else {
			currentWord = null;
		}

		return tokenKind;
	}

	// Returns the current token's kind
	public TokenKind getToken() {
		// If the most recent token has already been found, just return it
		if (currentTokenKind != null) {
			return currentTokenKind;
		} else {
			// If there are no leftovers from the last iteration, pull the next item from the scanner
			if (currentWord == null) {
				if (in.hasNext()) {
					currentWord = in.next();
				} else {
					currentTokenKind = TokenKind.EOF;
					return currentTokenKind;
				}
			}

			// The first character determines which token to look for
			char firstChar = currentWord.charAt(0);

			if (singleCharTokens.contains(firstChar)) {
				return processSingleCharToken(firstChar);
			} else if (equalityTokens.contains(firstChar)) {
				return processEqualityToken(firstChar);
			} else if (firstChar == '&' || firstChar == '|') {
				return processAndOrOperators(firstChar);
			} else if (Character.isUpperCase(firstChar)) {
				return processId();
			} else if (Character.isDigit(firstChar)) {
				return processIntConstant();
			} else if (Character.isLowerCase(firstChar)) {
				return processReservedWord();
			} else {
				return updateTokenizer(null, TokenKind.ERROR);
			}
		}
	}

	// Simply processes the single character
	private TokenKind processSingleCharToken(char c) {
		TokenKind k;

		switch (c) {
		case ';':
			k = TokenKind.SEMICOLON;
			break;
		case ',':
			k = TokenKind.COMMA;
			break;
		case '=':
			k = TokenKind.ASSIGNMENT_OPERATOR;
			break;
		case '!':
			k = TokenKind.NEGATION_OPERATOR;
			break;
		case '[':
			k = TokenKind.LEFT_BRACKET;
			break;
		case ']':
			k = TokenKind.RIGHT_BRACKET;
			break;
		case '(':
			k = TokenKind.LEFT_PARENTHESES;
			break;
		case ')':
			k = TokenKind.RIGHT_PARENTHESES;
			break;
		case '+':
			k = TokenKind.ADDITION_OPERATOR;
			break;
		case '-':
			k = TokenKind.SUBTRACTION_OPERATOR;
			break;
		case '*':
			k = TokenKind.MULTIPLICATION_OPERATOR;
			break;
		case '<':
			k = TokenKind.LESS_THAN_TEST;
			break;
		case '>':
			k = TokenKind.GREATER_THAN_TEST;
			break;
		default:
			k = TokenKind.ERROR;
			break;
		}

		return updateTokenizer(Character.toString(c), k);
	}

	// Determines if there is a single or double character token and processes it - valid either way
	private TokenKind processEqualityToken(char firstChar) {
		if (currentWord.length() > 1 && currentWord.charAt(1) == '=') {
			TokenKind k;

			switch (firstChar) {
			case '!':
				k = TokenKind.NON_EQUALITY_TEST;
				break;
			case '=':
				k = TokenKind.EQUALITY_TEST;
				break;
			case '<':
				k = TokenKind.LESS_THAN_OR_EQUAL_TEST;
				break;
			case '>':
				k = TokenKind.GREATER_THAN_OR_EQUAL_TEST;
				break;
			default:
				k = TokenKind.ERROR;
			}

			return updateTokenizer(Character.toString(firstChar) + "=", k);

		} else {
			return processSingleCharToken(firstChar);
		}
	}

	// Processes AND/OR operators - only valid as double character tokens
	private TokenKind processAndOrOperators(char firstChar) {

		if (currentWord.length() > 1 && currentWord.charAt(1) == firstChar) {

			TokenKind k;

			switch (firstChar) {
			case '&':
				k = TokenKind.AND_OPERATOR;
				break;
			case '|':
				k = TokenKind.OR_OPERATOR;
				break;
			default:
				k = TokenKind.ERROR;
			}

			return updateTokenizer(Character.toString(firstChar) + Character.toString(firstChar), k);

		} else {
			return updateTokenizer(null, TokenKind.ERROR);
		}
	}

	// Peeks at future characters to determine if there is an upcoming special symbol
	private boolean checkForSpecialSymbol(char currentChar, char nextChar) {
		return singleCharTokens.contains(currentChar) || equalityTokens.contains(currentChar)
				|| (currentChar == '&' && nextChar == '&') || currentChar == '|' && nextChar == '|';
	}

	// Gather subsequent uppercase characters followed only by subsequent digits to develop an identifier
	private TokenKind processId() {
		// Gather uppercase letters
		for (int i = 0; i < currentWord.length(); i++) {
			char currentChar = currentWord.charAt(i);

			if (!Character.isUpperCase(currentChar)) {
				// There may be a special symbol not separated by whitespace
				char nextChar = currentWord.length() > (i + 1) ? currentWord.charAt(i + 1) : '0';
				if (checkForSpecialSymbol(currentChar, nextChar)) {
					return updateTokenizer(currentWord.substring(0, i), TokenKind.IDENTIFIER);

					// Gather digits
				} else if (Character.isDigit(currentChar)) {

					for (int j = i; j < currentWord.length(); j++) {
						currentChar = currentWord.charAt(j);

						if (!Character.isDigit(currentChar)) {
							// There may be a special symbol not separated by
							// whitespace
							nextChar = currentWord.length() > (j + 1) ? currentWord.charAt(j + 1) : '0';
							if (checkForSpecialSymbol(currentChar, nextChar)) {
								return updateTokenizer(currentWord.substring(0, j), TokenKind.IDENTIFIER);

							} else {
								return updateTokenizer(null, TokenKind.ERROR);
							}
						}
					}
				} else {
					return updateTokenizer(null, TokenKind.ERROR);
				}
			}
		}

		return updateTokenizer(currentWord, TokenKind.IDENTIFIER);
	}

	// Gather subsequent digits to develop an integer constant
	private TokenKind processIntConstant() {
		for (int i = 0; i < currentWord.length(); i++) {
			char currentChar = currentWord.charAt(i);

			if (!Character.isDigit(currentChar)) {
				// There may be a special character not separated by whitespace
				char nextChar = currentWord.length() > (i + 1) ? currentWord.charAt(i + 1) : '0';
				if (checkForSpecialSymbol(currentChar, nextChar)) {
					return updateTokenizer(currentWord.substring(0, i), TokenKind.INTEGER_CONSTANT);

				} else {
					return updateTokenizer(null, TokenKind.ERROR);
				}
			}
		}

		return updateTokenizer(currentWord, TokenKind.INTEGER_CONSTANT);
	}

	// Returns the TokenKind associated with the reserved word
	private TokenKind getReservedWordKind(String word) {
		switch (reservedWords.get(reservedWords.indexOf(word))) {
		case "program":
			return TokenKind.PROGRAM;
		case "begin":
			return TokenKind.BEGIN;
		case "end":
			return TokenKind.END;
		case "int":
			return TokenKind.INT;
		case "if":
			return TokenKind.IF;
		case "then":
			return TokenKind.THEN;
		case "else":
			return TokenKind.ELSE;
		case "while":
			return TokenKind.WHILE;
		case "loop":
			return TokenKind.LOOP;
		case "read":
			return TokenKind.READ;
		case "write":
			return TokenKind.WRITE;
		default:
			return TokenKind.ERROR;
		}
	}

	// Gather subsequent lowercase chars to develop a lowercase word and determine if it is a reserved word
	private TokenKind processReservedWord() {
		String lowerCaseWord = "";

		for (int i = 0; i < currentWord.length(); i++) {
			char currentChar = currentWord.charAt(i);

			if (!Character.isLowerCase(currentChar)) {
				// There may be a special character not separated by whitespace
				char nextChar = currentWord.length() > (i + 1) ? currentWord.charAt(i + 1) : '0';
				if (checkForSpecialSymbol(currentChar, nextChar) && reservedWords.contains(lowerCaseWord)) {
					return updateTokenizer(currentWord.substring(0, i), getReservedWordKind(lowerCaseWord));

				} else {
					return updateTokenizer(null, TokenKind.ERROR);
				}

			} else {
				lowerCaseWord += currentChar;
			}
		}

		if (reservedWords.contains(lowerCaseWord)) {
			return updateTokenizer(currentWord, getReservedWordKind(lowerCaseWord));
		}
		return updateTokenizer(null, TokenKind.ERROR);
	}
	
}
