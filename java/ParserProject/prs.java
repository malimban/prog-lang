/**
 *  Jacob Malimban
 *
 *  Recieves tokens from lex()
 */
import java.io.*;
import java.lang.Character;

public class prs {

	// definitions
	enum Token {
		INT_LIT, IDENT, ASSIGN_OP, ADD_OP, SUB_OP, MULT_OP, DIV_OP, LEFT_PAREN, RIGHT_PAREN, END_OF_LINE, END_OF_FILE
	}

	// variables
	static	CharClass charClass;
	static	Token nextToken;
	static	String fileName = "lexInput.txt";
// "front.in";
	static	String line = null;

	public static void main(String[] args) {

		try {
			// print to file?
			if(true) {
				PrintStream o = new PrintStream(new File("lexOutput.txt"));
				System.setOut(o);
			}
		} catch (FileNotFoundException e) {}


		for(int i = 0; i < 80; i++)
			System.out.print("*");				// formatting
		System.out.println("Jacob M. Student, CSCI4200, Fall 2019, Parser");
		for(int i = 0; i < 80; i++)
			System.out.print("*");
		System.out.println();

		// try creating readers
		try {
			FileReader fReader = new FileReader(fileName);
			BufferedReader bReader = new BufferedReader(fReader);

			// read input line by line, and build lexemes in each line
			while((line = bReader.readLine()) != null) {
				System.out.println("Input: " + line);
				getChar();

				for(char ch : line.toCharArray()) {
					lex();
					if(nextToken == Token.END_OF_LINE)
						break;
				}
			}

			// end of file reached
			bReader.close();

			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = '\u0000';
			lexLen = 4;
			nextToken = Token.END_OF_FILE;

		} catch (Exception e) {
			System.out.println("Error - file unopenable or readers not initialized.");
		}
	}

	/**
	 *  For unknown, non-alphanumerics, addChar() to lexeme[] and assign token
	 *
	 *
	 */
	private static Token lookup(char ch) {
		switch(ch) {
			case '=':
				addChar();
				nextToken = Token.ASSIGN_OP;
				break;
			case '+':
				addChar();
				nextToken = Token.ADD_OP;
				break;
			case '-':
				addChar();
				nextToken = Token.SUB_OP;
				break;
			case '*':
				addChar();
				nextToken = Token.MULT_OP;
				break;
			case '/':
				addChar();
				nextToken = Token.DIV_OP;
				break;
			case '(':
				addChar();
				nextToken = Token.LEFT_PAREN;
				break;
			case ')':
				addChar();
				nextToken = Token.RIGHT_PAREN;
				break;
			default:
				addChar();
				nextToken = Token.END_OF_LINE;
				break;
		}
		return nextToken;
	}

	/**
	 *  
	 *  
	 *
	 */
	private static void assign() {
	}

	/**
	 *  Parses strings in the language generated by the rule:
	 *  <expr> -> <term> { (+ | -) <term> }
	 *
	 */
	private static void expr() {
		System.out.println("Enter <expr>");

		// parse first term
		term();

		while(nextToken == ADD_OP || nextToken == SUB_OP) {
			lex();
			term();
		}
		System.out.println("Exit <expr>");
	}

	/**
	 *  Parses strings in language generated by the rule:
	 *  <term> -> <factor> { (* | /) <factor> }
	 *
	 */
	private static void term() {
		System.out.println("Enter <term>");

		factor();

		while(nextToken == MULT_OP || nextToken == DIV_OP) {
			lex();
			factor();
		}
		System.out.println("Exit <term>");
	}

	/**
	 *  Parses strings in the language generated by the rule:
	 *  <factor> -> id | int_const | ( <expr> )
	 *
	 */
	private static void factor() {
		System.out.println("Enter <factor>");

		// get next Token
		if(nextToken == IDENT || nextToken == INT_LIT)
			lex();
		else {
			if(nextToken == LEFT_PAREN) {
				lex();
				expr();
				if(nextToken == RIGHT_PAREN)
					lex();
				else
					error();
			}
		}
		System.out.println("Exit <factor>");
	}

/** /
	/**
	 *  Fully identifies lexeme[] and associated nextToken (with lookup if necessary)
	 *
	 *
	 */
	public static Token lex() {
		lexLen = 0;
		getNonBlank();
		switch(charClass) {
			case LETTER:
				do {
					addChar();
					getChar();
				} while(charClass == CharClass.LETTER || charClass == CharClass.DIGIT);

				nextToken = Token.IDENT;
				break;

			case DIGIT:
				do {
					addChar();
					getChar();
				} while(charClass == CharClass.DIGIT);

				nextToken = Token.INT_LIT;
				break;

			case UNKNOWN:
				lookup(nextChar);
				getChar();
				break;

			case END_OF_LINE:
				for(int i = 0; i<80; i++)
					System.out.print("*");
				System.out.println();

				return nextToken = Token.END_OF_LINE;
		}
		System.out.printf("%s%-20s%s%s%n", "Next Token is: ", nextToken, "next lexeme is ", String.valueOf(lexeme).substring(0, lexLen));
		return nextToken;
	}
/**/
}
