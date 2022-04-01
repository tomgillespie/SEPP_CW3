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
    @Test
    void registerProvider(){
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
//how to test main rep stuff
    }
    @Test
    void nullInputs(){
    }
    @Test
    void sameEmail(){
    }
    @Test
    void sameOrgNameAndOrgAddress() {
    }
}