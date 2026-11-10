# MiniBank

MiniBank is a standalone application integrating the MiniBank-related concepts from
Practicals 1–14. It starts from the Practical 14 MiniBank implementation and evolves it
without changing the original `Lab-1` through `Lab-14` folders. Independent Part A
practice programs remain in their practical folders and are not part of this application.

## Requirements

- JDK 25 LTS
- Maven

## Build and test

Run these commands from the `MiniBank` directory:

```powershell
mvn clean verify
mvn package
```

The JUnit 5 tests cover customer cloning and persistence, command parsing, statement
formatting, account repository behavior, sorted transaction history, annotation validation,
and transaction processing.

## Run

```powershell
java -jar .\target\minibank-practical14-1.0.0.jar
```

On first run, the application creates a sample savings account. Choose `5` in the menu to
save and exit. Account state, transaction logs, and reports are written under
`data\minibank` relative to the current working directory.

For deposits and withdrawals, choose the corresponding menu option and enter a command:

```text
DEPOSIT <account-number> <amount>
WITHDRAW <account-number> <amount>
```

Transfers use the separate source-account, destination-account, and amount prompts.

## Architecture and features

- `model`: customer and address data, account base type and savings/current/fixed-deposit
  variants, banking interfaces, and runtime annotations.
- `service`: console application, the generic `@Id` account repository, transaction
  processing and history, transfer coordination, persistence, logging, and reports.
- `util`: reflection-based annotation validation and input-format validation.
- `src/test/java`: JUnit 5 tests.

The application supports account creation, deposits, withdrawals, transfers, sorted account
listing and transaction history, serialized account-state save/load, append-only transaction
logs, and end-of-day reports. Customer records have generated IDs, email/mobile details,
and an address; account state persists these records. Customer cloning follows the practical's
shallow-copy behavior, with an immutable address object.

## Practical integration

| Practical | MiniBank concept carried into this application |
|---:|---|
| 1 | Console menu, loop, and switch-based operation selection. |
| 2 | Encapsulated account/customer data, constructors, generated identifiers, deposits, and withdrawals. |
| 3 | Account string/equality/hash behavior, nested customer address, and customer cloning. |
| 4 | Email/mobile and amount validation, deposit/withdraw command parsing, and account statement formatting. |
| 5 | Abstract account type with savings, current, and fixed-deposit implementations and polymorphic behavior. |
| 6 | Banking interfaces, withdrawal rule examples, lambdas, and package organization. |
| 7 | Runtime `@Id`, `@Positive`, and `@MaxLength` annotations with reflection validation. |
| 8 | Checked banking exceptions and try-with-resources demonstration. |
| 9 | Synchronized account operations and worker-thread race/safety demonstrations. |
| 10 | Thread-pool and producer-consumer demonstrations plus deadlock-safe ordered transfers. |
| 11 | Serialized account state, transaction logging, and NIO end-of-day reports. |
| 12 | Concurrent-map account storage and comparator-sorted account/transaction collections. |
| 13 | Reflection-based generic `@Id` repository with CRUD and varargs lookup. |
| 14 | Layered Maven application, runnable JAR, and automated JUnit verification. |

The generic repository is the single account-store implementation: it uses a concurrent map
and supersedes the earlier standalone account-store class. The transaction processor and
bounded buffer are retained as demonstrations; actual transfers use the account-number lock
ordering coordinator.

## Validation and limitations

Customer name/contact lengths are checked using the reflection-based annotations; email and
mobile formats are checked when opening an account. Deposit/withdraw commands require a
positive whole-number amount, and account operations also enforce their banking rules. A
zero opening balance remains allowed; therefore `@Positive` account-balance validation is
demonstrated separately rather than applied to that workflow. Address fields are stored as
entered.

Data is stored locally in serialized files and text logs; this is an educational console
application, not a multi-user service. It does not use a database, online banking, external
APIs, or an authentication system.
