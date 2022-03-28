package controller;

import command.ICommand;

public class Controller {
    private Context keptContext;
    public Controller(){
        Context newContext = new Context();
        this.keptContext = newContext;
    }
    public void runCommand(ICommand command){
        command.execute(keptContext);
    }
}
