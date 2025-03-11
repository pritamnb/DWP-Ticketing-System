package com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.validation;

import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class DefaultTicketValidator implements TicketValidator{
    @Override
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
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

        for (TicketTypeRequest request : ticketTypeRequests) {
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
        }
        totalTickets += (adultTickets + childTickets);

        if (adultTickets == 0 && (childTickets > 0 || infantTickets > 0)) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without an Adult ticket");
        }

        if(adultTickets == 0 || adultTickets < 0){
            throw new InvalidPurchaseException("Adult ticket must be at least 1");
        }

        if(infantTickets > adultTickets){
            throw new InvalidPurchaseException("Infant tickets should not be greater than adult number of tickets");
        }

        if (totalTickets > 25) {
            throw new InvalidPurchaseException("Cannot purchase more than 25 tickets at a time");
        }

    }
}
