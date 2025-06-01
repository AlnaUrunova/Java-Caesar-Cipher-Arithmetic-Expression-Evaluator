import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;


public class MainApplication {

    private static final CaesarCipherService theCipherThing = new CaesarCipherService();
    private static final ExpressionEvaluatorService theMathGuy = new ExpressionEvaluatorService();
    private static final Scanner userInput = new Scanner(System.in);

    // Starting the program. Making loop.
    public static void main(String[] args) {
        System.out.println("Welcome to Gehtsoft Technical Assessment");

        boolean stillRunning = true;
        while (stillRunning) {
            showTheMenu();
            int usersPick = getUsersPick();

            switch (usersPick) {
                case 1:
                    doCipherEncrypt();
                    break;
                case 2:
                    doCipherDecrypt();
                    break;
                case 3:
                    doTheMathStuff();
                    break;
                case 4:
                    stillRunning = false;
                    System.out.println("Exiting app. Goodbye!");
                    break;
                default:
                    System.out.println("Bad choice. Enter number 1-4.");
            }

            if (stillRunning) {
                System.out.print("\nContinue? (y/n): ");
                String continueInput = userInput.nextLine().trim().toLowerCase();
                if (!continueInput.equals("y")) {
                    stillRunning = false;
                    System.out.println("Exiting app. Goodbye!");
                }
            }
        }
        userInput.close();
    }

