package io.orqueio.bpm.getstarted.dmn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Map<String, Object>> recommendTravel(
            @RequestParam String season,
            @RequestParam String budget,
            @RequestParam int nbTravelers,
            @RequestParam boolean withChildren) {

        VariableMap variables = Variables.createVariables()
            .putValue("season", season)
            .putValue("budget", budget)
            .putValue("nbTravelers", nbTravelers)
            .putValue("withChildren", withChildren);

        long start = System.currentTimeMillis();

        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(travelDecision, variables);

        String destination = result.getFirstResult().getEntry("destination");
        List<Object> activities = result.collectEntries("activity");

        long elapsed = System.currentTimeMillis() - start;

        LOGGER.log(Level.INFO, "DMN evaluation time: {0}ms", elapsed);

        Map<String, Object> response = new HashMap<>();
        response.put("destination", destination);
        response.put("activities", activities);
        response.put("evaluationTime", elapsed + " ms");

        return ResponseEntity.ok(response);
    }
}
