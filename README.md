# Splite Expense

Splite Expense is a web application designed to help users manage and track shared expenses among friends, family, or group members. Whether it's splitting the cost of a dinner, a trip, or any shared expense, Splite Expense simplifies the process and keeps everyone informed about their financial responsibilities.

## Features

- **User Authentication**: Users can sign up and log in to their accounts securely.
- **Expense Creation**: Create and manage shared expenses easily, including details like description, total amount, and contributors.
- **Expense Notifications**: Automatically notify group members via email about new expenses and their respective shares.
- **Real-Time Updates**: View all expenses in real-time and track who owes what.
- **Responsive Design**: A user-friendly interface that works seamlessly on both desktop and mobile devices.

## Technology Stack

- **Backend**: Java 8+, Spring Boot, PostgreSQL
- **Frontend**: React.js
- **Messaging Queue**: RabbitMQ for asynchronous task processing
- **Email Service**: Spring Email for sending notifications
- **Database Migration**: Flyway for managing database migrations

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven or Gradle
- PostgreSQL database
- RabbitMQ server
- Node.js and npm (for the frontend)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/splite-expense.git
   cd splite-expense
