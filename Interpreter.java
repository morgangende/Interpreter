import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Interpreter {

	public static void main(String[] args) {
		Scanner inProg, inData;
		try {
			inProg = new Scanner(Paths.get(args[0]));
		} catch (IOException e) {
			System.err.println("Error opening file: " + args[0]);
			return;
		}

		try {
			inData = new Scanner(Paths.get(args[1]));
		} catch (IOException e) {
			System.err.println("Error opening file: " + args[1]);
			inProg.close();
			return;
		}

		Tokenizer.create(inProg);

		Parser parser = new Parser();
		parser.parseProg();
		
		Printer printer = new Printer();
		printer.printProg();
		
		Executer executer = new Executer(inData);
		executer.execProg();

		inProg.close();
		inData.close();
	}
	
}
