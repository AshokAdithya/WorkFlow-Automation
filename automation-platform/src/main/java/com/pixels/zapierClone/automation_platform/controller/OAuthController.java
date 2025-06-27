package com.pixels.zapierClone.automation_platform.controller;

import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandler;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandlerRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    @Value("${slack.redirect-uri}")
    private String slackUri;

    @Value("${gmail.redirect-uri}")
    private String gmailUri;

    @Value("${googleforms.redirect-uri}")
    private String googleFormsUri;

    private final IntegrationHandlerRegistry handlerRegistry;

    public OAuthController(IntegrationHandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    @GetMapping("/{app}/authorize")
    public ResponseEntity<String> getAuthorizationUrl(
            @PathVariable String app,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized: Missing userId.");
        }

        IntegrationHandler handler = handlerRegistry.getHandler(app);
        String url = handler.getAuthorizationUrl(userId);
        System.out.println(url);
          // includes state=userId
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{app}/callback")
    public void handleCallback(
            @PathVariable String app,
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {

        Long userId = Long.parseLong(state);
        String redirectUri = getRedirectUriForApp(app);

        IntegrationHandler handler = handlerRegistry.getHandler(app);
        handler.handleCallback(userId, code, redirectUri);

        String html = """
            <html>
            <body>
            <script>
                window.opener.postMessage({ type: "OAUTH_SUCCESS", service: "%s" }, "*");
                window.close();
            </script>
            </body>
            </html>
            """.formatted(app);

        response.setContentType("text/html");
        response.getWriter().write(html);
    }

    private String getRedirectUriForApp(String app) {
        return switch (app) {
            case "gmail" -> gmailUri;
            case "slack" -> slackUri;
            case "google_forms"->googleFormsUri;
            default -> throw new IllegalArgumentException("Unsupported app: " + app);
        };
    }
}