    // Showing the menu.
    private static void showTheMenu() {
        System.out.println("\n----------------------------------------");
        System.out.println("Please choose an option:");
        System.out.println("1. Caesar Cipher Encryption");
        System.out.println("2. Caesar Cipher Decryption");
        System.out.println("3. Arithmetic Expression Evaluation");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUsersPick() {
        try {
            int pickedNum = userInput.nextInt();
            userInput.nextLine(); 
            return pickedNum;
        } catch (InputMismatchException e) {
            System.out.println("Error: Not a number.");
            userInput.nextLine(); 
            return -1; 
        }
    }

    // Doing cipher encrypt
    private static void doCipherEncrypt() {
        System.out.println("\n--- Caesar Cipher Encryption ---");
        String theText = grabText("encryption");
        if (theText == null) return;

        int shiftNum = getShiftNum();
        if (shiftNum == Integer.MIN_VALUE) return;

        String secretText = theCipherThing.makeSecret(theText, shiftNum);
        System.out.println("Result: " + secretText);
    }

    // Doing cipher decrypt
    private static void doCipherDecrypt() {
        System.out.println("\n--- Caesar Cipher Decryption ---");
        String theText = grabText("decryption");
        if (theText == null) return;

        System.out.print("Shift number known? (y/n): ");
        String knewShift = userInput.nextLine().trim().toLowerCase();

        if (knewShift.equals("y")) {
            int shiftNum = getShiftNum();
            if (shiftNum == Integer.MIN_VALUE) return;
            String unsecretText = theCipherThing.makeUnsecret(theText, shiftNum);
            System.out.println("Result: " + unsecretText);
        } else {
            System.out.println("Brute force decrypting...");
            System.out.println("Note: All options shown for English and Russian alfabets.");
            System.out.println("Real text needs statistical analisys.");
            String bruteResults = theCipherThing.bruteForceDecrypt(theText);
            System.out.println(bruteResults);
        }
    }

    // Grabing text. From console or file.
    private static String grabText(String whatFor) {
        System.out.println("Select text source for " + whatFor + ":");
        System.out.println("1. Console");
        System.out.println("2. File");
        System.out.print("Your choice: ");
        int sourceChoice = getUsersPick();

        String gotText = null;
        if (sourceChoice == 1) {
            System.out.print("Enter text: ");
            gotText = userInput.nextLine();
        } else if (sourceChoice == 2) {
            System.out.print("Enter file path: ");
            String filePath = userInput.nextLine();
            try {
                gotText = theCipherThing.readAFile(filePath);
            } catch (IOException e) {
                System.err.println("File read error: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Wrong source choice.");
        }
        return gotText;
    }

    // Getting shift number.
    private static int getShiftNum() {
        System.out.print("Enter shift value (integer): ");
        try {
            int shift = userInput.nextInt();
            userInput.nextLine(); 
            return shift;
        } catch (InputMismatchException e) {
            System.out.println("Error: Enter whole number.");
            userInput.nextLine(); 
            return Integer.MIN_VALUE; 
        }
    }

    private static void doTheMathStuff() {
        System.out.println("\n--- Arithmetic Expression Evaluation ---");
        System.out.print("Enter an arithmetic expresion (e.g., 2 + 3 * (4 - 1)): ");
        String mathExp = userInput.nextLine();

        try {
            double result = theMathGuy.calcExpression(mathExp);
            System.out.println("Result: " + result);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown calc error: " + e.getMessage());
        }
    }

    // This class is for Caesar cipher
    private static class CaesarCipherService {

        private static final String ENGLISH_ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";
        private static final String ENGLISH_ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String RUSSIAN_ALPHABET_LOWER = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        private static final String RUSSIAN_ALPHABET_UPPER = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

        // Reading file text
        public String readAFile(String filePath) throws IOException {
            StringBuilder fileContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append(System.lineSeparator());
                }
            }
            return fileContent.toString();
        }

        // Making text secret
        public String makeSecret(String someText, int shiftNum) {
            StringBuilder resultText = new StringBuilder();
            for (char charInText : someText.toCharArray()) {
                resultText.append(moveOneChar(charInText, shiftNum));
            }
            return resultText.toString();
        }

        // Making text unsecret
        public String makeUnsecret(String someText, int shiftNum) {
            return makeSecret(someText, -shiftNum);
        }

        // Brute force decrypting. Trying all shifts.
        public String bruteForceDecrypt(String someText) {
            StringBuilder allResults = new StringBuilder();

            allResults.append("--- English Alphabet (26 shifts) ---\n");
            for (int i = 0; i < ENGLISH_ALPHABET_LOWER.length(); i++) {
                allResults.append(String.format("Shift %2d: %s\n", i, makeUnsecret(someText, i)));
            }

            allResults.append("\n--- Russian Alphabet (33 shifts) ---\n");
            for (int i = 0; i < RUSSIAN_ALPHABET_LOWER.length(); i++) {
                allResults.append(String.format("Shift %2d: %s\n", i, makeUnsecret(someText, i)));
            }
            return allResults.toString();
        }

        // Moving just one character.
        private char moveOneChar(char aChar, int shiftNum) {
            if (ENGLISH_ALPHABET_LOWER.indexOf(aChar) != -1) {
                return moveAlphaChar(aChar, shiftNum, ENGLISH_ALPHABET_LOWER);
            } else if (ENGLISH_ALPHABET_UPPER.indexOf(aChar) != -1) {
                return moveAlphaChar(aChar, shiftNum, ENGLISH_ALPHABET_UPPER);
            }
            else if (RUSSIAN_ALPHABET_LOWER.indexOf(aChar) != -1) {
                return moveAlphaChar(aChar, shiftNum, RUSSIAN_ALPHABET_LOWER);
            } else if (RUSSIAN_ALPHABET_UPPER.indexOf(aChar) != -1) {
                return moveAlphaChar(aChar, shiftNum, RUSSIAN_ALPHABET_UPPER);
            }
            return aChar;
        }

        private char moveAlphaChar(char aChar, int shiftNum, String theAlphabet) {
            int alphabetLen = theAlphabet.length();
            int charIdx = theAlphabet.indexOf(aChar);

            if (charIdx == -1) {
                return aChar;
            }

            int newIdx = (charIdx + shiftNum) % alphabetLen;

            if (newIdx < 0) {
                newIdx += alphabetLen;
            }

            return theAlphabet.charAt(newIdx);
        }
    }

    // This class for math expresions.
    private static class ExpressionEvaluatorService {

