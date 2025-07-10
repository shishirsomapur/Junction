# Junction

**Junction** is a full-stack Document Management System built using **React**, **Spring Boot**, and **MySQL**, enabling users to securely create, edit, and manage documents with advanced access control and sharing features.

> 🚧 **Note:** This project is actively under development. Expect frequent updates, feature additions, and improvements.

---

## ✨ Features

### 🔐 Authentication & Security
- User registration with email verification
- Secure JWT-based login/logout
- Password reset with email link (token expires in 15 mins)
- Session management using localStorage

### 📄 Document Management
- Create, edit, and delete documents
- Rich text editor using **React Quill**
- Document attributes: `title`, `content`, `visibility`, `shared users`
- Visibility settings:
  - `PRIVATE`: Only the owner can access
  - `PUBLIC`: Visible to all users
  - `SHARED`: Specific users with shared access

### 👥 Access Control
- Only authenticated users can access document functionality
- Document creator can manage visibility and sharing
- Shared documents appear in the recipient's dashboard

### 📬 Email Integration
- Sends verification email on registration
- Sends reset password link on request
- Uses **Gmail SMTP** via Spring Boot

### 🖥️ Frontend
- Built with **React + Vite**
- Styled with **Tailwind CSS**
- Modals for login, register, forgot/reset password
- View and search documents
- Route protection and session management

### 🛠 Backend
- Built with **Spring Boot (Java 17)**
- RESTful APIs with Spring Security and JWT
- Email support using Java Mail Sender
- Uses **MySQL** and **JPA (Hibernate)** for persistence

---

## ⚙️ Tech Stack

| Layer      | Technology                          |
|------------|--------------------------------------|
| Frontend   | React, Vite, Tailwind CSS, Redux     |
| Backend    | Spring Boot, Spring Security, JWT    |
| Database   | MySQL                                |
| Editor     | React Quill (WYSIWYG)                |
| Email      | Gmail SMTP with JavaMailSender       |

---

## 🖥️ Setup & Installation

### Prerequisites
- **Java 17**
- **Node.js v18+**
- **MySQL** running locally with a database named `junction`
- **Gmail App Password** for email sending

### Step-by-step Setup (Run All in Terminal)
```bash
# 1. Install Java 17 (Ubuntu / Debian)
sudo apt update
sudo apt install openjdk-17-jdk -y

# Verify Java installation
java -version   # should print Java 17

# 2. Clone the repository
git clone https://github.com/shishirsomapur/junction.git
cd junction

# 3. Setup MySQL (if not done already)
# Login to MySQL and create the database
mysql -u root -p
CREATE DATABASE junction;

# 4. Backend Setup (Spring Boot)
cd junction

# Configure 'src/main/resources/application.properties'
# Use your own Gmail App Password and email for sending mails

# Sample 
# ------------------------------------------
# spring.datasource.username=root
# spring.datasource.password=your_password
# spring.datasource.url=jdbc:mysql://localhost:3306/junction
# jwt.secret=your_base64_secret_key
# spring.mail.username=your_email@gmail.com
# spring.mail.password=your_app_password

# Run backend
./mvnw spring-boot:run

# 5. Frontend Setup (React + Vite)
cd junction
npm install
npm run dev

# Frontend will run on: http://localhost:5173
# Backend will run on: http://localhost:8080
