package training.configclientdemo;

//demo.prefix néven fogja majd keresni
@ConfigurationProperties(prefix = "demo")
public record DemoProperties(String prefix) {

}