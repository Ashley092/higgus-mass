package io.higgus.lab.security.core.util;

//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityFrameworkUtils {

    public static final String AUTHORIZATION_BEARER = "Bearer";

    public static final String LOGIN_USER_HEAR = "login-user";

    private SecurityFrameworkUtils() {};

//
//    public static Authentication getAuthentication() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        if (context == null) {
//            return null;
//        }
//        return context.getAuthentication();
//    }

//    public static LoginUser setLoginUser(String loginUser, HttpServletRequest request) {
//        Authentication authentication = getAuthentication();
//        if (authentication == null) {
//            return null;
//        }
//        return authentication.getPrincipal() instanceof LoginUser ? (LoginUser) authentication
//    }

    public static Long getLoginUserId() {
        return 1L;
    }
}
