import command.LoginCommand;
import command.LogoutCommand;
import command.RegisterConsumerCommand;
import command.RegisterEntertainmentProviderCommand;
import controller.Controller;
import logging.LogEntry;
import logging.Logger;
import model.Consumer;
import model.EntertainmentProvider;
import model.GovernmentRepresentative;
import model.User;
import org.junit.jupiter.api.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
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

    // The following methods are used to set up the test environment - to test a login, sometimes it is //
    // useful to register a user first. //

    private static void registerEntertainmentProvider(Controller controller) {
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
        controller.runCommand(new LoginCommand("jbiggson1@hotmail.co.uk",
                "jbiggson2"));
    }

    private static void loginConsumer2(Controller controller) {
        controller.runCommand(new LoginCommand("jane@inf.ed.ac.uk",
                "giantsRverycool"));
    }

    private static void loginConsumer3(Controller controller) {
        controller.runCommand(new LoginCommand("i-will-kick-your@gmail.com", "it is wednesday my dudes"));
    }

    private static void loginEdinburgh(Controller controller) {
        controller.runCommand(new LoginCommand("pmathieson@ed.ac.uk",
                "hongkong"));
    }

    private static void registerAndLoginEdinburgh(Controller controller) {
        registerEntertainmentProvider(controller);
        loginEdinburgh(controller);
    }

    private static void loginGovernmentRepresentative(Controller controller) {
        controller.runCommand(new LoginCommand("margaret.thatcher@gov.uk", "The Good times  "));
    }


    // The following Tests are for each type of User: //
    // Government Representative, Consumer and Entertainment Provider //

    @Test
    @DisplayName("loginGovernmentRepresentativeTest")
    void logInGovernmentRepresentativeTest() {
        Controller controller = new Controller();
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        controller.runCommand(cmd);
        User loggerIn = (User) cmd.getResult();
        assertEquals("margaret.thatcher@gov.uk", loggerIn.getEmail());
    }

    @Test
    @DisplayName("loginConsumerTest")
    void logInConsumer() {
        Controller controller = new Controller();
        RegisterConsumerCommand regCmd = new RegisterConsumerCommand(
                "Tom",
                "tom@com",
                "075590",
                "pass",
                "tompay@com"
        );
        controller.runCommand(regCmd);
        controller.runCommand(new LogoutCommand());
        LoginCommand loginCmd = new LoginCommand("tom@com", "pass");
        controller.runCommand(loginCmd);
        User loggerIn = (User) loginCmd.getResult();
        assertEquals("tom@com", loggerIn.getEmail());
    }

    @Test
    @DisplayName("loginEntertainmentProviderTest")
    void logInEntertainmentProvider() {
        Controller controller = new Controller();
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        controller.runCommand(cmd);
        User loggerIn = (User) cmd.getResult();
        assertEquals("margaret.thatcher@gov.uk", loggerIn.getEmail());
    }

    // This test is described as detailed because it checks all attributes of the logged in user against the
    // equivalent entertainment provider. //
    @Test
    @DisplayName("DetailedLoginTest")
    void loginDetailedTest() {
        Controller controller = new Controller();
        registerEntertainmentProvider(controller);
        controller.runCommand(new LogoutCommand());
        LoginCommand cmd = new LoginCommand("pmathieson@ed.ac.uk", "hongkong");
        controller.runCommand(cmd);
        User loggerIn = (User) cmd.getResult();
        EntertainmentProvider edinburghUni = new
                EntertainmentProvider("University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        assertEquals(edinburghUni.getPaymentAccountEmail(),
                loggerIn.getPaymentAccountEmail());
        assertEquals(edinburghUni.getOrgAddress(), ((EntertainmentProvider) loggerIn).getOrgAddress());
        assertEquals(edinburghUni.getEmail(), ((EntertainmentProvider) loggerIn).getEmail());
        assertEquals(edinburghUni.getOrgName(), ((EntertainmentProvider) loggerIn).getOrgName());
    }

    // This test checks that null is returned when a user is logged out //
    @Test
    @DisplayName("LogoutTest")
    void logoutTest() {
        Controller controller = new Controller();
        registerEntertainmentProvider(controller);
        LogoutCommand cmd = new LogoutCommand();
        controller.runCommand(cmd);
        User loggedIn = (User) cmd.getResult();
        assertNull(loggedIn);
    }

    // The following tests test for incorrect inputs: null inputs, incorrect password and
    // unregistered email address //

    @Test
    @DisplayName("LoginWithNullInputsTest")
    void logInNullInputs() {
        Controller controller = new Controller();
        LoginCommand cmd1 = new LoginCommand(null, null);
        controller.runCommand(cmd1);
        User loggerIn1 = (User) cmd1.getResult();
        ;
        assertNull((((Consumer) (loggerIn1))));
        LoginCommand cmd2 = new LoginCommand("jane@inf.ed.ac.uk", null);
        controller.runCommand(cmd2);
        User loggerIn2 = (User) cmd2.getResult();
        ;
        assertNull((((Consumer) (loggerIn2))));
        LoginCommand cmd3 = new LoginCommand(null, "giantsRverycool");
        controller.runCommand(cmd3);
        User loggerIn3 = (User) cmd3.getResult();
        ;
        assertNull((((Consumer) (loggerIn3))));
    }

    @Test
    @DisplayName("LoginWithWrongPasswordTest")
    void registerAndLogInWithWrongPassword() {
        Controller controller = new Controller();
        controller.runCommand(new RegisterConsumerCommand(
                "Wednesday Kebede",
                "i-will-kick-your@gmail.com",
                "-",
                "it is wednesday my dudes",
                "i-will-kick-your@gmail.com"
        ));
        controller.runCommand(new LogoutCommand());
        LoginCommand cmd1 = new LoginCommand("i-will-kick-your@gmail.com", "this password is so wrong in crazy");
        controller.runCommand(cmd1);
        User loggerIn1 = (User) cmd1.getResult();
        ;
        assertNull((((Consumer) (loggerIn1))));
    }

    @Test
    @DisplayName("LoginWithUnregisteredEmailTest")
    void registerAndLogInWithWrongEmail(){
        Controller controller = new Controller();
        controller.runCommand(new RegisterConsumerCommand(
                "Wednesday Kebede",
                "i-will-kick-your@gmail.com",
                "-",
                "it is wednesday my dudes",
                "i-will-kick-your@gmail.com"
        ));
        controller.runCommand(new LogoutCommand());
        LoginCommand cmd1 = new LoginCommand("email@spam.com","it is wednesday my dudes");
        controller.runCommand(cmd1);
        User loggerIn1 = (User) cmd1.getResult();;
        assertNull((((Consumer)(loggerIn1))));
    }

    // The following tests are somewhat more complicated, building more realistic scenarios //

    @Test
    void register3UsersAndLogThemIn() {
        Controller controller = new Controller();
        register3Consumers(controller);
        LoginCommand cmd1 = new LoginCommand("jbiggson1@hotmail.co.uk",
                "jbiggson2");
        controller.runCommand(cmd1);
        User loggerIn1 = (User) cmd1.getResult();
        Consumer JohnBiggson = new Consumer("John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk");
        assertEquals(JohnBiggson.getName(), ((Consumer)
                (loggerIn1)).getName());
        assertEquals(JohnBiggson.getEmail(), ((Consumer)
                (loggerIn1)).getEmail());
        assertEquals(JohnBiggson.getPaymentAccountEmail(), ((Consumer)
                (loggerIn1)).getPaymentAccountEmail());
        assertEquals(JohnBiggson.getPhoneNumber(), ((Consumer)
                (loggerIn1)).getPhoneNumber());
        controller.runCommand(new LogoutCommand());
        LoginCommand cmd2 = new LoginCommand("jane@inf.ed.ac.uk",
                "giantsRverycool");
        controller.runCommand(cmd2);
        User loggerIn2 = (User) cmd2.getResult();
        Consumer JaneGiantsdottir = new Consumer("Jane Giantsdottir",
                "jane@inf.ed.ac.uk",
                "04462187232",
                "giantsRverycool",
                "jane@aol.com");
        assertEquals(JaneGiantsdottir.getName(), ((Consumer)
                (loggerIn2)).getName());
        assertEquals(JaneGiantsdottir.getEmail(), ((Consumer)
                (loggerIn2)).getEmail());
        assertEquals(JaneGiantsdottir.getPaymentAccountEmail(), ((Consumer)
                (loggerIn2)).getPaymentAccountEmail());
        assertEquals(JaneGiantsdottir.getPhoneNumber(), ((Consumer)
                (loggerIn2)).getPhoneNumber());
        controller.runCommand(new LogoutCommand());
        LoginCommand cmd3 = new LoginCommand("i-will-kick-your@gmail.com", "it is wednesday my dudes");
        controller.runCommand(cmd3);
        User loggerIn3 = (User) cmd3.getResult();
        Consumer WednesdayKebede = new Consumer("Wednesday Kebede",
                "i-will-kick-your@gmail.com",
                "-",
                "it is wednesday my dudes",
                "i-will-kick-your@gmail.com");
        assertEquals(WednesdayKebede.getName(), ((Consumer)
                (loggerIn3)).getName());
        assertEquals(WednesdayKebede.getEmail(), ((Consumer)
                (loggerIn3)).getEmail());
        assertEquals(WednesdayKebede.getPaymentAccountEmail(), ((Consumer)
                (loggerIn3)).getPaymentAccountEmail());
        assertEquals(WednesdayKebede.getPhoneNumber(), ((Consumer)
                (loggerIn3)).getPhoneNumber());
        controller.runCommand(new LogoutCommand());
    }

    @Test
    void logIn2GovernmentRepresentatives() {
        Controller controller = new Controller();
        LoginCommand cmd1 = new LoginCommand("gov1@gov.uk", "Gov123");
        controller.runCommand(cmd1);
        User loggerIn1 = (User) cmd1.getResult();
        GovernmentRepresentative govRep1 = new
                GovernmentRepresentative("gov1@gov.uk", "Gov123", "gov1pay@gov.uk");
        assertEquals(govRep1.getEmail(), loggerIn1.getEmail());
        assertEquals(govRep1.getPaymentAccountEmail(),
                ((GovernmentRepresentative) (loggerIn1)).getPaymentAccountEmail());
        controller.runCommand(new LogoutCommand());
        LoginCommand cmd2 = new LoginCommand("gov2@gov.uk", "Gov456");
        controller.runCommand(cmd2);
        User loggerIn2 = (User) cmd2.getResult();
        GovernmentRepresentative govRep2 = new
                GovernmentRepresentative("gov2@gov.uk", "Gov456", "gov2pay@gov.uk");
        assertEquals(govRep2.getEmail(), loggerIn2.getEmail());
        assertEquals(govRep2.getPaymentAccountEmail(),
                ((GovernmentRepresentative) (loggerIn2)).getPaymentAccountEmail());
        controller.runCommand(new LogoutCommand());
    }
}