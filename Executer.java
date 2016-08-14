import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Executer {
	
	private static ParseTree p = ParseTree.getInstance();
	private Scanner in;
	private Map<String, Integer> lookupTable = new HashMap<String, Integer>();

	public Executer(Scanner in) {
		this.in = in;
	}

	// EXECUTION METHODS

	public void execProg() {
		p.goDownLB();
		execDeclSeq();
		p.goUP();
		p.goDownMB();
		execStmtSeq();
		p.goUP();
	}

	private void execDeclSeq() {
		p.goDownLB();
		execDecl();
		p.goUP();

		if (p.currAltNo() == 2) {
			p.goDownMB();
			execDeclSeq();
			p.goUP();
		}
	}

	private void execStmtSeq() {
		p.goDownLB();
		execStmt();
		p.goUP();

		if (p.currAltNo() == 2) {
			p.goDownMB();
			execStmtSeq();
			p.goUP();
		}
	}

	private void execDecl() {
		p.goDownLB();
		ArrayList<String> idList = execIdList();
		p.goUP();

		for (String id : idList) {
			lookupTable.put(id, null);
		}
	}

	private ArrayList<String> execIdList() {
		ArrayList<String> idList = new ArrayList<String>();

		p.goDownLB();
		idList.add(execId());
		p.goUP();

		if (p.currAltNo() == 2) {
			p.goDownMB();
			idList.addAll(execIdList());
			p.goUP();
		}

		return idList;
	}

	private void execStmt() {
		switch (p.currAltNo()) {
		case 1:
			p.goDownLB();
			execAssign();
			p.goUP();
			break;
		case 2:
			p.goDownLB();
			execIf();
			p.goUP();
			break;
		case 3:
			p.goDownLB();
			execLoop();
			p.goUP();
			break;
		case 4:
			p.goDownLB();
			execIn();
			p.goUP();
			break;
		case 5:
			p.goDownLB();
			execOut();
			p.goUP();
			break;
		default:
			break;
		}
	}

	private void execAssign() {
		p.goDownLB();
		String id = execId();
		p.goUP();
		p.goDownMB();
		int val = execExp();
		p.goUP();

		if (lookupTable.containsKey(id)) {
			lookupTable.put(id, val);
		} else {
			System.err.println("Error: Undeclared identifier");
			System.exit(0);
		}
	}

	private void execIf() {
		p.goDownLB();
		boolean b = execCond();
		p.goUP();

		if (b) {
			p.goDownMB();
			execStmtSeq();
			p.goUP();
		} else if (p.currAltNo() == 2) {
			p.goDownRB();
			execStmtSeq();
			p.goUP();
		}
	}

	private void execLoop() {
		p.goDownLB();
		boolean b = execCond();
		p.goUP();

		while (b) {
			p.goDownMB();
			execStmtSeq();
			p.goUP();
			p.goDownLB();
			b = execCond();
			p.goUP();
		}
	}

	private void execIn() {
		p.goDownLB();
		ArrayList<String> idList = execIdList();
		p.goUP();

		for (String id : idList) {
			if (lookupTable.containsKey(id)) {
				try {
					lookupTable.put(id, in.nextInt());
				} catch (Exception e) {
					System.err.println("Error: No input data");
					System.exit(0);
				}
			} else {
				System.err.println("Error: Undeclared identifier");
				System.exit(0);
			}
		}
	}

	private void execOut() {
		p.goDownLB();
		ArrayList<String> idList = execIdList();
		p.goUP();

		for (String id : idList) {
			Integer val = lookupTable.get(id);
			if (val != null) {
				System.out.println(id + " = " + val);
			} else {
				System.err.println("Error: Uninitialized identifier used");
				System.exit(0);
			}
		}
	}

	private boolean execCond() {
		p.goDownLB();
		boolean cond = execComp();
		p.goUP();

		switch (p.currAltNo()) {
		case 1:
			return cond;
		case 2:
			return !cond;
		case 3:
			p.goDownMB();
			boolean condAnd = execCond();
			p.goUP();
			return cond && condAnd;
		case 4:
			p.goDownMB();
			boolean condOr = execCond();
			p.goUP();
			return cond || condOr;
		default:
			return false;
		}
	}

	private boolean execComp() {
		p.goDownLB();
		int op1 = execOp();
		p.goUP();
		p.goDownRB();
		int op2 = execOp();
		p.goUP();
		p.goDownMB();
		int compOp = execCompOp();
		p.goUP();

		switch (compOp) {
		case 1:
			return op1 != op2;
		case 2:
			return op1 == op2;
		case 3:
			return op1 < op2;
		case 4:
			return op1 > op2;
		case 5:
			return op1 <= op2;
		case 6:
			return op1 >= op2;
		default:
			return false;
		}
	}

	private int execExp() {
		p.goDownLB();
		int exp = execTrm();
		p.goUP();

		switch (p.currAltNo()) {
		case 2:
			p.goDownMB();
			exp += execExp();
			p.goUP();
			break;
		case 3:
			p.goDownMB();
			exp -= execExp();
			p.goUP();
			break;
		default:
			break;
		}

		return exp;
	}

	private int execTrm() {
		p.goDownLB();
		int trm = execOp();
		p.goUP();

		if (p.currAltNo() == 2) {
			p.goDownMB();
			trm *= execTrm();
			p.goUP();
		}

		return trm;
	}

	private int execOp() {		
		int op = 0;

		switch (p.currAltNo()) {
		case 1:
			p.goDownLB();
			op = execNo();
			p.goUP();
			break;
		case 2:
			p.goDownLB();
			op = lookupTable.get(execId());
			p.goUP();
			break;
		case 3:
			p.goDownLB();
			op = execExp();
			p.goUP();
			break;
		default:
			break;
		}

		return op;
	}

	private int execCompOp() {
		return p.currAltNo();
	}

	private String execId() {
		return p.currVal();
	}

	private int execNo() {
		return Integer.parseInt(p.currVal());
	}
	
}
