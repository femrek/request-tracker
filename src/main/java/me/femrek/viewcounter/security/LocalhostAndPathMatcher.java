package me.femrek.viewcounter.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * Helper class to match requests from localhost with a specific path prefix.
 * Used for allowing access to debug endpoints only from localhost.
 */
public class LocalhostAndPathMatcher implements RequestMatcher {
    private static final List<String> LOCALHOSTS = List.of("127.0.0.1", "::1", "0:0:0:0:0:0:0:1");
    private final String pathPrefix;

    public LocalhostAndPathMatcher(String pathPrefix) {
        this.pathPrefix = pathPrefix.endsWith("/") ? pathPrefix : pathPrefix + "/";
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String path = request.getRequestURI();

        return isLocalhost(remoteAddr) && path.startsWith(pathPrefix);
    }

    private boolean isLocalhost(String ip) {
        return LOCALHOSTS.contains(ip);
    }
}
