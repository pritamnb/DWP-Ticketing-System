package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;

    /**
     * Default constructor that instantiates the external services.
     */
    public TicketServiceImpl() {
        this(new TicketPaymentServiceImpl(), new SeatReservationServiceImpl());
    }

    /**
     * Constructor for dependency injection (useful for testing).
     */
    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService seatReservationService) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        // Validate accountId
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account id");
        }
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("No ticket requests provided");
        }

        int adultTickets = 0;
        int childTickets = 0;
        int infantTickets = 0;
        int totalTickets = 0;

        // Process each ticket request
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request.getNoOfTickets() <= 0) {
                throw new InvalidPurchaseException("Number of tickets must be positive");
            }
            switch (request.getTicketType()) {
                case ADULT:
                    adultTickets += request.getNoOfTickets();
                    break;
                case CHILD:
                    childTickets += request.getNoOfTickets();
                    break;
                case INFANT:
                    infantTickets += request.getNoOfTickets();
                    break;
                default:
                    throw new InvalidPurchaseException("Unknown ticket type");
            }
            totalTickets += request.getNoOfTickets();
        }

        // Validate maximum ticket purchase limit (25 tickets)
        if (totalTickets > 25) {
            throw new InvalidPurchaseException("Cannot purchase more than 25 tickets at a time");
        }

        // Validate that any child or infant tickets require an adult ticket
        if (adultTickets == 0 && (childTickets > 0 || infantTickets > 0)) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without an Adult ticket");
        }

        // Calculate total cost (Infants are free)
        int totalAmountToPay = (adultTickets * 25) + (childTickets * 15);
        // Calculate seats to reserve (only Adult and Child tickets get seats)
        int totalSeatsToAllocate = adultTickets + childTickets;

        // Process payment and seat reservation
        paymentService.makePayment(accountId, totalAmountToPay);
        seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);
    }

}
