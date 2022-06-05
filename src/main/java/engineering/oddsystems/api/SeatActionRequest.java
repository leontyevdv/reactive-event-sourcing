package engineering.oddsystems.api;

enum Action {
  RESERVE, CANCEL_RESERVATION
}

public record SeatActionRequest(Action action) {

}