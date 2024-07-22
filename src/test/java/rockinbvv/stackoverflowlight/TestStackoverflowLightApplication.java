package rockinbvv.stackoverflowlight;

import org.springframework.boot.SpringApplication;

public class TestStackoverflowLightApplication {

    public static void main(String[] args) {
        SpringApplication.from(StackoverflowLightApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
