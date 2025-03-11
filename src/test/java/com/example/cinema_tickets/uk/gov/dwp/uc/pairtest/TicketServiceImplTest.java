package com.example.cinema_tickets.uk.gov.dwp.uc.pairtest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.cinema_tickets.thirdparty.paymentgateway.TicketPaymentService;
import com.example.cinema_tickets.thirdparty.seatbooking.SeatReservationService;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.pricing.DefaultTicketPricingStrategy;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.pricing.TicketPricingStrategy;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.validation.DefaultTicketValidator;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.validation.TicketValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketServiceImplTest {

    private TicketPaymentService paymentService;
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketService;

    @BeforeEach
    public void setUp() {
        paymentService = mock(TicketPaymentService.class);
        seatReservationService = mock(SeatReservationService.class);
        TicketValidator ticketValidator = new DefaultTicketValidator();
        TicketPricingStrategy pricingStrategy = new DefaultTicketPricingStrategy();
        ticketService = new TicketServiceImpl(paymentService, seatReservationService, ticketValidator, pricingStrategy);
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

    @Test
    public void testPurchaseWithNoAdultTicket() {
        // Only Child ticket provided (should fail)
        TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L, childRequest));
    }

    @Test
    public void testPurchaseExceedingMaxTickets() {
        // Exceeds the maximum of 25 tickets
        TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 26);
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L, adultRequest));
    }

    @Test
    public void testInvalidAccountId() {
        // Invalid account id (<= 0)
        TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(0L, adultRequest));
    }

    @Test
    public void testInvalidTicketNumber() {
        // Request with zero tickets
        TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 0);
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L, adultRequest));
    }
}
