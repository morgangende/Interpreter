
public class Parser {
	
	private static Tokenizer t = Tokenizer.getInstance();
	private static ParseTree p = ParseTree.getInstance();
	
	// PARSING METHODS

	public void parseProg() {
		p.setAltNo(1);
		p.createBranch(2);

		if (t.getToken() == TokenKind.PROGRAM) {
			t.skipToken();
			p.goDownLB();
			parseDeclSeq();
			p.goUP();

			if (t.getToken() == TokenKind.BEGIN) {
				t.skipToken();
				p.goDownMB();
				parseStmtSeq();
				p.goUP();

				if (t.getToken() == TokenKind.END) {
					t.skipToken();

				} else {
					System.err.println("Error: Invalid program");
					System.exit(0);
				}
			} else {
				System.err.println("Error: Invalid program");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid program");
			System.exit(0);
		}
	}

	private void parseDeclSeq() {
		p.setAltNo(1);
		p.createBranch(1);

		p.goDownLB();
		parseDecl();
		p.goUP();

		if (t.getToken() != TokenKind.BEGIN) {
			p.setAltNo(2);
			p.createBranch(1);

			p.goDownMB();
			parseDeclSeq();
			p.goUP();
		}
	}

	private void parseStmtSeq() {
		p.setAltNo(1);
		p.createBranch(1);

		p.goDownLB();
		parseStmt(t);
		p.goUP();

		if (t.getToken() != TokenKind.END && t.getToken() != TokenKind.ELSE) {
			p.setAltNo(2);
			p.createBranch(1);

			p.goDownMB();
			parseStmtSeq();
			p.goUP();
		}
	}

	private void parseDecl() {
		if (t.getToken() == TokenKind.INT) {
			t.skipToken();

			p.setAltNo(1);
			p.createBranch(1);

			p.goDownLB();
			parseIdList();
			p.goUP();

			if (t.getToken() == TokenKind.SEMICOLON) {
				t.skipToken();

			} else {
				System.err.println("Error: Invalid declaration");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid declaration");
			System.exit(0);
		}
	}

	private void parseIdList() {
		p.setAltNo(1);
		p.createBranch(1);

		p.goDownLB();
		parseId();
		p.goUP();

		if (t.getToken() == TokenKind.COMMA) {
			t.skipToken();

			p.setAltNo(2);
			p.createBranch(1);

			p.goDownMB();
			parseIdList();
			p.goUP();
		}
	}

	private void parseStmt(Tokenizer t) {
		p.createBranch(1);

		switch (t.getToken()) {
		case IDENTIFIER:
			p.setAltNo(1);
			p.goDownLB();
			parseAssign();
			p.goUP();
			break;
		case IF:
			p.setAltNo(2);
			p.goDownLB();
			parseIf();
			p.goUP();
			break;
		case WHILE:
			p.setAltNo(3);
			p.goDownLB();
			parseLoop();
			p.goUP();
			break;
		case READ:
			p.setAltNo(4);
			p.goDownLB();
			parseIn();
			p.goUP();
			break;
		case WRITE:
			p.setAltNo(5);
			p.goDownLB();
			parseOut();
			p.goUP();
			break;
		default:
			System.err.println("Error: Invalid statement");
			System.exit(0);
			break;
		}
	}

	private void parseAssign() {
		if (t.getToken() == TokenKind.IDENTIFIER) {
			p.setAltNo(1);
			p.createBranch(2);

			p.goDownLB();
			parseId();
			p.goUP();

			if (t.getToken() == TokenKind.ASSIGNMENT_OPERATOR) {
				t.skipToken();

				p.goDownMB();
				parseExp();
				p.goUP();

				if (t.getToken() == TokenKind.SEMICOLON) {
					t.skipToken();

				} else {
					System.err.println("Error: Invalid assigment");
					System.exit(0);
				}
			} else {
				System.err.println("Error: Invalid assigment");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid assigment");
			System.exit(0);
		}
	}

	private void parseIf() {
		if (t.getToken() == TokenKind.IF) {
			t.skipToken();

			p.setAltNo(1);
			p.createBranch(2);

			p.goDownLB();
			parseCond();
			p.goUP();

			if (t.getToken() == TokenKind.THEN) {
				t.skipToken();

				p.goDownMB();
				parseStmtSeq();
				p.goUP();

				if (t.getToken() == TokenKind.ELSE) {
					t.skipToken();

					p.setAltNo(2);
					p.createBranch(1);

					p.goDownRB();
					parseStmtSeq();
					p.goUP();
				}

				if (t.getToken() == TokenKind.END) {
					t.skipToken();

					if (t.getToken() == TokenKind.SEMICOLON) {
						t.skipToken();

					} else {
						System.err.println("Error: Invalid if statement");
						System.exit(0);
					}
				} else {
					System.err.println("Error: Invalid if statement");
					System.exit(0);
				}
			} else {
				System.err.println("Error: Invalid if statement");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid if statement");
			System.exit(0);
		}
	}

	private void parseLoop() {
		if (t.getToken() == TokenKind.WHILE) {
			t.skipToken();

			p.setAltNo(1);
			p.createBranch(2);

			p.goDownLB();
			parseCond();
			p.goUP();

			if (t.getToken() == TokenKind.LOOP) {
				t.skipToken();

				p.goDownMB();
				parseStmtSeq();
				p.goUP();

				if (t.getToken() == TokenKind.END) {
					t.skipToken();

					if (t.getToken() == TokenKind.SEMICOLON) {
						t.skipToken();

					} else {
						System.err.println("Error: Invalid loop");
						System.exit(0);
					}
				} else {
					System.err.println("Error: Invalid loop");
					System.exit(0);
				}
			} else {
				System.err.println("Error: Invalid loop");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid loop");
			System.exit(0);
		}
	}

	private void parseIn() {
		if (t.getToken() == TokenKind.READ) {
			t.skipToken();

			p.setAltNo(1);
			p.createBranch(1);

			p.goDownLB();
			parseIdList();
			p.goUP();

			if (t.getToken() == TokenKind.SEMICOLON) {
				t.skipToken();

			} else {
				System.err.println("Error: Invalid read statement");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid read statement");
			System.exit(0);
		}
	}

	private void parseOut() {
		if (t.getToken() == TokenKind.WRITE) {
			t.skipToken();

			p.setAltNo(1);
			p.createBranch(1);

			p.goDownLB();
			parseIdList();
			p.goUP();

			if (t.getToken() == TokenKind.SEMICOLON) {
				t.skipToken();

			} else {
				System.err.println("Error: Invalid write statement");
				System.exit(0);
			}
		} else {
			System.err.println("Erro: Invalid write statement");
			System.exit(0);
		}
	}

	private void parseCond() {
		p.createBranch(1);

		if (t.getToken() == TokenKind.NEGATION_OPERATOR) {
			t.skipToken();

			p.setAltNo(2);

			p.goDownLB();
			parseCond();
			p.goUP();

		} else if (t.getToken() == TokenKind.LEFT_BRACKET) {
			t.skipToken();

			p.createBranch(1);

			p.goDownLB();
			parseCond();
			p.goUP();

			if (t.getToken() == TokenKind.AND_OPERATOR) {
				t.skipToken();

				p.setAltNo(3);

				p.goDownMB();
				parseCond();
				p.goUP();

			} else if (t.getToken() == TokenKind.OR_OPERATOR) {
				t.skipToken();

				p.setAltNo(4);

				p.goDownMB();
				parseCond();
				p.goUP();

			} else {
				System.err.println("Error: Invalid condition");
				System.exit(0);
			}

			if (t.getToken() == TokenKind.RIGHT_BRACKET) {
				t.skipToken();

			} else {
				System.err.println("Error: Invalid condition");
				System.exit(0);
			}
		} else {
			p.setAltNo(1);

			p.goDownLB();
			parseComp();
			p.goUP();
		}
	}

	private void parseComp() {
		if (t.getToken() == TokenKind.LEFT_PARENTHESES) {
			t.skipToken();

			p.setAltNo(1);
			p.createBranch(3);

			p.goDownLB();
			parseOp();
			p.goUP();

			p.goDownMB();
			parseCompOp();
			p.goUP();

			p.goDownRB();
			parseOp();
			p.goUP();

			if (t.getToken() == TokenKind.RIGHT_PARENTHESES) {
				t.skipToken();

			} else {
				System.err.println("Error: Invalid comparator");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid comparator");
			System.exit(0);
		}
	}

	private void parseExp() {
		p.setAltNo(1);
		p.createBranch(1);

		p.goDownLB();
		parseTrm();
		p.goUP();

		if (t.getToken() == TokenKind.ADDITION_OPERATOR) {
			t.skipToken();

			p.setAltNo(2);
			p.createBranch(1);

			p.goDownMB();
			parseExp();
			p.goUP();

		} else if (t.getToken() == TokenKind.SUBTRACTION_OPERATOR) {
			t.skipToken();

			p.setAltNo(3);
			p.createBranch(1);

			p.goDownMB();
			parseExp();
			p.goUP();
		}
	}

	private void parseTrm() {
		p.setAltNo(1);
		p.createBranch(1);

		p.goDownLB();
		parseOp();
		p.goUP();

		if (t.getToken() == TokenKind.MULTIPLICATION_OPERATOR) {
			t.skipToken();

			p.setAltNo(2);
			p.createBranch(1);

			p.goDownMB();
			parseTrm();
			p.goUP();
		}
	}

	private void parseOp() {
		p.createBranch(1);

		if (t.getToken() == TokenKind.INTEGER_CONSTANT) {
			p.setAltNo(1);

			p.goDownLB();
			parseNo();
			p.goUP();

		} else if (t.getToken() == TokenKind.IDENTIFIER) {
			p.setAltNo(2);

			p.goDownLB();
			parseId();
			p.goUP();

		} else if (t.getToken() == TokenKind.LEFT_PARENTHESES) {
			t.skipToken();

			p.setAltNo(3);

			p.goDownLB();
			parseExp();
			p.goUP();

			if (t.getToken() == TokenKind.RIGHT_PARENTHESES) {
				t.skipToken();

			} else {
				System.err.println("Error: Invalid operator");
				System.exit(0);
			}
		} else {
			System.err.println("Error: Invalid operator");
			System.exit(0);
		}
	}

	// Terminal node
	private void parseCompOp() {

		switch (t.getToken()) {
		case NON_EQUALITY_TEST:
			t.skipToken();
			p.setAltNo(1);
			break;
		case EQUALITY_TEST:
			t.skipToken();
			p.setAltNo(2);
			break;
		case LESS_THAN_TEST:
			t.skipToken();
			p.setAltNo(3);
			break;
		case GREATER_THAN_TEST:
			t.skipToken();
			p.setAltNo(4);
			break;
		case LESS_THAN_OR_EQUAL_TEST:
			t.skipToken();
			p.setAltNo(5);
			break;
		case GREATER_THAN_OR_EQUAL_TEST:
			t.skipToken();
			p.setAltNo(6);
			break;
		default:
			System.err.println("Error: Invalid comparison operator");
			System.exit(0);
			break;
		}
	}

	// Terminal node
	private void parseId() {
		p.setAltNo(1);

		if (t.getToken() == TokenKind.IDENTIFIER) {
			p.setVal(t.getRawToken());
			t.skipToken();
		} else {
			System.err.println("Error: Invalid identifier");
			System.exit(0);
		}
	}
 
	// Terminal node
	private void parseNo() {
		p.setAltNo(1);

		if (t.getToken() == TokenKind.INTEGER_CONSTANT) {
			p.setVal(t.getRawToken());
			t.skipToken();
		} else {
			System.err.println("Error: Invalid integer constant");
			System.exit(0);
		}
	}
}
