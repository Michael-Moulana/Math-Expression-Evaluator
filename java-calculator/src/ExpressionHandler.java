import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

class ExpressionHandler {
	// HashMap to store variable assignments
	public static HashMap<Character, String> variables = new HashMap<Character, String>();
	
	// Check if the given expression is a valid assignment
	public boolean isAssignmentExpression(String expression) {
        if (expression.length() > 2) {
            char secondChar = expression.charAt(1);
            if (Character.isLetter(expression.charAt(0)) && secondChar == '=')
                return true;
        }
        return false;
    }
	
	// Perform the given operation on the accepted expression
	public void performOperation(String acceptedExpression) throws ScriptException {
        if (variables.containsKey(acceptedExpression.charAt(0)))
        	variables.remove(acceptedExpression.charAt(0));
        
        // example: selects A from A=1 or B from B=3*4
        char variableLetter = acceptedExpression.charAt(0);
        // example: selects 1 from A=1 or 3*4 from B=3*4    
        String mathExpression = acceptedExpression.substring(2);
        variables.put(variableLetter, Integer.toString(calculate(mathExpression)));
    }
	
	// Calculate the result of a mathematical expression
	public int calculate(String expressionToOperate) throws ScriptException {
        String res = " ";
         
        // Traverse the characters of the exprToOperate
        for (int i = 0; i < expressionToOperate.length(); i++)
        {
            // if there is a letter, get value of that key
            // otherwise, add it to result 
            if (Character.isLetter(expressionToOperate.charAt(i))) {
            	// if "a" is key, 2+a. 
                if (variables.containsKey(expressionToOperate.charAt(i))) 
                    res = res + variables.get(expressionToOperate.charAt(i));
            } else {
            	res = res + Character.toString(expressionToOperate.charAt(i));
            }	 
        }
        int calculatedValue =  (int) evaluate(res);
        return calculatedValue;
    }
	
	// Show the value of the accepted expression
	public void showVariableValue(char acceptedVariable) {
        System.out.println(variables.get(acceptedVariable));
    }
	
	// Show all expressions
	public void showAllVariables() {
		for (char key : variables.keySet()) {
            System.out.println(key + " = " + variables.get(key));
        }
    }
	
	// Evaluate a mathematical expression using recursive descent parsing
	public static double evaluate(final String str) {
	    return new Object() {
	        int pos = -1, ch;
	        
	        // Move to the next character in the input string
	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }
	        
	        // Check if the current character matches the expected character
	        boolean Accept(int charToAccept) {
	        	// Skip whitespaces
	            while (ch == ' ') nextChar();
	            if (ch == charToAccept) {
	            	// Move to the next character
	                nextChar();
	                return true;
	            }
	            return false;
	        }
	        
	        // Entry point for parsing the entire expression
	        double parse() {
	        	// Start by moving to the first character
	            nextChar();
	            
	            // Parse the highest-precedence part of the expression
	            double x = parseExpression();
	            
	            // Check for unexpected characters
	            if (pos < str.length()) 
	            	throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }
	        
	        // Parse an expression (addition and subtraction)
	        double parseExpression() {
	        	// Parse the lowest-precedence part of the expression
	            double x = parseTerm();
	            // Loop to handle multiple additions and subtractions
	            for (;;) {
	            	// If '+' is encountered, parse and add another term
	                if (Accept('+')) 
	                	x = x + parseTerm();
	                // If '-' is encountered, parse and subtract another term
	                else if (Accept('-')) 
	                	x = x - parseTerm();
	                // Return the result if no more addition/subtraction
	                else 
	                	return x;
	            }
	        }
	        
	        // Parse a term (multiplication and division)
	        double parseTerm() {
	        	// Parse the lowest-precedence part of the expression
	            double x = parseFactor();
	            // Loop to handle multiple multiplications and divisions
	            for (;;) {
	            	// If '*' is encountered, parse and multiply by another factor
	                if (Accept('*')) 
	                	x = x * parseFactor();
	                // If '/' is encountered, parse and divide by another factor
	                else if (Accept('/')) 
	                	x = x / parseFactor();
	                // Return the result if no more multiplication/division
	                else return x;
	            }
	        }

	        // Parse a factor (operands, parentheses, and unary plus/minus)
	        double parseFactor() {
	        	// Unary plus
	            if (Accept('+')) 
	            	return parseFactor(); 
	            // Unary minus
	            if (Accept('-')) 
	            	return -parseFactor(); 
	            
	            double x;
	            
	            int startPosition = this.pos;

	            if (Accept('(')) { 
	            	// Parse the expression within parentheses
	                x = parseExpression();
	               
	                // Ensure closing parenthesis
	                Accept(')');
	            //checking numbers
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { 
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	            	// Parse numeric literals
	                x = Double.parseDouble(str.substring(startPosition, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { 
	            	// Parse functions (not fully implemented in this code)
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String function = str.substring(startPosition, this.pos);
	                x = parseFactor();
	            } else throw new RuntimeException("Unexpected: " + (char)ch);
	            // Handle exponentiation (^)
	            if (Accept('^')) x = Math.pow(x, parseFactor()); 

	            return x;
	        }
	    // Call the entry point function to start parsing
	    }.parse();
	}
}