package pl.michol;

import pl.michol.annotations.Autowired;
import pl.michol.annotations.Bean;
import pl.michol.annotations.Runner;
import pl.michol.services.ServiceOne;
import pl.michol.services.ServiceTwo;

@Bean
@Runner
class ApplicationRunner {

    public ApplicationRunner() {
    }

    private ServiceOne serviceOne;
    private ServiceTwo serviceTwo;

    @Autowired
    public void setServiceTwo(ServiceTwo serviceTwo) {
        this.serviceTwo = serviceTwo;
    }

    @Autowired
    public void setServieOne(ServiceOne serviceOne){
        this.serviceOne = serviceOne;
    }

    public void run(){
        String result = serviceOne.methodOne();
        System.out.println("Result: " + result);
        result = serviceTwo.methodTwo();
        System.out.println("Result: " + result);
    }

}
