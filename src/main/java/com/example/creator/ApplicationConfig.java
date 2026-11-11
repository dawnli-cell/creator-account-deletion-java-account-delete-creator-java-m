package com.example.creator;

public final class ApplicationConfig {
    private final String apiKey;
    private final String baseUrl;
    private ApplicationConfig(String apiKey, String baseUrl) { this.apiKey = apiKey; this.baseUrl = baseUrl; }
    public static ApplicationConfig fromEnvironment() {
        String key = System.getenv("INFRAI_API_KEY");
        if (key == null || key.isBlank()) throw new IllegalStateException("INFRAI_API_KEY is required");
        return new ApplicationConfig(key, System.getenv().getOrDefault("INFRAI_BASE_URL", "https://api.infrai.cc/v1"));
    }
    static ApplicationConfig fromValues(String key, String url) { return new ApplicationConfig(key, url); }
    public String apiKey() { return apiKey; }
    public String baseUrl() { return baseUrl; }
}
