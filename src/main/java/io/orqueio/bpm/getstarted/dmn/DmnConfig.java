package io.orqueio.bpm.getstarted.dmn;

import java.io.InputStream;

import io.orqueio.bpm.dmn.engine.DmnDecision;
import io.orqueio.bpm.dmn.engine.DmnEngine;
import io.orqueio.bpm.dmn.engine.DmnEngineConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DmnConfig {

    @Bean
    public DmnEngine dmnEngine() {
        return DmnEngineConfiguration
            .createDefaultDmnEngineConfiguration()
            .buildEngine();
    }

    @Bean
    public DmnDecision travelDecision(DmnEngine dmnEngine) {
        InputStream dmn = getClass().getResourceAsStream("/travelRecommendations.dmn");
        return dmnEngine.parseDecision("travel", dmn);
    }
}
