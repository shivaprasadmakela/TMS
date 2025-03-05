package com.tms.tms_backend.util;

import com.tms.tms_backend.repository.ClientRepository;
import reactor.core.publisher.Mono;

import java.util.Random;

public class ClientUtil {

    private static final int MAX_RETRIES = 10;
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";

    public static Mono<String> generateUniqueCode(String email, ClientRepository clientRepository) {
        return generateUniqueCodeWithRetries(email, clientRepository, 0);
    }

    private static Mono<String> generateUniqueCodeWithRetries(String email, ClientRepository clientRepository, int retryCount) {
        if (retryCount >= MAX_RETRIES) {
            return Mono.error(new RuntimeException("Failed to generate a unique client code after " + MAX_RETRIES + " attempts"));
        }

        return generateCode(email)
                .flatMap(code -> checkIfCodeExists(code, clientRepository)
                        .flatMap(exists -> exists
                                ? generateUniqueCodeWithRetries(email, clientRepository, retryCount + 1)
                                : Mono.just(code)
                        ));
    }

    private static Mono<String> generateCode(String email) {
        // Extract full email before '@'
        String base = email.split("@")[0].toUpperCase();
        base = base.replaceAll("[^A-Z0-9]", "");

        if (base.isEmpty()) {
            base = getRandomString(5);
        }

        // Ensure at least 5 characters
        String code = base.length() >= 5 ? base.substring(0, 5) : base + getRandomString(5 - base.length());

        return Mono.just(code);
    }

    private static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    private static Mono<Boolean> checkIfCodeExists(String code, ClientRepository clientRepository) {
        return clientRepository.existsByCode(code);
    }
}
