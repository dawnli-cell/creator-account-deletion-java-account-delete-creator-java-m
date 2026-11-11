package com.example.creator;

public final class AccountDeletionService {
    private final InfraiClient infrai;
    public AccountDeletionService(InfraiClient infrai) { this.infrai = infrai; }
    public String delete(CreatorWorkspace workspace) {
        workspace.prepareLocalDeletion();
        for (InfraiClient.Session session : infrai.listSessions(workspace.userId())) infrai.revokeSession(session.id());
        infrai.revokeAccountKey(workspace.keyId());
        return "DELETED";
    }
}
