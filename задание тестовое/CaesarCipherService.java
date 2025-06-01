import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CaesarCipherService {

    // Alphabets for use
    private static final String ENGLISH_ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String ENGLISH_ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String RUSSIAN_ALPHABET_LOWER = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final String RUSSIAN_ALPHABET_UPPER = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    // Reading text from file
    public String readFromFile(String filePath) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String lineOfText;
            while ((lineOfText = fileReader.readLine()) != null) {
                fileContent.append(lineOfText).append(System.lineSeparator());
            }
        }
        return fileContent.toString();
    }

    // Encrypting text with cipher
    public String encrypt(String inputText, int shiftValue) {
        StringBuilder encryptedText = new StringBuilder();
        for (char currentCharacter : inputText.toCharArray()) {
            encryptedText.append(shiftCharacter(currentCharacter, shiftValue));
        }
        return encryptedText.toString();
    }

    // Decrypting text with cipher
    public String decrypt(String encryptedText, int shiftValue) {
        return encrypt(encryptedText, -shiftValue);
    }
    //Trying all shifts
    public String decryptBruteForce(String encryptedText) {
        StringBuilder allResults = new StringBuilder();
        // English
        allResults.append(String.format("--- English Alphabet (26 shifts) ---\n")); 
        for (int i = 0; i < ENGLISH_ALPHABET_LOWER.length(); i++) {
            allResults.append(String.format("Shift %2d: %s\n", i, decrypt(encryptedText, i))); 
        }
        // Russian
        allResults.append("\n--- Russian Alphabet (33 shifts) ---\n");
        for (int i = 0; i < RUSSIAN_ALPHABET_LOWER.length(); i++) {
            allResults.append(String.format("Shift %2d: %s\n", i, decrypt(encryptedText, i))); 
        }
        return allResults.toString();
    }

    // Shifting one character.
    private char shiftCharacter(char charToShift, int shiftAmount) {
        // Checking English chars.
        if (ENGLISH_ALPHABET_LOWER.indexOf(charToShift) != -1) { 
            return shiftAlphabetChar(charToShift, shiftAmount, ENGLISH_ALPHABET_LOWER); 
        } else if (ENGLISH_ALPHABET_UPPER.indexOf(charToShift) != -1) {
            return shiftAlphabetChar(charToShift, shiftAmount, ENGLISH_ALPHABET_UPPER); 
        }
        // Checking Russian chars.
        else if (RUSSIAN_ALPHABET_LOWER.indexOf(charToShift) != -1) {
            return shiftAlphabetChar(charToShift, shiftAmount, RUSSIAN_ALPHABET_LOWER);
        } else if (RUSSIAN_ALPHABET_UPPER.indexOf(charToShift) != -1) {
            return shiftAlphabetChar(charToShift, shiftAmount, RUSSIAN_ALPHABET_UPPER);
        }
        // Not a letter return as is
        return charToShift;
    }
    private char shiftAlphabetChar(char charToShift, int shiftAmount, String currentAlphabet) {
        int alphabetLen = currentAlphabet.length();
        int charPos = currentAlphabet.indexOf(charToShift);
        if (charPos == -1) {
            return charToShift;
        }
        int newPos = (charPos + shiftAmount) % alphabetLen;

        if (newPos < 0) {
            newPos += alphabetLen;
        }

        return currentAlphabet.charAt(newPos);
    }
}