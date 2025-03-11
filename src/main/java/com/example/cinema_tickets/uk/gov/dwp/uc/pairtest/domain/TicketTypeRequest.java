package com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable object representing a ticket type request.
 */
public class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    @JsonCreator
    public TicketTypeRequest(@JsonProperty("ticketType") Type type,
                             @JsonProperty("noOfTickets") int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT, CHILD, INFANT
    }
}
