package com.psedb.timeclock.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class PinService {

    private final BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(12);
    private final byte[] pepper;

    /**
     * Initializes pepper from environment; fails if missing
     */
    public PinService() {
        String p = System.getenv("PIN_PEPPER");
        if (p == null || p.isBlank()) {
            throw new IllegalStateException("PIN_PEPPER environment variable not set.");
        }
        this.pepper = p.getBytes(StandardCharsets.UTF_8);
    }

    public String bcryptHash(String pin) {
        return bCrypt.encode(pin);
    }

    /**
     * Generates lookup key from PIN using HMAC
     */
    public byte[] lookupKey(String pin){
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(pepper, "HmacSHA256"));
            return mac.doFinal(pin.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to generate PIN lookup key.", e);
        }
    }
}
