package pl.kacper.zielinski.infra;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Job {

    @Scheduled(fixedDelay = 1000)
    public void moveFile() {
        // todo here some logic
    }

}
