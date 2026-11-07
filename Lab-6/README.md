# Practical 6 - Interfaces, Lambdas, Packages, and Runnable JAR

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A implements switchable devices with a default toggle and demonstrates device permissions using both an anonymous class and a lambda. A second example broadcasts a message through email and SMS notifier lambdas, sending twice to the urgent sender. Part B adds banking interfaces, a default yearly-interest method, withdrawal rules, packages, a static import and a runnable MiniBank JAR. The optional advanced discount-engine exercise is marked for home and was not included.

The official source is the practical-list PDF supplied at `C:\Users\meett\Downloads\2026-27-ODD-CEUE203-OOPJ-PracticalList.pdf`.

## Aim

Practice interfaces, default methods, functional interfaces, lambdas, anonymous classes, marker interfaces, packages, static imports and runnable JAR packaging.

## Source Files

Part A sources are in [`pra1`](./pra1): switchable device types and `RemoteControlDemo`, plus `Notifier`, `Urgent`, `UrgentNotifier`, and `NotificationDemo`.

Part B sources are in [`pra2/model`](./pra2/model), [`pra2/util`](./pra2/util), and [`pra2/service`](./pra2/service). The runnable archive is `pra2/minibank.jar`, built from `pra2/MANIFEST.MF`.

## Compile and Run

From the workspace root, compile and run Part A:

```powershell
cd Lab-6\pra1
javac *.java
java RemoteControlDemo
java NotificationDemo
```

Build and run the Part B runnable JAR from its folder:

```powershell
cd ..\pra2
javac -d out .\model\*.java .\util\*.java .\service\*.java
jar --create --file minibank.jar --manifest MANIFEST.MF -C out .
java -jar minibank.jar
```

Enter `5` at the menu to exit. JDK 17 or newer is required.

## Expected Checks

- Both devices toggle on through `Switchable.toggle()`.
- The anonymous permission and lambda each return a boolean result for a device and hour.
- The email notifier is marked urgent and sends twice; the SMS notifier sends once.
- MiniBank prints yearly savings interest, exercises both withdrawal-rule implementations, uses the static-imported mobile validator, and displays its menu.
- `java -jar minibank.jar` starts `service.MiniBank`; the manifest specifies that main class.

## Explanation

`Switchable` declares on/off/state operations and supplies a default toggle. `SwitchPermission` and `WithdrawRule` are functional interfaces, so each can be implemented by a lambda; an anonymous class is also shown. `Urgent` is a method-free marker, and `UrgentNotifier` combines it with the notifier contract. In Part B, `Account` implements `Transactable` and `InterestBearing`, whose default method calculates yearly interest from balance and rate. Project classes use the `model`, `util` and `service` packages.

## Word Submission

Prepare `25CS104_MeetTanti_Practical6.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots, and your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
