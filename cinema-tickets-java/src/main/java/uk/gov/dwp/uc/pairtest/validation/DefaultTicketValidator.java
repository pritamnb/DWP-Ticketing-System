package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

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

        if (totalTickets > 25) {
            throw new InvalidPurchaseException("Cannot purchase more than 25 tickets at a time");
        }
        if (adultTickets == 0 && (childTickets > 0 || infantTickets > 0)) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without an Adult ticket");
        }
    }
}
