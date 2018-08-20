package OnlineMarket.controller.web;

import OnlineMarket.listener.OnReviewApprovedEvent;
import OnlineMarket.service.RatingService;
import OnlineMarket.util.ResponseResult;
import OnlineMarket.util.exception.rating.RatingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rating")
public class ApiRatingController {

    @Autowired
    RatingService ratingService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/modify-review-state", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST,
            RequestMethod.GET })
    public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "id") Integer id) {
        boolean error = false;
        String message = "";
        try {
            eventPublisher.publishEvent(new OnReviewApprovedEvent(ratingService.activeRating(id)));
        } catch (RatingNotFoundException e) {
            error = true;
            message = e.getMessage();
        }

        return ResponseEntity.ok(new ResponseResult(error, message));
    }

}
