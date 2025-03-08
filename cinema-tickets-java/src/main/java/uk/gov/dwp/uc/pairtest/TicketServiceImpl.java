package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.pricing.DefaultTicketPricingStrategy;
import uk.gov.dwp.uc.pairtest.pricing.TicketPricingStrategy;
import uk.gov.dwp.uc.pairtest.validation.DefaultTicketValidator;
import uk.gov.dwp.uc.pairtest.validation.TicketValidator;

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
