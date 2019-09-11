package OnlineMarket.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import OnlineMarket.listener.OnReviewApprovedEvent;
import OnlineMarket.result.api.ResponseResult;
import OnlineMarket.service.RatingService;
import OnlineMarket.util.exception.rating.RatingNotFoundException;

@RestController
@RequestMapping("/api/rating")
public class ApiRatingController {

    @Autowired
    RatingService ratingService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/modify-review-state", method = { RequestMethod.POST,
            RequestMethod.GET })
    public ResponseEntity<?> checkSlugUnique(@RequestParam(value = "id") Integer id, HttpServletRequest request) {
        boolean error = false;
        String message = "";
        try {
            eventPublisher.publishEvent(new OnReviewApprovedEvent(ratingService.activeRating(id), request));
        } catch (RatingNotFoundException e) {
            error = true;
            message = e.getMessage();
        }

        return ResponseEntity.ok(new ResponseResult(error, message));
    }

}
