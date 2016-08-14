
public class Printer {

	private static ParseTree p = ParseTree.getInstance();
	private int indent = 0;
	private static final int TAB_SIZE = 4;
	
	// HELPER METHODS
	
	// Prints the indent at the beginning of a new line
	private void newLine() {
		for(int i = 0; i < indent; i++) {
			for(int j = 0; j < TAB_SIZE; j++) {
			System.out.print(" ");
			}
		}
	}
	
	// Prints the string with appropriate whitespace before and after
	private void write(String s, int spaceBefore, int spaceAfter) {
		switch(spaceBefore) {
		case 1: 
			System.out.print(" ");
			break;
		case 2: 
			newLine();
			break;
		default: 
			break;
		}
		
		System.out.print(s);

		switch(spaceAfter) {
		case 1:
			System.out.print(" ");
			break;
		case 2:
			System.out.print("\n");
			break;
		case 3:
			System.out.print("\n\n");
		default:
		}
	}
	
	// PRINTING METHODS
	
	public void printProg() {
		write("program", 2, 2);
		indent++;
		p.goDownLB();
		printDeclSeq();
		p.goUP();
		indent--;
		
		write("begin", 2, 2);
		indent++;
		p.goDownMB();
		printStmtSeq();
		indent--;
		p.goUP();
		write("end", 2, 3);
	}

	private void printDeclSeq() {
		p.goDownLB();
		printDecl();
		p.goUP();
		
		if(p.currAltNo() == 2) {
			p.goDownMB();
			printDeclSeq();
			p.goUP();
		}
	}

	private void printStmtSeq() {
		p.goDownLB();
		printStmt();
		p.goUP();
		
		if(p.currAltNo() == 2) {
			p.goDownMB();
			printStmtSeq();
			p.goUP();
		}		
	}
	
	private void printDecl() {
		write("int", 2, 1);
		p.goDownLB();
		printIdList();
		p.goUP();
		write(";", 0, 2);
	}

	private void printIdList() {
		p.goDownLB();
		printId();
		p.goUP();
		
		if(p.currAltNo() == 2) {
			write(",", 0, 1);
			p.goDownMB();
			printIdList();
			p.goUP();
		}
	}
	
	private void printStmt() {		
		switch(p.currAltNo()) {
		case 1:
			p.goDownLB();
			printAssign();
			p.goUP();
			break;
		case 2:
			p.goDownLB();
			printIf();
			p.goUP();
			break;
		case 3:
			p.goDownLB();
			printLoop();
			p.goUP();
			break;
		case 4:
			p.goDownLB();
			printIn();
			p.goUP();
			break;
		case 5:
			p.goDownLB();
			printOut();
			p.goUP();
			break;
		default: 
			break;
		}
	}
	
	private void printAssign() {
		write("", 2, 0);
		p.goDownLB();
		printId();
		p.goUP();
		write(" =", 0, 1);
		p.goDownMB();
		printExp();
		p.goUP();
		write(";", 0, 2);
	}
	
	private void printIf() {
		write("if", 2, 1);
		p.goDownLB();
		printCond();
		p.goUP();
		write("then", 0, 2);
		indent++;
		p.goDownMB();
		printStmtSeq();
		p.goUP();
		
		if(p.currAltNo() == 2) {
			indent--;
			write("else", 2, 2);
			indent++;
			p.goDownRB();
			printStmtSeq();
			p.goUP();
		}
		
		indent--;
		write("end;", 2, 2);
	}
	
	private void printLoop() {
		write("while", 2, 1);
		p.goDownLB();
		printCond();
		p.goUP();
		write("loop", 0, 2);
		indent ++;
		p.goDownMB();
		printStmtSeq();
		p.goUP();
		indent--;
		write("end;", 2, 2);
	}
	
	private void printIn() {
		write("read", 2, 1);
		p.goDownLB();
		printIdList();
		p.goUP();
		write(";", 0, 2);
	}
	
	private void printOut() {
		write("write", 2, 1);
		p.goDownLB();
		printIdList();
		p.goUP();
		write(";", 0, 2);
	}
	
	private void printCond() {
		switch(p.currAltNo()) {
		case 1:
			p.goDownLB();
			printComp();
			p.goUP();
			break;
		case 2:
			write("!", 0, 0);
			
			p.goDownLB();
			printCond();
			p.goUP();
			break;
		case 3:
			write("[", 0, 0);
			
			p.goDownLB();
			printCond();
			p.goUP();
			
			write("&&", 1, 1);
			
			p.goDownMB();
			printCond();
			p.goUP();
			
			write("]", 0, 1);
			break;
		case 4:
			write("[", 0, 0);
			
			p.goDownLB();
			printCond();
			p.goUP();
			
			write("||", 1, 1);
			
			p.goDownMB();
			printCond();
			p.goUP();
			
			write("]", 0, 1);
			break;
		default:
			break;	
		}
	}
	
	private void printComp() {
		write("(", 0, 0);
		p.goDownLB();
		printOp();
		p.goUP();
		p.goDownMB();
		printCompOp();
		p.goUP();
		p.goDownRB();
		printOp();
		p.goUP();
		write(")", 0, 1);
	}
	
	private void printExp() {
		p.goDownLB();
		printTrm();
		p.goUP();
		
		if(p.currAltNo() == 2) {
			write("+", 1, 1);
			p.goDownMB();
			printExp();
			p.goUP();
			
		} else if(p.currAltNo() == 3) {
			write("-", 1, 1);
			p.goDownMB();
			printExp();
			p.goUP();
		}
	}
	
	private void printTrm() {
		p.goDownLB();
		printOp();
		p.goUP();
		
		if(p.currAltNo() == 2) {
			write("*", 1, 1);
			p.goDownMB();
			printTrm();
			p.goUP();
		}
	}
	
	private void printOp() {
		switch(p.currAltNo()) {
		case 1:
			p.goDownLB();
			printNo();
			p.goUP();
			break;
		case 2:
			p.goDownLB();
			printId();
			p.goUP();
			break;
		case 3:
			System.out.print("(");
			p.goDownLB();
			printExp();
			p.goUP();			
			write(")", 0, 1);
			break;
		default:
			break;
		}
	}
	
	private void printCompOp() {
		switch(p.currAltNo()) {
		case 1:
			write("!=", 1, 1);
			break;
		case 2:
			write("==", 1, 1);
			break;
		case 3:
			write("<", 1, 1);
			break;
		case 4:
			write(">", 1, 1);
			break;
		case 5:
			write("<=", 1, 1);
			break;
		case 6:
			write(">=", 1, 1);
			break;
		default:
			break;
		}
	}
	
	private void printId() {
		write(p.currVal(), 0, 0);
	}

	private void printNo() {
		write(p.currVal(), 0, 0);
	}
}
