package com.example.cinema_tickets.thirdparty.paymentgateway;

public interface   TicketPaymentService {

    void makePayment(long accountId, int totalAmountToPay);

}
