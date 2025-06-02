package com.cava.AuthService.utils;

public class UsersUtils {

    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("O número de telefone não pode ser nulo ou vazio");
        }
        String cleanedNumber = phoneNumber.replaceAll("\\D", "");

        if (cleanedNumber.startsWith("55") && cleanedNumber.length() > 11) {
            cleanedNumber = cleanedNumber.substring(2);
        }

        if (!cleanedNumber.matches("^\\d{2}9\\d{8}$")) {
            throw new IllegalArgumentException("Número de celular inválido. Deve conter DDD seguido de 9 dígitos. Exemplo: 11912345678");
        }

        return cleanedNumber;
    }
}
