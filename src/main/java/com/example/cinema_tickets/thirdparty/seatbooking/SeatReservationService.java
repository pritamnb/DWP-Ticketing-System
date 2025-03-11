package com.example.cinema_tickets.thirdparty.seatbooking;

public interface SeatReservationService {

    void reserveSeat(long accountId, int totalSeatsToAllocate);

}
