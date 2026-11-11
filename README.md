# Restaurant Management System 🍽️

A desktop application for managing a food-delivery platform: restaurants, customers, orders and delivery riders.
Built in **Java** with a **JavaFX** graphical interface, following **Object-Oriented Programming** principles and the **MVC** pattern.

> Course project: Object-Oriented Programming, B.Sc. Information Systems, University of Haifa.

## ✨ Features

The system has four types of users, each with its own login and dashboard:

| User | What they can do |
|---|---|
| **System Admin** | Manage customers, restaurants, orders, riders and restaurant admins; reports and sorting; save/load data |
| **Restaurant Admin** | View their restaurants, add customers and orders, assign riders to orders, update restaurant rating, close a restaurant, basic reports |
| **Customer** | Place new orders, view order history, update personal details, deposit/withdraw money and check balance, search restaurants |
| **Rider** | View assigned and active orders, update status to *Delivering* / *Delivered*, view delivery history and totals |

Other highlights:
- **Order lifecycle:** `PENDING → DELIVERING → DELIVERED`
- **Reports:** customer with the most orders, rider with the most deliveries, open restaurants by type, sorting by rating and by final order price
- **Data persistence:** data is saved to and loaded from text files
- **Input validation** with custom exceptions

## 🧩 OOP concepts used

- **Inheritance:** `Restaurant` → `FastFoodRestaurant`, `PremiumRestaurant`; `Admin` → `RestAdmin`
- **Encapsulation:** private/protected fields with getters and setters
- **Interfaces:** `Comparable` and custom `Comparator` classes for sorting
- **Generics:** bounded wildcards (`? extends`, `? super`) and generic methods in `SystemReports`
- **Enums:** `OrderStatus`
- **Custom exceptions:** `ValidationException`, `CustomerNotFoundException`, `RestaurantIsNotManagedByThisAdmin`
- **Collections:** `ArrayList`, `HashMap`

## 🗂 Project structure

```
src/
├── application/   # Entry point (MainGUI), data initialization, CSS
├── controller/    # JavaFX controllers for every screen
├── view/          # FXML screens
├── model/         # Core logic: orders, customers, riders, database, reports
├── restaurant/    # Restaurant class hierarchy
├── admin/         # Admin class hierarchy
├── utils/         # File handling and validation helpers
└── textFiles/     # Sample data
```

> The user interface is in Hebrew and English.

## 🛠 Tech stack

Java 21 · JavaFX · FXML · CSS · Eclipse

## ▶️ How to run

1. Install **Java 21** and **JavaFX** (or a JDK that bundles JavaFX, e.g. Liberica Full JDK).
2. Clone the repository and import it into **Eclipse** (*File → Import → Existing Projects into Workspace*).
3. Run `src/application/MainGUI.java`.

**Demo login:** System Admin: username `admin`, password `12345`.
Sample customers, riders and restaurant admins are loaded from `src/textFiles/`.
