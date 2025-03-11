package com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.pricing;

import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
public interface TicketPricingStrategy {
    int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests);
    int calculateTotalSeats(TicketTypeRequest... ticketTypeRequests);
}
