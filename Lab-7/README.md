# Practical 7 - Annotations and Reflection

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A demonstrates field annotations for signup-form validation and a small reflection-based test runner that invokes only methods marked `@Run`. Part B extends MiniBank with runtime `@Id`, `@Positive`, and `@MaxLength` annotations and a reflection-based validator. The optional column-mapping exercise is for advanced learners and is not included.

## Aim

Define runtime annotations, use `@Retention` and `@Target`, inspect fields and methods through reflection, validate object data, and apply Java's `@Override` and `@FunctionalInterface` annotations to existing code.

## Source Files

- Part A programs: [`pra1`](./pra1)
- MiniBank model and annotations: [`pra2/model`](./pra2/model) and [`pra2/model/annotation`](./pra2/model/annotation)
- Reflection validator and service: [`pra2/util`](./pra2/util) and [`pra2/service`](./pra2/service)

## Compile and Run

From `Lab-7\pra1`:

```powershell
javac -Xlint:all *.java
java FormValidatorDemo
java MiniTestRunnerDemo
```

From `Lab-7\pra2`:

```powershell
javac -Xlint:all -d out .\model\annotation\*.java .\model\*.java .\util\*.java .\service\*.java
java -cp out service.MiniBank
```

JDK 17 or newer is required.

## Expected Checks

- The form validator reports the blank name and overlong email, while a valid form returns no errors.
- The mini runner invokes only the two no-argument methods marked `@Run` and reports a count of two.
- The MiniBank validator returns `must be > 0` for a negative balance and an empty array for a valid account.
- The supplementary overlong owner-name example reports the `@MaxLength` violation.

## Explanation

Part A's `@NotBlank`, `@MaxLength`, and `@Run` annotations are retained at runtime so reflection can inspect them. `FormValidator` reads annotated fields and returns a list of messages; `MiniTestRunner` invokes only annotated no-argument methods. Part B places the runtime field annotations in `model.annotation`. `AnnotationValidator.validate(Object)` examines each class's declared fields up the object's class hierarchy, makes annotated fields accessible, reads their values, and returns all positive-number and maximum-length errors. `@Id` marks the account key for a later generic-repository exercise. The Practical 7 `Account` constructor accepts a negative opening balance so the required runtime-validation case can be demonstrated; the MiniBank menu uses a separate valid account.

Practical 7 also introduces the standard `@Override` and `@FunctionalInterface` annotations. Existing override implementations through Practical 6 were annotated without changing their behavior.

## Word Submission

Prepare `25CS104_MeetTanti_Practical7.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots, and your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
