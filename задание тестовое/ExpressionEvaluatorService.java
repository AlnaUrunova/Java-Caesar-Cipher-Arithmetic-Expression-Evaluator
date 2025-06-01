import java.util.Stack;

// Class for math expression evaluation.
public class ExpressionEvaluatorService {

    // Evaluate expression.
    public double evaluateExpression(String expression) {
        Stack<Double> numStack = new Stack<>(); // Stack for numbers.
        Stack<Character> opStack = new Stack<>(); // Stack for operators.

        expression = expression.replaceAll("\\s+", ""); // Remove spaces.

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            // Handle unary minus.
            if (currentChar == '-' && (i == 0 || expression.charAt(i - 1) == '(' || isOperator(expression.charAt(i - 1)))) {
                numStack.push(0.0); // Push zero for unary minus.
                opStack.push('-');
                continue; // Next char.
            }

            // Is digit or dot.
            if (Character.isDigit(currentChar) || currentChar == '.') {
                StringBuilder numBuilder = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numBuilder.append(expression.charAt(i));
                    i++;
                }
                i--; // Adjust index.
                try {
                    numStack.push(Double.parseDouble(numBuilder.toString()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Bad number: " + numBuilder.toString());
                }
            }
            else if (currentChar == '(') {
                opStack.push(currentChar);
            }
            else if (currentChar == ')') {
                while (!opStack.empty() && opStack.peek() != '(') {
                    if (numStack.size() < 2) {
                        throw new IllegalArgumentException("Not enough numbers for operation " + opStack.peek() + " before ')'");
                    }
                    numStack.push(applyOperation(opStack.pop(), numStack.pop(), numStack.pop()));
                }
                if (opStack.empty()) {
                    throw new IllegalArgumentException("Mismatched brackets: missing '('");
                }
                opStack.pop(); 
            }
            else if (isOperator(currentChar)) {
                while (!opStack.empty() && checkPrecedence(currentChar, opStack.peek())) {
                    if (numStack.size() < 2) {
                        throw new IllegalArgumentException("Not enough numbers for operation " + opStack.peek());
                    }
                    numStack.push(applyOperation(opStack.pop(), numStack.pop(), numStack.pop()));
                }
                opStack.push(currentChar); 
            } else {
                throw new IllegalArgumentException("Invalid character in expression: '" + currentChar + "'");
            }
        }

        // Apply remaining operations
        while (!opStack.empty()) {
            if (opStack.peek() == '(') {
                throw new IllegalArgumentException("Mismatched brackets: missing ')'");
            }
            if (numStack.size() < 2) {
                throw new IllegalArgumentException("Not enough numbers for operation " + opStack.peek());
            }
            numStack.push(applyOperation(opStack.pop(), numStack.pop(), numStack.pop()));
        }

        // Final result check
        if (numStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression or syntax error.");
        }
        return numStack.pop();
    }

    // Checking for operator
    private boolean isOperator(char character) {
        return character == '+' || character == '-' || character == '*' || character == '/';
    }

    // Get operator priority
    private int getOperatorPriority(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        }
        if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0; 
    }

    // Checking operator precedence
    private boolean checkPrecedence(char currentOp, char stackTopOp) {
        if (stackTopOp == '(' || stackTopOp == ')') {
            return false; 
        }
        return getOperatorPriority(stackTopOp) >= getOperatorPriority(currentOp);
    }

    // Applying arithmetic operation
    private double applyOperation(char operationChar, double secondNum, double firstNum) {
        switch (operationChar) {
            case '+':
                return firstNum + secondNum;
            case '-':
                return firstNum - secondNum;
            case '*':
                return firstNum * secondNum;
            case '/':
                if (secondNum == 0) {
                    throw new IllegalArgumentException("Division by zero!");
                }
                return firstNum / secondNum;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operationChar);
        }
    }
}