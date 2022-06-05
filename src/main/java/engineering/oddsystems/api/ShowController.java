package engineering.oddsystems.api;

import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.badRequest;

import engineering.oddsystems.cinema.reservation.application.ShowEntityResponse;
import engineering.oddsystems.cinema.reservation.application.ShowEntityResponse.CommandProcessed;
import engineering.oddsystems.cinema.reservation.application.ShowEntityResponse.CommandRejected;
import engineering.oddsystems.cinema.reservation.application.ShowService;
import engineering.oddsystems.cinema.reservation.domain.SeatNumber;
import engineering.oddsystems.cinema.reservation.domain.ShowId;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/shows")
public class ShowController {

  private final ShowService showService;

  public ShowController(ShowService showService) {
    this.showService = showService;
  }

  @GetMapping(value = "{showId}", produces = "application/json")
  public Mono<ShowResponse> findById(@PathVariable UUID showId) {
    CompletionStage<ShowResponse> showResponse = showService.findShowBy(ShowId.of(showId))
        .thenApply(ShowResponse::from);
    return Mono.fromCompletionStage(showResponse);
  }

  @PatchMapping(value = "{showId}/seats/{seatNum}", consumes = "application/json")
  public Mono<ResponseEntity<String>> reserve(@PathVariable("showId") UUID showIdValue,
      @PathVariable("seatNum") int seatNumValue,
      @RequestBody SeatActionRequest request) {

    ShowId showId = ShowId.of(showIdValue);
    SeatNumber seatNumber = SeatNumber.of(seatNumValue);
    CompletionStage<ShowEntityResponse> actionResult = switch (request.action()) {
      case RESERVE -> showService.reserveSeat(showId, seatNumber);
      case CANCEL_RESERVATION -> showService.cancelReservation(showId, seatNumber);
    };

    return Mono.fromCompletionStage(actionResult.thenApply(response -> switch (response) {
      case CommandProcessed ignored -> accepted().body(request.action() + " successful");
      case CommandRejected rejected ->
          badRequest().body(request.action() + " failed with: " + rejected.error().name());
    }));
  }
}
