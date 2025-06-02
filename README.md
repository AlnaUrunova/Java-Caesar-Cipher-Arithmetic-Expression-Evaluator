# Java Console Program
Console application for the Gehtsoft Training Program entrance test. It has two main parts: a Caesar Cipher tool and a math expression calculator.
##What This Project Does
I made this program to do two main things:

1.  **Caesar Cipher:**
    * It encrypts and decrypts text.
    * You can provide text from the keyboard or from a file.
    * Works for English and Russian letters. Keeps letter case. Other symbols stay same.
    * You input a shift number, which can be positive or negative.
    * Letters will wrap around the alphabet.
    * Decrypting can use a shift number, or it can try all shifts.

2.  **Arithmetic Expression Evaluator:**
    * The arithmetic expression evaluator calculates math problems.
    * It supports addition, subtraction, multiplication, and division.
    * Parentheses are used to control the order of operations.
    * It handles decimal numbers.
    * The program attempts to manage division by zero.
    * It also works with negative numbers.

## How to Compile and Run

 The MainApplication.java file serves as the main entry point for the program.

1. **Compile the code:**
  * Use the Java compiler to build the program:
```
javac MainApplication.java CaesarCipherService.java ExpressionEvaluatorService.java
```

2.  **Run the program:** 
    * After successful compilation, execute the main program file: 
        ```
        java MainApplication
        ```
The program will then start and display a menu in your console.

## My Approach and Assumptions

My code is organized into three separate files. This helps keep the project structured.
* MainApplication.java: This is the main file. It displays the user menu. It then calls functions from the other files based on user selection.
* CaesarCipherService.java: This file contains all the code for the Caesar cipher. This includes encryption, decryption, and reading from files.
* ExpressionEvaluatorService.java: This file has the code for evaluating arithmetic expressions. It calculates the results of math problems.

I aimed to make the program handle various inputs and errors.

## Examples of Usage

Here are some examples of how to use the program:

**1. Caesar Cipher Encryption:**
Welcome to Gehtsoft Technical Assessment
Please choose an option:
1. Caesar Cipher Encryption
2. Caesar Cipher Decryption
3. Arithmetic Expression Evaluation
4. Exit
Enter your choice: 1
--- Caesar Cipher Encryption ---
Select text source for encryption:
1. Console
2. File
Your choice: 1
Enter text: Hello World
Enter shift value (integer): 3
Result: Khoor Zruog

Continue? (y/n): y

**2. Arithmetic Expression Evaluation:**
Welcome to Gehtsoft Technical Assessment
Please choose an option:
1. Caesar Cipher Encryption
2. Caesar Cipher Decryption
3. Arithmetic Expression Evaluation
4. Exit
Enter your choice: 3
--- Arithmetic Expression Evaluation ---
Enter an arithmetic expression (e.g., 2 + 3 * (4 - 1)): 2 * (3 + 4) - 1
Result: 13.0

Continue? (y/n): n
Exiting app. Goodbye!
