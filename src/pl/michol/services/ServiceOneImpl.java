package pl.michol.services;

import pl.michol.annotations.Bean;

@Bean
public class ServiceOneImpl implements ServiceOne {
    @Override
    public String methodOne() {
        return "method one";
    }
}
