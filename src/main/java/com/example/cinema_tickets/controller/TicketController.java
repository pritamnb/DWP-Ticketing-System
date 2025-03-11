package com.example.cinema_tickets.controller;

import com.example.cinema_tickets.dto.ApiResponse;
import com.example.cinema_tickets.dto.TicketPurchaseRequest;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.TicketService;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.pricing.DefaultTicketPricingStrategy;
import com.example.cinema_tickets.uk.gov.dwp.uc.pairtest.pricing.TicketPricingStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketPricingStrategy pricingStrategy;

    public TicketController() {
        // Using default constructor to instantiate TicketService with default dependencies.
        this.ticketService = new TicketServiceImpl();
        this.pricingStrategy = new DefaultTicketPricingStrategy(); // Inject pricing strategy

    }

    /**
     * Endpoint to purchase tickets.
     * Expects a JSON body with accountId and a list of ticketRequests.
     */
    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse> purchaseTickets(@RequestBody TicketPurchaseRequest request) {
        List<TicketTypeRequest> ticketRequests = request.getTicketRequests();
        TicketTypeRequest[] ticketArray = ticketRequests.toArray(new TicketTypeRequest[0]);

        ticketService.purchaseTickets(request.getAccountId(), ticketArray);
        int totalAmountPaid = pricingStrategy.calculateTotalAmount(ticketArray);

        ApiResponse response = new ApiResponse("SUCCESS", "Ticket purchase successful", totalAmountPaid);
        return ResponseEntity.ok(response);
    }
}
