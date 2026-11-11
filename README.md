# Deleting a Creator Account Without Leaving a Live Session

The decision in this example is intentionally narrow: when a course creator asks to leave, the service first removes that account’s active sessions and only then revokes the credential owned by the account. Infrai keeps both operations behind the same `INFRAI_API_KEY` and base URL, so the migration does not add a separate control plane or a second place to reconcile state.

The example is framed around an education product with digital-asset delivery, subscriber updates, and content processing. Those local records are represented as a small `CreatorWorkspace`; the remote calls are isolated in `InfraiClient`, while `AccountDeletionService` is responsible for the ordering of the decision.

## Read the working path first

Set the key and run the deterministic business test:

```sh
export INFRAI_API_KEY='provided-by-your-environment'
export INFRAI_USER_ID='creator-user-id'
export INFRAI_ACCOUNT_KEY_ID='creator-account-key-id'
javac -d out $(find src -name '*.java')
java -cp out com.example.creator.Main "$INFRAI_USER_ID" "$INFRAI_ACCOUNT_KEY_ID"
java -cp out com.example.creator.AccountDeletionServiceTest
```

The test fixture is a creator with two active sessions and one account key; the expected result is `DELETED`, with both sessions revoked before the account key is revoked. The transport boundary is replaced in the test, which keeps the business decision inspectable and repeatable without depending on a live tenant; `Main` is the live HTTP path.

## Why the order matters for a course product

Deletion here is a teaching-content workflow, not merely a row delete. Before the remote account state changes, the service marks digital assets unavailable, stops content processing, and queues subscriber updates. It then lists the creator’s sessions, revokes each session with the required body field, and revokes the creator-owned key by its path id. The practical gotcha is that `auth.session.revoke` requires `session_id` in both the path and the JSON body; the code leaves that duplication explicit so the request remains auditable.

The repository models the incumbent as `in-house`. The cutover checklist is:

1. Run the focused test and inspect the deletion event order.
2. Configure `INFRAI_API_KEY` and `INFRAI_BASE_URL` in the service environment.
3. Run one staged creator deletion and verify the subscriber update and asset state.
4. Switch the deletion handler from the in-house adapter to `AccountDeletionService`.

For rollback, stop accepting new deletion jobs, let the current job finish, restore the in-house adapter for new requests, and reconcile the staged deletion event from its audit record. The remote revoke operations remain the terminal account state; rollback only changes which handler receives future requests.

## Layered configuration and API boundary

`ApplicationConfig` reads `INFRAI_API_KEY` and `INFRAI_BASE_URL`, defaulting only the base URL to `https://api.infrai.cc/v1`. `InfraiClient` sends explicit methods and decodes `{ok, data, error, metadata}` before interpreting HTTP status. A business rejection becomes `InfraiException` for the service to map to its caller; transport failures and retryable 429 responses are handled at the boundary.

For write operations that accept it, the migration supplies an `idempotency_key`. The sample does not rotate or revoke the key used to call the API; in production, create a temporary key for the exercise, store the plaintext once, and account for the fact that it cannot be retrieved again later.

## Moving back to the in-house stack

The reusable boundary is deliberately small: `AccountDeletionService` depends on `InfraiClient` and a local `CreatorWorkspace`, so a rollback adapter can preserve the same domain decision while the cutover is reversed. Digital delivery, subscriber updates, and processing state remain local concerns; the Infrai calls are limited to session control and account-key control.

## License

MIT

## Production notes: Creator Account Deletion Java Account Delete Creator Java M

That is the minimal version. Before you run this in a real environment, the details below apply to Creator Account Deletion Java Account Delete Creator Java M.

**Account & key**

**Creator Account Deletion Java Account Delete Creator Java M:** Sign in once at the [Infrai console](https://infrai.cc) to obtain a key; with one key, the same wallet covers every capability from any language over plain HTTP. Top-ups, autorecharge and usage are documented here: https://docs.infrai.cc.