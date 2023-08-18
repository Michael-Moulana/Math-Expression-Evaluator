import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MathExpressionEvaluator  {
    public static void main(String[] args) throws ScriptException {
        Scanner scanner = new Scanner(System.in);
        
        // Welcome message and instructions
        System.out.println("     Welcome to the \"Math Expression Calculator\"\n"
                + "- You can use the following commands:");
        System.out.println("1) Add variable with value (e.g., \"a=2\", \"b=2^3\", \"c=a/5\", ...)\n"
                + "2) Show a variable's Value or all variables (e.g., \"show=x\", \"show(x)\", \"show:x\", \"show\")\n"
                + "3) Exit    (Type 0)\n");

        // Initialize the expression handler
        ExpressionHandler exprHandler = new ExpressionHandler();
        String userCommand;

        do {
        	// Prompt for user userCommand
            System.out.print("Your Command: ");
            userCommand = scanner.nextLine().trim();

            if (!userCommand.equals("0")) {
                if (userCommand.startsWith("show")) {
                    if (userCommand.contains("=") || userCommand.contains("(") || userCommand.contains(":")) {
                    	// Display the value of a specific variable
                        try {
                            char variableName = userCommand.charAt(5);
                            exprHandler.showVariableValue(variableName);
                        } catch (Exception e) {
                            System.out.println("Invalid command format. Use: show=x, show(x), show:x");
                        }
                    } else {
                    	// Display values of all variables
                    	exprHandler.showAllVariables();
                    }
                } else if (exprHandler.isAssignmentExpression(userCommand)) {
                	 // Handle variable assignment
                	exprHandler.performOperation(userCommand);
                } else {
                	// Invalid userCommand format
                    System.out.println("Invalid command. Please use the specified formats.");
                }
            }
        } while (!userCommand.equals("0"));

        // Exit message
        System.out.println("Exited!");
    }
}