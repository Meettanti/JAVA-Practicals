# Practical 4 - Text Validation and MiniBank Command Parsing

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A rates passwords against four rules and filters a small chat log by a case-insensitive keyword, skipping malformed lines. Part B validates mobile, email, PAN and IFSC text with compiled patterns, parses transaction commands into a record, and formats an account statement with `StringBuilder`. The optional advanced template-filler exercise is marked for home and was not included.

The official source is the practical-list PDF supplied at `C:\Users\meett\Downloads\2026-27-ODD-CEUE203-OOPJ-PracticalList.pdf`.

## Aim

Practice strings, regular expressions, `Pattern` and `Matcher`, `StringBuilder`, `split`, enums, records and input validation.

## Source Files

Part A: [`PasswordChecker.java`](./pra1/PasswordChecker.java), [`ChatFilter.java`](./pra1/ChatFilter.java), and [`Driver.java`](./pra1/Driver.java). The single driver runs both exercises to avoid duplicate `Driver` classes in one folder.

Part B: [`Validator.java`](./pra2/Validator.java), [`TransactionType.java`](./pra2/TransactionType.java), [`Command.java`](./pra2/Command.java), [`CommandParser.java`](./pra2/CommandParser.java), [`Account.java`](./pra2/Account.java), [`StatementFormatter.java`](./pra2/StatementFormatter.java), and [`MiniBankValidationDemo.java`](./pra2/MiniBankValidationDemo.java).

## Compile and Run

From the workspace root:

```powershell
cd Lab-4\pra1
javac *.java
java Driver
```

Enter `hello` when the chat filter asks for a keyword.

Run the MiniBank validation demo:

```powershell
cd ..\pra2
javac *.java
java MiniBankValidationDemo
```

JDK 17 or newer is required.

## Sample Checks

The Part A password checks label `abc` as `Weak` and `Abcd1234!` as `Strong`. Entering `hello` for the supplied chat lines prints `Matches: 1` and the matching `10:05 alice: Hello there` line; the malformed line is skipped.

The Part B demo checks each validator with one valid and one invalid value. In particular:

```text
Mobile 9876543210: true
Mobile 12345: false
Command type: DEPOSIT
Command account: AC0001
Command amount: 500
```

It also demonstrates a formatted statement and reports a clear error for a command with the wrong number of parts. `isValidAmount` rejects zero as an additional positive-amount check.

## Explanation

`PasswordChecker` uses compiled patterns for the four password rules and derives the label from the count passed. `ChatFilter` splits each log line into three parts, checks only the message, and builds the matching report. `Validator` uses anchored patterns for each input format. `CommandParser` validates the command shape, converts its type and amount, and returns a `Command` record. `StatementFormatter` assembles an account summary using a `StringBuilder`.

## Word Submission

Prepare `25CS104_MeetTanti_Practical4.docx` with the program title and problem summary, complete Java source code, and actual VS Code execution screenshots. Use Consolas or Courier New for code and include your own GitHub repository URL in the footer once that repository exists. No screenshots or Word document have been fabricated or created here.

Capture real output by running both programs in the VS Code terminal and using Windows Snipping Tool (`Win`+`Shift`+`S`).
