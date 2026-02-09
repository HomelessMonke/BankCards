package homeless.monkey.com.bankcards.util;

import java.security.SecureRandom;

public class CardUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    private CardUtils() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    public static String generateCardNumber(String bankBin){

        var sb = new StringBuilder(bankBin);
        for (int i = 0; i < 9; i++) {
            sb.append(RANDOM.nextInt(10));
        }

        String calculationNumber = sb.toString();
        int checkDigit = calculateLuhnCheckDigit(calculationNumber);
        return calculationNumber + checkDigit;
    }

    public static int calculateLuhnCheckDigit(String number){

        int sum = 0;
        boolean doubleDigit = true; // удваиваем каждую вторую цифру справа

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (10 - (sum % 10)) % 10;
    }

    public static boolean isValidCardNumberByLuhn(String cardNumber){

        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }

        int sum = 0;
        boolean doubleDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return sum % 10 == 0;
    }

    public static String getMaskedCardNumber(String number){
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}
