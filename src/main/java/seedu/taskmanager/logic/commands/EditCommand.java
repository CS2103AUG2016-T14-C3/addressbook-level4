package seedu.taskmanager.logic.commands;

import java.util.logging.*;
import seedu.taskmanager.commons.core.LogsCenter;
import seedu.taskmanager.commons.core.Messages;
import seedu.taskmanager.commons.core.UnmodifiableObservableList;
import seedu.taskmanager.commons.exceptions.IllegalValueException;
import seedu.taskmanager.model.item.Item;
import seedu.taskmanager.model.item.ReadOnlyItem;
import seedu.taskmanager.model.item.UniqueItemList;
import seedu.taskmanager.model.item.UniqueItemList.ItemNotFoundException;

/**
 * Edits an item identified using it's last displayed index from the task manager.
 */
public class EditCommand extends Command {
    private static final Logger logger = LogsCenter.getLogger(EditCommand.class);
    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the item identified by the index number used in the last item listing.\n"
            + "Parameters: INDEX (must be a positive integer)" 
            + " n/[NAME]"
            + " sd/[START_DATE]"
            + " st/[START_TIME]"
            + " ed/[END_DATE]"
            + " et/[END_TIME]"
            + " (at least one field must be specifed)"
            + "\n" + "Example: " + COMMAND_WORD + " 1" + " n/buy milk";

    public static final String MESSAGE_EDIT_ITEM_SUCCESS = "Edited %1$s";
    
    public final int targetIndex;
    public final String newName;
    public final String newStartDate;
    public final String newStartTime;
    public final String newEndDate;
    public final String newEndTime;

    /*
     * Edits deadline, task, or event by index.
     *      
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(int targetIndex, String name, String startDate, String startTime, String endDate, String endTime)
            throws IllegalValueException {
        
        //At least one parameter is being edited
        assert (name != null || startDate != null || startTime!= null || endDate != null || endTime != null);
        
        this.targetIndex = targetIndex;
        this.newName = name;
        this.newStartDate = startDate;
        this.newStartTime = startTime;
        this.newEndDate = endDate;
        this.newEndTime = endTime;
        logger.info("EditCommand object successfully created!");
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        ReadOnlyItem itemToEdit = lastShownList.get(targetIndex - 1);
        Item itemToReplace = new Item(itemToEdit);
        try {
            if (newName != null) {
                itemToReplace.setName(newName);
            }
            if (newStartDate != null) {
                itemToReplace.setStartDate(newStartDate);
            }
            if (newStartTime != null) {
                itemToReplace.setStartTime(newStartTime);
            }
            if (newEndDate != null) {
                itemToReplace.setEndDate(newEndDate);
            }
            if (newEndTime != null) {
                itemToReplace.setEndTime(newEndTime);
            }
        } catch (IllegalValueException ive) {
            return new CommandResult(ive.getMessage());
        }
        
        try {
            model.replaceItem(itemToEdit, itemToReplace);
        } catch (ItemNotFoundException pnfe) {
            assert false : "The target item cannot be missing";
        } catch (UniqueItemList.DuplicateItemException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ITEM);
        }
        return new CommandResult(String.format(MESSAGE_EDIT_ITEM_SUCCESS, itemToReplace));
    }

}
