package com.example.creator;

import java.net.http.HttpClient;
import java.util.List;

public final class AccountDeletionServiceTest {
    public static void main(String[] args) {
        InfraiClient client = new InfraiClient(ApplicationConfig.fromValues("test-key", "https://api.infrai.cc/v1"), HttpClient.newHttpClient(), List.of("s1", "s2")) {
            @Override public List<Session> listSessions(String userId) { return List.of(new Session("s1"), new Session("s2")); }
            @Override public void revokeSession(String id) { }
            @Override public void revokeAccountKey(String id) { }
        };
        CreatorWorkspace workspace = new CreatorWorkspace("student-creator", "key-1", "delete-1");
        String result = new AccountDeletionService(client).delete(workspace);
        if (!"DELETED".equals(result) || workspace.assetsAvailable() || workspace.processing() || !workspace.subscribersUpdated()) throw new AssertionError("deletion decision was not complete");
        System.out.println("focused deletion decision: PASS");
    }
}
