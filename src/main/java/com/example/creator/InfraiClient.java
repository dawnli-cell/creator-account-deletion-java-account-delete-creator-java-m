package com.example.creator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class InfraiClient {
    public record Session(String id) {}
    private final ApplicationConfig config;
    private final HttpClient http;
    public InfraiClient(ApplicationConfig config, HttpClient http, List<String> ignoredSessionIds) { this.config = config; this.http = http; }
    public List<Session> listSessions(String userId) {
        String response = request("GET", "/v1/auth/session/list_for_user/" + userId, "");
        return Pattern.compile("\\\"(?:session_id|id)\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"")
                .matcher(response).results().map(match -> new Session(match.group(1))).toList();
    }
    public void revokeSession(String sessionId) { // auth.session.revoke
        request("POST", "/v1/auth/session/revoke/" + sessionId, "{\"session_id\":\"" + sessionId + "\"}");
    }
    public void revokeAccountKey(String keyId) { request("DELETE", "/v1/account/keys/revoke/" + keyId, ""); }
    private String request(String method, String path, String body) {
        String endpoint = config.baseUrl().endsWith("/v1") ? config.baseUrl().substring(0, config.baseUrl().length() - 3) + path : config.baseUrl() + path;
        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint)).timeout(Duration.ofSeconds(10)).header("Authorization", "Bearer " + config.apiKey()).header("Content-Type", "application/json").method(method, body.isEmpty() ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body)).build();
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            String envelope = response.body();
            if (!Pattern.compile("\\\"ok\\\"\\s*:\\s*true").matcher(envelope).find()) throw new InfraiException(envelope);
            if (response.statusCode() == 429) throw new InfraiException("retryable response");
            if (response.statusCode() >= 400) throw new InfraiException(envelope);
            return envelope;
        } catch (java.io.IOException | InterruptedException e) { throw new InfraiException(e.getMessage()); }
    }
}
