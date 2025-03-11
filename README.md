Here's a README file that explains how to run the Cinema Ticket application locally, along with a detailed explanation of the API.

---

# Cinema Ticket Service

This is a Spring Boot application for purchasing cinema tickets with a focus on handling multiple ticket types, processing payments, and reserving seats.

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17 or higher** (Java 17 is recommended)
- **Maven** (For building the application)
- **IDE** (e.g., IntelliJ IDEA, Eclipse, or Visual Studio Code)

## Running the Application Locally

### Step 1: Clone the Repository

Clone this repository to your local machine using:

```bash
git clone https://github.com/pritamnb/DWP-Ticketing-System.git
cd DWP-Ticketing-System
```

### Step 2: Build the Application

Navigate to the project folder in your terminal, and run the following Maven command to build the project:

```bash
mvn clean install
```

### Step 3: Run the Application

You can now run the application using the following Maven command:

```bash
mvn spring-boot:run
```

Alternatively, you can run the application from your IDE if you have set up the Spring Boot configuration.

Once the application is running, it will start on `http://localhost:8080` by default.

### Step 4: Access the API

The API is ready to receive HTTP requests at the following endpoint:

```bash
http://localhost:8080/tickets/purchase
```

### Step 5: Test the API

You can use tools like **Postman** or **curl** to test the API by sending requests.

---

## API Explanation

### Endpoint: **POST /tickets/purchase**

This endpoint allows the user to purchase tickets. It takes the account ID and a list of ticket requests as input. Based on the ticket request, the service validates and processes the request accordingly.

#### Request Format

```json
{
  "accountId": 1,
  "ticketRequests": [
    { "ticketType": "ADULT", "noOfTickets": 1 },
    { "ticketType": "CHILD", "noOfTickets": 0 },
    { "ticketType": "INFANT", "noOfTickets": 0 }
  ]
}
```

- **accountId** (long) : The account ID of the user making the purchase.
- **ticketRequests** (array of objects): A list of ticket requests where each object contains:
  - **ticketType** (String): The type of ticket being purchased (`ADULT`, `CHILD`, or `INFANT`).
  - **noOfTickets** (int): The number of tickets of that type.

#### Example Request

```json
{
  "accountId": 123,
  "ticketRequests": [
    { "ticketType": "ADULT", "noOfTickets": 2 },
    { "ticketType": "CHILD", "noOfTickets": 1 },
    { "ticketType": "INFANT", "noOfTickets": 1 }
  ]
}
```

#### Response Format

The response will be returned as an object with a status and message:

```json
{
  "status": "SUCCESS",
  "message": "Ticket purchase successful",
  "totalAmount": 65
}
```


#### Validation Rules

1. A **maximum of 25 tickets** can be purchased at a time.
2. **Infants do not require a ticket** and are not allocated a seat.
3. **Child and Infant tickets cannot be purchased without purchasing at least one Adult ticket.**
4. The system will reject invalid purchase requests, such as:
   - No adult ticket is purchased when requesting child or infant tickets.
   - No tickets of any type.
   - Requests that exceed 25 tickets in total.

#### Example Responses

**Valid Request Example:**

```json
HTTP Status : 200 Ok
{
  "status": "SUCCESS",
  "message": "Ticket purchase successful",
  "totalAmount":0
}
```

**Invalid Request Example (No Adult Ticket):**

```json
HTTP Status : 400 Bad Request
{
  "status": "FAILURE",
  "message": "Child and Infant tickets cannot be purchased without an Adult ticket"
}
```


### Testing the Cinema Ticket Service
This project includes unit tests to validate the business logic, ensuring that ticket purchases follow the given constraints and rules.

![Ticket Booking Flow](testcases.png)



## Services and Business Logic

The system contains two external services:

1. **TicketPaymentService**: Handles payment processing. This service is assumed to work and will always succeed when a payment request is made.
2. **SeatReservationService**: Handles seat reservations for the tickets. Similar to the payment service, this will always succeed when a reservation request is made.

### Ticket Pricing

The following ticket types are supported, along with their prices:

- **INFANT**: Free of charge (£0), no seat required.
- **CHILD**: £15 per ticket.
- **ADULT**: £25 per ticket.

### Constraints and Assumptions

- All accounts with an ID greater than zero are valid.
- Infants do not need seats, and only adults occupy actual seats.
- The payment and seat reservation will always succeed when requested, as the services are assumed to function without defects.
