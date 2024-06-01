package com.yangjae.lupine.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yangjae.lupine.model.dto.ErrorResponse;
import com.yangjae.lupine.model.enums.UserError;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtil {

    public static int generateRandomNumber(int digit) {
        if (digit <= 0) {
            throw new IllegalArgumentException("Digit must be a positive integer.");
        }

        // 범위 계산을 위해 10의 거듭제곱을 구함
        int minBound = (int) Math.pow(10, digit - 1);
        int maxBound = (int) Math.pow(10, digit);

        // ThreadLocalRandom을 사용하여 랜덤 객체 생성
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 범위 내의 랜덤 숫자 생성
        return random.nextInt(minBound, maxBound);
    }

    // UserError 를 json 형태로 response
    public static void printErrorMessage(HttpServletResponse response, @NonNull UserError userError) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        int statusCode;

        if (userError.getHttpStatus() != null) {
            statusCode = userError.getHttpStatus().value();
        } else {
            statusCode = HttpStatus.UNAUTHORIZED.value();
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.of(userError);

        String errorJson = objectMapper.writeValueAsString(errorResponse);
        PrintWriter writer = response.getWriter();
        writer.println(errorJson);
    }

    // 10 ~ 12자리 랜덤 비밀번호 생성
    // 영어소문자, 대문자, 숫자, 특수문자(1~2개)
    public static String generatePassword() {
        final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String NUMBERS = "0123456789";
        final String SPECIALCHARS = "!@#$%&";
        final SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();
        List<String> charsPool = new ArrayList<>();

        // 각 카테고리에서 임의의 문자 추가
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        password.append(SPECIALCHARS.charAt(random.nextInt(SPECIALCHARS.length())));

        // 추가적인 특수 문자를 0~1개 무작위로 추가
        for (int i = 0; i < random.nextInt(2); i++) {
            password.append(SPECIALCHARS.charAt(random.nextInt(SPECIALCHARS.length())));
        }

        // 모든 문자 풀을 결합하여 나머지 비밀번호 길이를 채움
        charsPool.add(LOWERCASE);
        charsPool.add(UPPERCASE);
        charsPool.add(NUMBERS);
        charsPool.add(SPECIALCHARS);

        // 10~12자리 중 남은 길이를 계산하여 랜덤 문자로 채움
        int remainingLength = 10 + random.nextInt(3) - password.length();
        for (int i = 0; i < remainingLength; i++) {
            String selectedPool = charsPool.get(random.nextInt(charsPool.size()));
            password.append(selectedPool.charAt(random.nextInt(selectedPool.length())));
        }

        // 비밀번호 문자 위치를 무작위로 섞음
        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars);

        // 최종 비밀번호 생성
        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }

    // Pagination Offset
    // Limit 값에 따른 Offset
    public static int getPagenationOffset(int page, int limit) {
        return (page - 1) * limit;
    }

    public static String maskingName(String name) {
        if (name == null || name.length() <= 2) {
            return name;
        }

        String middle = name.substring(1, name.length() - 1);
        middle = middle.replaceAll(".", "*");
        return name.charAt(0) + middle + name.charAt(name.length() - 1);
    }

    public static void maskingName(List<Map<String, Object>> list, String key) {
        for (Map<String, Object> map : list) {
            map.put(key, maskingName((String) map.get(key)));
        }
    }

    public static String maskingPhone(String phone) {
        if (phone == null || phone.length() <= 7) {
            return phone;
        }

        String start = phone.substring(0, 3);
        String end = phone.substring(phone.length() - 4);
        String middle = phone.substring(3, phone.length() - 4).replaceAll(".", "*");

        return start + middle + end;
    }

    public static void maskingPhone(List<Map<String, Object>> list, String key) {
        for (Map<String, Object> map : list) {
            map.put(key, maskingPhone((String) map.get(key)));
        }
    }

    public static String maskingAddress(String address) {
        if (address == null) {
            return address;
        }

        // 공백을 기준으로 문자열을 나눈다.
        String[] parts = address.split(" ");

        // 3번째 공백 이후부터 마스킹 처리하기 위해, 처음 3개 요소는 그대로 두고 나머지는 '*'로 변경
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i < 3) { // 처음 3개 요소는 그대로 추가
                masked.append(parts[i]);
                if (i < parts.length - 1) { // 마지막 요소가 아니라면 공백 추가
                    masked.append(" ");
                }
            } else { // 3번째 이후 요소는 마스킹 처리
                String maskedPart = parts[i].replaceAll(".", "*");
                masked.append(maskedPart);
                if (i < parts.length - 1) {
                    masked.append(" ");
                }
            }
        }

        return masked.toString();
    }

    public static void maskingAddress(List<Map<String, Object>> list, String key) {
        for (Map<String, Object> map : list) {
            map.put(key, maskingAddress((String) map.get(key)));
        }
    }

    public static String maskingEmail(String email) {
        if (email == null) {
            return email;
        }

        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            // 이메일이 유효하지 않은 경우, 원본을 그대로 반환
            return email;
        }

        String localPart = email.substring(0, atIndex); // '@' 기호 이전의 부분
        String domainPart = email.substring(atIndex); // '@' 기호 포함 이후의 부분

        if (localPart.length() > 2) {
            // 맨 앞 2자리를 제외한 나머지를 '*'로 마스킹 처리
            char[] maskedChars = new char[localPart.length() - 2];
            java.util.Arrays.fill(maskedChars, '*');
            String maskedPart = new String(maskedChars);

            // 마스킹 처리된 문자열과 도메인 부분을 합침
            return localPart.substring(0, 2) + maskedPart + domainPart;
        } else {
            // 로컬 파트의 길이가 2 이하인 경우, 전체 로컬 파트를 그대로 반환
            return email;
        }
    }

    public static void maskingEmail(List<Map<String, Object>> list, String key) {
        for (Map<String, Object> map : list) {
            map.put(key, maskingEmail((String) map.get(key)));
        }
    }
}
