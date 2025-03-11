package com.example.cinema_tickets.dto;

import java.util.List;

import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketPurchaseRequest {
    private final Long accountId;
    private final List<TicketTypeRequest> ticketRequests;

    @JsonCreator
    public TicketPurchaseRequest(@JsonProperty("accountId") Long accountId,
                                 @JsonProperty("ticketRequests") List<TicketTypeRequest> ticketRequests) {
        this.accountId = accountId;
        this.ticketRequests = ticketRequests;
    }

    public Long getAccountId() {
        return accountId;
    }

    public List<TicketTypeRequest> getTicketRequests() {
        return ticketRequests;
    }
}
