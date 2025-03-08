package uk.gov.dwp.uc.pairtest.pricing;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class DefaultTicketPricingStrategy implements TicketPricingStrategy{

    private static final int ADULT_PRICE = 25;
    private static final int CHILD_PRICE = 15;
    // Infants are free and do not receive a seat allocation.

    @Override
    public int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests) {
        int totalAmount = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT:
                    totalAmount += request.getNoOfTickets() * ADULT_PRICE;
                    break;
                case CHILD:
                    totalAmount += request.getNoOfTickets() * CHILD_PRICE;
                    break;
                case INFANT:
                    // Infants are free
                    break;
            }
        }
        return totalAmount;
    }

    @Override
    public int calculateTotalSeats(TicketTypeRequest... ticketTypeRequests) {
        int totalSeats = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT:
                case CHILD:
                    totalSeats += request.getNoOfTickets();
                    break;
                case INFANT:
                    // Infants do not get a seat
                    break;
            }
        }
        return totalSeats;
    }
}
