package io.orqueio.bpm.getstarted.dmn;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.orqueio.bpm.dmn.engine.DmnDecision;
import io.orqueio.bpm.dmn.engine.DmnDecisionTableResult;
import io.orqueio.bpm.dmn.engine.DmnEngine;
import io.orqueio.bpm.engine.variable.VariableMap;
import io.orqueio.bpm.engine.variable.Variables;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StopWatch;

@RestController
public class TravelController {

    private static final Logger LOGGER = Logger.getLogger(TravelController.class.getName());

    private final DmnEngine dmnEngine;
    private final DmnDecision travelDecision;

    public TravelController(DmnEngine dmnEngine, DmnDecision travelDecision) {
        this.dmnEngine = dmnEngine;
        this.travelDecision = travelDecision;
    }

    @GetMapping("/travel")
    public ResponseEntity<TravelRecommendation> recommendTravel(
            @RequestParam String season,
            @RequestParam String budget,
            @RequestParam int nbTravelers,
            @RequestParam boolean withChildren) {

        VariableMap variables = Variables.createVariables()
            .putValue("season", season)
            .putValue("budget", budget)
            .putValue("nbTravelers", nbTravelers)
            .putValue("withChildren", withChildren);

        StopWatch watch = new StopWatch();
        watch.start();

        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(travelDecision, variables);

        watch.stop();

        var elapsed = watch.getTotalTimeMillis();

        LOGGER.log(Level.INFO, "DMN evaluation time: {0} ms", elapsed);

        return ResponseEntity.ok(new TravelRecommendation(
            result.getFirstResult().<String>getEntry("destination"),
            result.<String>collectEntries("activity"),
            elapsed + " ms"
        ));
    }
}