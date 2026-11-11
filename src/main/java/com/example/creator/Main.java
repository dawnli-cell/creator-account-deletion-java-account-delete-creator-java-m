package com.example.creator;

import java.net.http.HttpClient;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        if (args.length != 2) throw new IllegalArgumentException("usage: Main <user_id> <account_key_id>");
        ApplicationConfig config = ApplicationConfig.fromEnvironment();
        InfraiClient client = new InfraiClient(config, HttpClient.newHttpClient(), List.of());
        CreatorWorkspace workspace = new CreatorWorkspace(args[0], args[1], "delete-" + args[0]);
        System.out.println(new AccountDeletionService(client).delete(workspace));
    }
}
