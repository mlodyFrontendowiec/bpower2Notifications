package com.example.bpower2notifications;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class UserKey {
    private final String login;
    private final String password;

    public UserKey(String login, String password) {
        this.login = login;
        this.password = password;
    }

    private String hashPassword() {
        return new DigestUtils("SHA-256").digestAsHex(password);
    }

    private String createUserKey(String passwordHash) {
        String stringToHash = login + ":" + passwordHash;
        return new String(Base64.encodeBase64(stringToHash.getBytes()));
    }

    public String getUserKey() {
        String hashedPassword = hashPassword();
        return createUserKey(hashedPassword);
    }
}