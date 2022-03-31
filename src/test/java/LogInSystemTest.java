import command.LoginCommand;
import command.LogoutCommand;
import command.RegisterConsumerCommand;
import command.RegisterEntertainmentProviderCommand;
import controller.Controller;
import logging.Logger;
import model.Consumer;
import model.EntertainmentProvider;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogInSystemTest {

    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    private static void registerEntertainmentProvider(Controller controller){
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk")
        ));
    }

    private static void register3Consumers(Controller controller) {
        controller.runCommand(new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand(
                "Jane Giantsdottir",
                "jane@inf.ed.ac.uk",
                "04462187232",
                "giantsRverycool",
                "jane@aol.com"
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand(
                "Wednesday Kebede",
                "i-will-kick-your@gmail.com",
                "-",
                "it is wednesday my dudes",
                "i-will-kick-your@gmail.com"
        ));
        controller.runCommand(new LogoutCommand());
    }

    private static void loginConsumer1(Controller controller) {
        controller.runCommand(new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2"));
    }

    private static void loginConsumer2(Controller controller) {
        controller.runCommand(new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool"));
    }

    private static void loginConsumer3(Controller controller) {
        controller.runCommand(new LoginCommand("i-will-kick-your@gmail.com", "it is wednesday my dudes"));
    }

    private static void loginEdinburgh(Controller controller) {
        controller.runCommand(new LoginCommand("pmathieson@ed.ac.uk", "hongkong"));
    }

    private static void registerAndLoginEdinburgh(Controller controller){
        registerEntertainmentProvider(controller);
        loginEdinburgh(controller);
    }

    @Test
    void registerAndLogInEdinburghUniversityAsEntertainmentProvider(){
        Controller controller = new Controller();
        registerEntertainmentProvider(controller);
        controller.runCommand(new LogoutCommand());
        LoginCommand cmd = new LoginCommand("pmathieson@ed.ac.uk", "hongkong");
        controller.runCommand(cmd);
        User loggerIn = (User) cmd.getResult();
        EntertainmentProvider edinburghUni = new EntertainmentProvider("University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));

        assertEquals(edinburghUni.getPaymentAccountEmail(), loggerIn.getPaymentAccountEmail());
        assertEquals(edinburghUni.getEmail(), loggerIn.getEmail());
        assertEquals(edinburghUni.getOrgName(), ((EntertainmentProvider)(loggerIn)).getOrgName());
        assertEquals(edinburghUni.getOrgAddress(), ((EntertainmentProvider)(loggerIn)).getOrgAddress());
    }
    @Test
    void register3UsersAndLogThemIn(){
        Controller controller = new Controller();
        register3Consumers(controller);
        LoginCommand cmd1 = new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2");
        controller.runCommand(cmd1);
        User loggerIn1 = (User) cmd1.getResult();
        Consumer JohnBiggson = new Consumer("John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk");
        assertEquals(JohnBiggson.getName(), ((Consumer)(loggerIn1)).getName());
        assertEquals(JohnBiggson.getEmail(), ((Consumer)(loggerIn1)).getEmail());
        assertEquals(JohnBiggson.getPaymentAccountEmail(), ((Consumer)(loggerIn1)).getPaymentAccountEmail());
        assertEquals(JohnBiggson.getPhoneNumber(), ((Consumer)(loggerIn1)).getPhoneNumber());
    }
}
