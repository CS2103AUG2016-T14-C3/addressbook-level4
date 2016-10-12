package seedu.taskmanager.logic.commands;

/**
 * Lists all events in the task manager to the user.
 */

public class ListDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "listdeadline";

    public static final String MESSAGE_SUCCESS = "Listed all deadlines";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all deadlines.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD; 

    public ListDeadlineCommand() {}
   
    @Override
    public CommandResult execute() {
        model.updateFilteredListToShowAll();
        model.updateFilteredListToShowDeadline();

        return new CommandResult(MESSAGE_SUCCESS);
    }
}

