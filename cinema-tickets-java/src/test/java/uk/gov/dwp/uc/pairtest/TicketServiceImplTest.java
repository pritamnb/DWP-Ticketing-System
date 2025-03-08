package uk.gov.dwp.uc.pairtest;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

public class TicketServiceImplTest {

    private TicketPaymentService paymentService;
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketService;

    @Before
    public void setUp() {
        paymentService = mock(TicketPaymentService.class);
        seatReservationService = mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(paymentService, seatReservationService);
    }

    @Test
    public void testValidPurchase() {
        // Request: 2 Adult, 1 Child, 1 Infant
        TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(Type.INFANT, 1);

        ticketService.purchaseTickets(1L, adultRequest, childRequest, infantRequest);

        // Expected payment: (2 * £25) + (1 * £15) = £65
        verify(paymentService).makePayment(1L, 65);
        // Expected seats reserved: 2 (Adult) + 1 (Child) = 3 seats (Infant does not get a seat)
        verify(seatReservationService).reserveSeat(1L, 3);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseWithNoAdultTicket() {
        // Only Child ticket provided (should fail)
        TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 1);
        ticketService.purchaseTickets(1L, childRequest);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseExceedingMaxTickets() {
        // Exceeds the maximum of 25 tickets
        TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 26);
        ticketService.purchaseTickets(1L, adultRequest);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testInvalidAccountId() {
        // Invalid account id (<= 0)
        TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
        ticketService.purchaseTickets(0L, adultRequest);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testInvalidTicketNumber() {
        // Request with zero tickets
        TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 0);
        ticketService.purchaseTickets(1L, adultRequest);
    }
}