        // Calculating math expression. Using two stacks.
        public double calcExpression(String mathExp) {
            Stack<Double> numStack = new Stack<>();
            Stack<Character> opStack = new Stack<>();

            mathExp = mathExp.replaceAll("\\s+", "");

            for (int i = 0; i < mathExp.length(); i++) {
                char charNow = mathExp.charAt(i);

                if (charNow == '-' && (i == 0 || mathExp.charAt(i - 1) == '(' || isAnOp(mathExp.charAt(i - 1)))) {
                    numStack.push(0.0);
                    opStack.push('-');
                    continue;
                }

                if (Character.isDigit(charNow) || charNow == '.') {
                    StringBuilder numBuilder = new StringBuilder();
                    while (i < mathExp.length() && (Character.isDigit(mathExp.charAt(i)) || mathExp.charAt(i) == '.')) {
                        numBuilder.append(mathExp.charAt(i));
                        i++;
                    }
                    i--; 
                    try {
                        numStack.push(Double.parseDouble(numBuilder.toString()));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Bad number: " + numBuilder.toString());
                    }
                }
                // Opening bracket.
                else if (charNow == '(') {
                    opStack.push(charNow);
                }
                // Closing bracket.
                else if (charNow == ')') {
                    while (!opStack.empty() && opStack.peek() != '(') {
                        if (numStack.size() < 2) {
                            throw new IllegalArgumentException("Not enuff numbers for op " + opStack.peek() + " before ')'");
                        }
                        numStack.push(doTheOp(opStack.pop(), numStack.pop(), numStack.pop()));
                    }
                    if (opStack.empty()) {
                        throw new IllegalArgumentException("Bracket error: missing '('");
                    }
                    opStack.pop(); 
                }
                // Is an operator.
                else if (isAnOp(charNow)) {
                    while (!opStack.empty() && opHasPriority(charNow, opStack.peek())) {
                        if (numStack.size() < 2) {
                            throw new IllegalArgumentException("Not enough numbers for op " + opStack.peek());
                        }
                        numStack.push(doTheOp(opStack.pop(), numStack.pop(), numStack.pop()));
                    }
                    opStack.push(charNow);
                } else {
                    throw new IllegalArgumentException("Bad char in expression: '" + charNow + "'");
                }
            }

            // Finish up op
            while (!opStack.empty()) {
                if (opStack.peek() == '(') {
                    throw new IllegalArgumentException("Bracket error: missing ')'");
                }
                if (numStack.size() < 2) {
                    throw new IllegalArgumentException("Not enough numbers for op " + opStack.peek());
                }
                numStack.push(doTheOp(opStack.pop(), numStack.pop(), numStack.pop()));
            }

            // Final result
            if (numStack.size() != 1) {
                throw new IllegalArgumentException("Bad expression or syntax error.");
            }
            return numStack.pop();
        }

        // Checking if op.
        private boolean isAnOp(char someChar) {
            return someChar == '+' || someChar == '-' || someChar == '*' || someChar == '/';
        }

        // Get op priority.
        private int getOpPriority(char theOp) {
            if (theOp == '+' || theOp == '-') {
                return 1;
            }
            if (theOp == '*' || theOp == '/') {
                return 2;
            }
            return 0;
        }

        // Check op priority.
        private boolean opHasPriority(char currentOp, char opOnStack) {
            if (opOnStack == '(' || opOnStack == ')') {
                return false;
            }
            return getOpPriority(opOnStack) >= getOpPriority(currentOp);
        }

        // Doing the actual operation.
        private double doTheOp(char theOp, double numB, double numA) {
            switch (theOp) {
                case '+':
                    return numA + numB;
                case '-':
                    return numA - numB;
                case '*':
                    return numA * numB;
                case '/':
                    if (numB == 0) {
                        throw new IllegalArgumentException("Division by zero!");
                    }
                    return numA / numB;
                default:
                    throw new IllegalArgumentException("Unkown operator: " + theOp);
            }
        }
    }
}