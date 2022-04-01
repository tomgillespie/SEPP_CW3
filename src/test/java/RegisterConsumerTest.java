import command.LogoutCommand;
import command.RegisterConsumerCommand;
import controller.Controller;
import logging.Logger;
import model.Consumer;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import static org.junit.jupiter.api.Assertions.*;
public class RegisterConsumerTest {
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
    void registerConsumer(){
        Controller controller = new Controller();
        RegisterConsumerCommand cmd1 = new
                RegisterConsumerCommand("John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk");
        controller.runCommand(cmd1);
        User register1 = (User) cmd1.getResult();
        Consumer johnBiggson = new Consumer("John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk");
        assertEquals(johnBiggson.getName(), ((Consumer)
                (register1)).getName());
        assertEquals(johnBiggson.getEmail(), ((register1)).getEmail());
        assertEquals(johnBiggson.getPhoneNumber(), ((Consumer)
                (register1)).getPhoneNumber());
        assertEquals(johnBiggson.getPaymentAccountEmail(),
                ((register1)).getPaymentAccountEmail());
        controller.runCommand(new LogoutCommand());
    }
    @Test
    void registerConsumerNullInputs(){
        Controller controller = new Controller();
        RegisterConsumerCommand cmd1 = new
                RegisterConsumerCommand(null,
                null,
                null,
                null,
                null);
        controller.runCommand(cmd1);
        User register1 = (User) cmd1.getResult();
//should i be using the logstatus here or not?
        assertNull((((Consumer)(register1))));
        RegisterConsumerCommand cmd2 = new
                RegisterConsumerCommand("John Biggson",
                null,
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk");
        controller.runCommand(cmd2);
        User register2 = (User) cmd2.getResult();
//should i be using the logstatus here or not?
        assertNull((((Consumer)(register2))));
    }
    @Test
    void emailAlreadyExists(){
        Controller controller = new Controller();
        RegisterConsumerCommand cmd1 = new
                RegisterConsumerCommand("John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk");
        controller.runCommand(cmd1);
        RegisterConsumerCommand cmd2 = new
                RegisterConsumerCommand("John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk");
        controller.runCommand(cmd2);
        User register2 = (User) cmd2.getResult();
//should i be using the logstatus here or not?
        assertNull((((Consumer)(register2))));
    }
}