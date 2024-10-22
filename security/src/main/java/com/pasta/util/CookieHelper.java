package com.pasta.util;

import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CookieHelper {

    public static void addRefreshTokenCookie(String refreshToken, HttpServletResponse response, long validity) {
        ZonedDateTime expires = ZonedDateTime.now(ZoneId.of("UTC")).plus(validity, ChronoUnit.SECONDS);
        String expiresString = expires.format(DateTimeFormatter.ISO_INSTANT);
        response.addHeader("Set-Cookie",
                String.format("refreshToken=%s; path=/; Max-Age=%s; Expires=%s; SameSite=None;",
                        refreshToken, validity, expiresString));
    }

}
