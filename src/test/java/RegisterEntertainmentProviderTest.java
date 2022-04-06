import command.LogoutCommand;
import command.RegisterConsumerCommand;
import command.RegisterEntertainmentProviderCommand;
import controller.Controller;
import logging.Logger;
import model.Consumer;
import model.EntertainmentProvider;
import model.User;
import org.junit.jupiter.api.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class RegisterEntertainmentProviderTest {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }
    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    // The following tests test several scenarios: //
    // When inputs are correct, when null inputs are provided, when the email address is already registered //
    // when the orgName and orgAddress are already registered, //
    // when the same orgName is provided, but a different orgAddress is given - this should succeed //

    @Test
    @DisplayName("RegisterProviderTest")
    void registerProviderTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand cmd1 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome",
                "bowling@ed.ac.uk",
                "barry bowling",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        controller.runCommand(cmd1);
        User register1 = (User) cmd1.getResult();
        EntertainmentProvider bowling = new EntertainmentProvider(
                "bowling club",
                "the bowladrome",
                "bowling@ed.ac.uk",
                "barry bowling",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        assertEquals(bowling.getOrgName(),((EntertainmentProvider)
                (register1)).getOrgName());
        assertEquals(bowling.getOrgAddress(),((EntertainmentProvider)
                (register1)).getOrgAddress());
        assertEquals(bowling.getPaymentAccountEmail(),
                ((EntertainmentProvider)(register1)).getPaymentAccountEmail());
        controller.runCommand(new LogoutCommand());

    }
    @Test
    void nullInputs(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand cmd1 = new
                RegisterEntertainmentProviderCommand(
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(),
                List.of());
        controller.runCommand(cmd1);
        User register1 = (User) cmd1.getResult();
        assertNull((((EntertainmentProvider)(register1))));
    }

    @Test
    void sameEmail(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand cmd1 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome",
                "bowling@ed.ac.uk",
                "barry bowling",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        controller.runCommand(cmd1);
        RegisterEntertainmentProviderCommand cmd2 = new
                RegisterEntertainmentProviderCommand(
                "better bowling club",
                "the bowladrome 2.0",
                "bowling@ed.ac.uk",
                "jeremeny clarkston ",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        controller.runCommand(cmd2);
        User register2 = (User) cmd2.getResult();
        assertNull((EntertainmentProvider)(register2));
    }

    @Test
    void sameOrgNameAndOrgAddress() {
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand cmd1 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome",
                "bowling@ed.ac.uk",
                "barry bowling",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        controller.runCommand(cmd1);
        RegisterEntertainmentProviderCommand cmd2 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome",
                "bowling2000@ed.ac.uk",
                "different name ",
                "differentemail@ed.ac.uk",
                "10 pin king",
                List.of("new_strike", "spare"),
                List.of("spare_strike@ed.ac.uk"));
        controller.runCommand(cmd2);
        User register2 = (User) cmd2.getResult();
        assertNull((EntertainmentProvider)(register2));
    }

    @Test
    void sameOrgNameDiffOrgAddress() {
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand cmd1 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome",
                "bowling@ed.ac.uk",
                "barry bowling",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        controller.runCommand(cmd1);
        RegisterEntertainmentProviderCommand cmd2 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome2",
                "bowling2000@ed.ac.uk",
                "different name ",
                "differentemail@ed.ac.uk",
                "10 pin king",
                List.of("new_strike", "spare"),
                List.of("spare_strike@ed.ac.uk"));
        controller.runCommand(cmd2);
        User register2 = (User) cmd2.getResult();
        assertEquals("differentemail@ed.ac.uk",register2.getEmail());
    }
}