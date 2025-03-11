package com.example.cinema_tickets.uk.gov.dwp.uc.pairtest;

import com.example.cinema_tickets.thirdparty.paymentgateway.TicketPaymentService;
import com.example.cinema_tickets.thirdparty.paymentgateway.TicketPaymentServiceImpl;
import com.example.cinema_tickets.thirdparty.seatbooking.SeatReservationService;
import com.example.cinema_tickets.thirdparty.seatbooking.SeatReservationServiceImpl;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.pricing.DefaultTicketPricingStrategy;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.pricing.TicketPricingStrategy;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.validation.DefaultTicketValidator;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.validation.TicketValidator;

public class TicketServiceImpl implements TicketService {
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;
    private final TicketValidator ticketValidator;
    private final TicketPricingStrategy pricingStrategy;

    /**
     * Default constructor that instantiates the external services and default implementations.
     */
    public TicketServiceImpl() {
        this(new TicketPaymentServiceImpl(),
                new SeatReservationServiceImpl(),
                new DefaultTicketValidator(),
                new DefaultTicketPricingStrategy());
    }

    /**
     * Constructor for dependency injection (useful for testing and flexibility).
     */
    public TicketServiceImpl(TicketPaymentService paymentService,
                             SeatReservationService seatReservationService,
                             TicketValidator ticketValidator,
                             TicketPricingStrategy pricingStrategy) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
        this.ticketValidator = ticketValidator;
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        // Validate the ticket request
        ticketValidator.validate(accountId, ticketTypeRequests);

        // Calculate payment and seat allocation using strategy
        int totalAmountToPay = pricingStrategy.calculateTotalAmount(ticketTypeRequests);
        int totalSeatsToAllocate = pricingStrategy.calculateTotalSeats(ticketTypeRequests);
        // Process payment and reserve seats
        paymentService.makePayment(accountId, totalAmountToPay);
        seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);
    }

}
