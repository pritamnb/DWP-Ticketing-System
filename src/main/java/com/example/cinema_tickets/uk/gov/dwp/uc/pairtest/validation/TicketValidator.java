package com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.validation;

import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
public interface TicketValidator {
    void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException;
}
