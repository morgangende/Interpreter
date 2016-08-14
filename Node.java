import java.util.ArrayList;

public class Node {

	private int altNo;
	private Node parent;
	private ArrayList<Node> children = new ArrayList<Node>(3);
	private String val;

	// GETTERS
	
	public int getAltNo() {
		return this.altNo;
	}

	public Node getParent() {
		return this.parent;
	}

	public ArrayList<Node> getChildren() {
		return this.children;
	}

	public String getVal() {
		return this.val;
	}

	// SETTERS
	
	public void setAltNo(int no) {
		this.altNo = no;
	}

	public void setParent(Node n) {
		this.parent = n;
	}

	public void addChild(Node n) {
		this.children.add(n);
	}

	public void setVal(String val) {
		this.val = val;
	}
	
}
