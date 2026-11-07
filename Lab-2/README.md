# Practical 2 - Thermostat, Cinema Booking, and MiniBank Data Classes

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

The required Part A programs model a thermostat with bounded temperature and an active-instance counter, and a cinema show whose bookings cannot exceed available seats and whose cancellations cannot exceed capacity. Part B adds a `Customer` with generated ID and an encapsulated `Account` with generated account number, constructors, deposit/withdraw methods, and no public balance setter. The optional advanced parking-lot exercise is marked for home and was not included.

The official source is the practical-list PDF supplied at `C:\Users\meett\Downloads\2026-27-ODD-CEUE203-OOPJ-PracticalList.pdf`.

## Aim

Practice classes and objects, private fields, constructors, constructor chaining, static members, encapsulation, and arrays of objects.

## Source Files

- [`Thermostat.java`](./pra1/Thermostat.java)
- [`CinemaShow.java`](./pra1/CinemaShow.java)
- [`Customer.java`](./pra2/Customer.java)
- [`Account.java`](./pra2/Account.java)
- [`AccountDemo.java`](./pra2/AccountDemo.java)

## Compile and Run

Run commands from the workspace root:

```powershell
cd Lab-2\pra1
javac Thermostat.java CinemaShow.java
java Thermostat
java CinemaShow
```

Run the MiniBank data-class demonstration:

```powershell
cd ..\pra2
javac AccountDemo.java
java AccountDemo
```

JDK 17 or newer is required.

## Sample Checks

### Thermostat

The program creates two thermostats, raises the living-room temperature to its maximum, then lowers it to its minimum. Boundary calls report the corresponding maximum/minimum message; the final active count is `2`.

### Cinema show

```text
Book 6: true, seats available: 4
Book 5: false, seats available: 4
Cancel 3, seats available: 7
Book 4: true, seats available: 3
Cancel 10, seats available: 10
Default show seats: 100
Total tickets booked: 10
```

### MiniBank account

The demo creates three accounts, deposits 2000 into an account that started at 5000, successfully withdraws 3000, then attempts to withdraw 10000:

```text
AC0001 balance after deposit: 7000
Withdraw 3000 successful: true
AC0001 balance after withdrawal: 4000
Withdraw 10000 successful: false
AC0001 balance after failed withdrawal: 4000
Deposit rejected: amount cannot be negative.
Withdrawal rejected: amount cannot be negative.
Negative withdrawal successful: false
Balance after rejected negative amounts: 4000
```

## Explanation

`Thermostat` validates the starting temperature and uses static state to count instances. `CinemaShow` updates the seat count and cumulative bookings only for accepted bookings, while limiting cancellations to its capacity. `Customer` and `Account` keep their data private and expose getters; account balances change only through deposit and withdrawal methods. IDs are generated with static counters.

## Word Submission

Prepare `25CS104_MeetTanti_Practical2.docx` with the program title and problem summary, complete Java source code, and actual VS Code execution screenshots. Use Consolas or Courier New for code and include your own GitHub repository URL in the footer once that repository exists. No screenshots or Word document have been fabricated or created here.

Capture real output by running the programs in the VS Code terminal and using Windows Snipping Tool (`Win`+`Shift`+`S`).
