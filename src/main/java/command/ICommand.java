package command;

public interface ICommand {
    void execute(controller.Context context);

    Object getResult();

}
