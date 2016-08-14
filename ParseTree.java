
public class ParseTree {	

	private static ParseTree p;
	private static Node currentNode;
	
	private ParseTree() {}
	
	public static ParseTree getInstance() {
		if(p == null) {
			p = new ParseTree();
			currentNode = new Node();
		}
		return p;
	}
	
	// GETTERS
	public int currAltNo() {
		return currentNode.getAltNo();
	}
	
	public String currVal() {
		return currentNode.getVal();
	}
	
	// SETTERS
	
	public void setAltNo(int no) {
		currentNode.setAltNo(no);
	}
	
	public void setVal(String val) {
		currentNode.setVal(val);
	}
	
	public void createBranch(int numBranches) {
		for(int i = 0; i < numBranches; i++) {
			Node child = new Node();
			child.setParent(currentNode);
			currentNode.addChild(child);
		}
	}		
	
	// TRAVERSAL
	
	public void goDownLB() {
		Node child = currentNode.getChildren().get(0);
		currentNode = child;
	}
	
	public void goDownMB() {
		Node child = currentNode.getChildren().get(1);
		currentNode = child;
	}
	
	public void goDownRB() {
		Node child = currentNode.getChildren().get(2);
		currentNode = child;
	}
	
	public void goUP() {
		Node parent = currentNode.getParent();
		currentNode = parent;
	}
	
}
