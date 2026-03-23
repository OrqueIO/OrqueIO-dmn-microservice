package io.orqueio.bpm.getstarted.dmn;

import java.io.IOException;

import io.orqueio.bpm.dmn.engine.DmnDecision;
import io.orqueio.bpm.dmn.engine.DmnEngine;
import io.orqueio.bpm.dmn.engine.DmnEngineConfiguration;
import io.orqueio.bpm.engine.variable.Variables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DmnConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DmnConfig.class);

    @Bean
    public DmnEngine dmnEngine() {
        return DmnEngineConfiguration
            .createDefaultDmnEngineConfiguration()
            .buildEngine();
    }

    @Bean
    public DmnDecision travelDecision(DmnEngine dmnEngine) {
        try (var inputStream = getClass().getResourceAsStream("/travelRecommendations.dmn")) {
            if (inputStream == null) {
                throw new IllegalStateException("DMN file not found: /travelRecommendations.dmn");
            }
            return dmnEngine.parseDecision("travel", inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load DMN file", e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse DMN decision 'travel'", e);
        }
    }

    @Bean
    public ApplicationRunner warmUp(DmnEngine dmnEngine, DmnDecision travelDecision) {
        return args -> {
            var variables = Variables.createVariables()
                .putValue("season", "Summer")
                .putValue("budget", "Medium")
                .putValue("numberOfTravelers", 2)
                .putValue("withChildren", false);

            var result = dmnEngine.evaluateDecisionTable(travelDecision, variables);

            LOGGER.info("DMN engine warm-up completed");
        };
    }
}