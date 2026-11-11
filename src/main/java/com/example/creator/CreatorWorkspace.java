package com.example.creator;

public final class CreatorWorkspace {
    private final String userId, keyId, idempotencyKey;
    private boolean assetsAvailable = true, processing = true, subscribersUpdated;
    public CreatorWorkspace(String userId, String keyId, String idempotencyKey) { this.userId = userId; this.keyId = keyId; this.idempotencyKey = idempotencyKey; }
    public void prepareLocalDeletion() { assetsAvailable = false; processing = false; subscribersUpdated = true; }
    public String userId() { return userId; }
    public String keyId() { return keyId; }
    public String idempotencyKey() { return idempotencyKey; }
    public boolean assetsAvailable() { return assetsAvailable; }
    public boolean processing() { return processing; }
    public boolean subscribersUpdated() { return subscribersUpdated; }
}
