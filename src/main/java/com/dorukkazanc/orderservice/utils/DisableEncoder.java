/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class DisableEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}
