# 📚 Course Management System

## 🧾 Project Overview

The **Course Management System** is a platform that allows **teachers** to upload and manage educational resources (courses, assignments, practical work, etc.) and **students** to consult courses and submit their work before defined deadlines.

The system is designed to simplify course distribution, assignment submission, and evaluation in an academic environment.

---

## 🎯 Main Objectives

* Centralize course materials in one platform
* Allow teachers to define deadlines and collect student submissions
* Enable students to access courses and submit assignments easily
* Ensure secure access with role-based permissions

---

## 👥 User Roles

### 👨‍🏫 Teacher

* Upload courses and educational materials
* Define deadlines for assignments
* Access student submissions
* Download submitted work
* Assign grades and feedback

### 👨‍🎓 Student

* View available courses by subject
* Download course materials
* Submit assignments before deadlines
* View submitted work

---

## 🔐 Functional Requirements

### 1. Authentication

* Login using username/email and password
* Role-based access (Teacher / Student)

### 2. Teacher Features

* Add course type (Course, TD, TP, etc.)
* Create a course (title, subject, file upload)
* Define a deadline (optional depending on course type)
* Automatic creation of a submission space if a deadline exists
* View student submissions
* Download submitted files
* Grade student work

### 3. Student Features

* View list of available courses by subject
* See deadlines for each course
* Download course files
* Submit assignments in the submission space
* View submitted assignments

---

## ⚙️ Non-Functional Requirements

| Category     | Requirement                                                      |
| ------------ | ---------------------------------------------------------------- |
| Ergonomics   | Simple, clear, and user-friendly interface adapted to both roles |
| Security     | Secure authentication, role management, password encryption      |
| Performance  | Fast response time (< 2 seconds) for uploads and downloads       |
| Storage      | Efficient file management, up to 50 MB per file                  |
| Availability | System available 24/7 with minimal downtime                      |

---

## 🛠️ Technologies (To Be Defined)

* Programming Language: **Java**
* Architecture: Desktop or Web (depending on implementation)
* Database: MySQL / File-based storage
* UI: JavaFX

---

## 🚀 Future Improvements

* Internal messaging system
* Notifications for deadlines
* Admin role
* Advanced grading and feedback system

---

## 📌 Project Status

🚧 **Work in progress** – Core features are partially implemented.

---

## 👤 Author

* **Jasser Mhamdi**

---

## 📄 License

This project is developed for educational purposes.
