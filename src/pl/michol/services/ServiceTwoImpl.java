package pl.michol.services;

import pl.michol.annotations.Bean;

@Bean
public class ServiceTwoImpl implements ServiceTwo {
    @Override
    public String methodTwo() {
        return "method two";
    }
}
