# 🏦 Online Banking System (Java + MySQL)

A simple **console-based banking system** built with **Java** and **MySQL**.  
This project demonstrates **Core Java (OOP)**, **JDBC connectivity**, **password hashing**, and **transaction handling with commit/rollback**.  

It is ideal for showcasing in your resume or portfolio as it highlights **Java + Database integration** and secure programming practices.

---

## 🚀 Features
- ✅ **User Registration** with password hashing (SHA-256)  
- ✅ **Login Authentication** with hashed passwords  
- ✅ **Deposit / Withdraw Funds** securely  
- ✅ **Transfer Money** between users (by username)  
- ✅ **Check Balance** at any time  
- ✅ **View Transaction History** with unique `transactionId`, `accountId`, type, amount, and timestamp  
- ✅ Uses **JDBC + MySQL backend**  
- ✅ Demonstrates **transaction rollback** for failed transfers  

---

## 🛠️ Tech Stack
- **Java (JDK 11/17/21)** – Core Java, OOP principles, exception handling  
- **MySQL** – Relational Database for accounts & transactions  
- **JDBC (MySQL Connector/J)** – Database connectivity  
- **SHA-256** – Password hashing for security  

---

## 🗄️ Database Setup

-1. Open MySQL CLI or Workbench.  
-2. Run the SQL file provided (`database.sql`)

## 🐛 Common Errors & Fixes

- **No suitable driver found**  
  → Add **MySQL Connector/J** `.jar` to your classpath.  

- **Access denied for user 'root'@'localhost'**  
  → Check your MySQL username/password and update them in `DatabaseConnector.java`.  

- **Public Key Retrieval is not allowed**  
  → Add `allowPublicKeyRetrieval=true` to your JDBC URL.  

- **InputMismatchException**  
  → Happens when wrong input type is entered. Fixed by safe input parsing (`Integer.parseInt` instead of `nextInt`).  

---

## 💡 Future Enhancements

- Build a **GUI frontend** (JavaFX / Swing)  
- Create a **Web version** using **Spring Boot + REST APIs**  
- Use **bcrypt** instead of SHA-256 for stronger password security  
- Support **multiple accounts per user** (Savings, Current, etc.)  
- Add an **Admin role** to manage users & transactions  
