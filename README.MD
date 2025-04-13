# Barber Shop Management System API

![Barber Shop](https://img.icons8.com/color/96/000000/barber.png)

This project is part of the **Web Development Program Using Java and SpringBoot** presented by **Tuwaiq Academy**.

## Table of Contents
- [Models](#models)
- [Controllers](#controllers)
  - [Appointment](#appointment-controller)
  - [Barber](#barber-controller)
  - [Client](#client-controller)
  - [Order](#order-controller)
  - [Review](#review-controller)
  - [ServiceItem](#serviceitem-controller)
  - [Shop](#shop-controller)
- [Services](#services)
- [Contact Information](#contact)

## Models

### Appointment
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| appointmentDate | LocalDateTime | Not null | Scheduled date/time |
| appointmentFinishTime | LocalDateTime | Calculated | End time (date + service duration) |
| barberId | Integer | Not null | Associated barber ID |
| clientId | Integer | Not null | Associated client ID |
| orderId | Integer |  | Linked order ID |

### Barber
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| age | Integer | @Positive | Barber's age |
| firstName | String | @Size(min=4), Not empty | First name |
| lastName | String | @Size(min=4), Not empty | Last name |
| phoneNumber | String | @Pattern(05[0-9]{8}), Unique | Barber phone number |

### Client
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| age | Integer | @Positive | Client's age |
| firstName | String | @Size(min=4), Unique | First name |
| lastName | String | @Size(min=4), Unique | Last name |
| phoneNumber | String | @Pattern(05[0-9]{8}), Unique | Client phone number |
| points | Integer | Default 0 | Loyalty points |

### Order
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| orderPrice | Double | Not null | Order price |
| orderDone | Boolean | Default false | Completion status |
| canceled | Boolean | Default false | Cancellation status |
| serviceItemId | Integer |  | Service reference |
| appointmentId | Integer |  | Appointment reference |
| barberId | Integer |  | Barber reference |
| clientId | Integer |  | Client reference |
| reviewId | Integer |  | Review reference |

### Review
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| description | String | Max 100 chars | Optional review text |
| rating | Double | @Min(1), @Max(5) | Rating (1-5 stars) |
| upVotes | Integer | Default 0 | Helpful votes |
| downVotes | Integer | Default 0 | Unhelpful votes |
| reply | String |  | Owner response |
| clientId | Integer |  | Client ID |
| orderId | Integer |  | Order reference |

### ServiceItem
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| servicePrice | Double |  | Service cost |
| serviceName | String | @Size(min=4), Unique | Service name |
| durationsInMinute | Integer | Not null | Service duration |

### Shop
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| numberOfPointsNeededForOver | Integer | Default 2 | Loyalty threshold |
| shopInitialized | Boolean | Default false | System init flag |
### Vote
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Integer | Auto-generated | Unique identifier |
| vote | String | "up" or "down" | Vote type|
| reviewId | Integer |  | Reviewer ID |
| clientId | Integer |  | Client ID |

## Controllers

### Appointment Controller
| Endpoint | Method | Parameters | Description |
|----------|--------|------------|-------------|
| `/book-appointment/{clientId}/{barberId}/{date}/{serviceName}` | POST | clientId, barberId, date(LocalDateTime), serviceName | Create new appointment |
| `/reschedule-appointment/{appointmentId}/{clientId}/{barberId}/{date}/{serviceName}` | PUT | appointmentId, clientId, barberId, date(LocalDateTime), serviceName | Update existing appointment |
| `/change-barber/{appointmentId}/{barberId}` | PUT | appointmentId, barberId | Modify assigned barber |

### Barber Controller
| Endpoint | Method | Parameters | Description |
|----------|--------|------------|-------------|
| `/get-barber-appointments/{barberId}` | GET | barberId | Get barber's appointments |
| `/get-barber-total-revenue/{barberId}` | GET | barberId | Calculate barber's earnings |
| `/get-barber-stats/{barberId}` | GET | barberId | Get performance stats |

### Client Controller
| Endpoint | Method | Parameters | Description |
|----------|--------|------------|-------------|
| `/check-barber-availability/{barberId}/{date}` | GET | barberId, date(LocalDateTime) | Check barber availability |
| `/get-client-appointments/{clientId}` | GET | clientId | Get client's appointments |
| `/check-if-client-eligible-for-over/{clientId}` | GET | clientId | Check loyalty eligibility |
| `/redeem-over/{clientId}/{orderId}` | PUT | clientId, orderId | Redeem loyalty points |

### Order Controller
| Endpoint | Method | Parameters | Description |
|----------|--------|------------|-------------|
| `/complete-order/{orderId}` | PUT | orderId | Mark order as complete |
| `/cancel-order/{orderId}` | PUT | orderId | Cancel order |
| `/apply-discount/{orderId}/{numberOfServicesNeeded}/{discountAmount}` | PUT | orderId, numberOfServicesNeeded, discountAmount | Apply discount |

### Review Controller
| Endpoint | Method | Parameters | Description |
|----------|--------|------------|-------------|
| `/review-order/{clientId}/{orderId}/{rating}` | POST | clientId, orderId, rating | Submit rating |
| `/review-order-with-description/{clientId}/{orderId}/{rating}/{description}` | POST | clientId, orderId, rating, description | Submit detailed review |
| `/get-client-reviews/{clientId}` | GET | clientId | Get client's reviews |
| `/add-description-to-a-review/{reviewId}/{description}` | PUT | reviewId, description | Add review text |
| `/up-vote-review/{reviewId}` | PUT | reviewId | Upvote review |
| `/down-vote-review/{reviewId}` | PUT | reviewId | Downvote review |
| `/add-reply-to-review/{reviewId}/{reply}` | PUT | reviewId, reply | Owner response |
| `/get-barber-avg-rating/{barberId}` | GET | barberId | Calculate a barber average rating |
| `/get-top-rated-barber` | GET | None | Find highest-rated barber |


### Shop Controller
| Endpoint | Method | Parameters | Description |
|----------|--------|------------|-------------|
| `/get-shop-total-revenue` | GET | None | Calculate total revenue |
| `/initialize-shop` | PUT | None | First-run setup |
| `/is-shop-initialized` | GET | None | Check initialization status |

## Services

### Key Service Methods

**AppointmentService**
- `requestAppointment()`: Validates and creates new appointments
- `rescheduleAppointment()`: Handles appointment modifications
- `validateAppointment()`: Checks time conflicts and valid references
- `checkBarberAvailability()`: Verifies barber schedule availability

**ReviewService**
- `reviewOrder()`: Processes new ratings with validation
- `getTopRatedBarber()`: Calculates and returns highest-rated barber
- `calculateBarberAvgRating()`: Computes average rating for barber

**OrderService**
- `completeOrderStatus()`: Marks orders as completed
- `applyDiscountToAnOrder()`: Applies conditional discounts
- `cancelOrder()`: Handles order cancellations

**ClientService**
- `redeemOver()`: Manages loyalty point redemption
- `checkIfClientEligibleForOver()`: Verifies loyalty status

## Contact Information
For any questions, feedback, or collaboration opportunities, feel free to reach out:

- **Email**: Ayman.f.alharbi@gmail.com