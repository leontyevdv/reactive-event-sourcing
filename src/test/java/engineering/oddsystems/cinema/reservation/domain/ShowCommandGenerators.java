package engineering.oddsystems.cinema.reservation.domain;

import static engineering.oddsystems.cinema.reservation.domain.DomainGenerators.randomSeatNumber;

import engineering.oddsystems.cinema.reservation.domain.ShowCommand.CancelSeatReservation;
import engineering.oddsystems.cinema.reservation.domain.ShowCommand.ReserveSeat;

public class ShowCommandGenerators {

  public static ReserveSeat randomReserveSeat(ShowId showId) {
    return new ReserveSeat(showId, randomSeatNumber());
  }

  public static CancelSeatReservation randomCancelSeatReservation(ShowId showId) {
    return new CancelSeatReservation(showId, randomSeatNumber());
  }

}
