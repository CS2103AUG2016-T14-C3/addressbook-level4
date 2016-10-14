package seedu.taskmanager.logic.commands;

import seedu.taskmanager.commons.exceptions.IllegalValueException;
import seedu.taskmanager.model.item.Date;
import seedu.taskmanager.model.item.Item;
import seedu.taskmanager.model.item.ItemType;
import seedu.taskmanager.model.item.Name;
import seedu.taskmanager.model.item.Time;
import seedu.taskmanager.model.item.UniqueItemList;
import seedu.taskmanager.model.tag.Tag;
import seedu.taskmanager.model.tag.UniqueTagList;

import java.util.HashSet;
import java.util.Set;

/**
 * Adds an item to the task manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    
    public static final String EVENT_MESSAGE_USAGE = "Event start datetime must come before end datetime";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task, deadline or event to the task manager. \n"
            + "Task Parameters: " + ItemType.TASK_WORD + " n/NAME \n"
            + "Deadline Parameters: " + ItemType.DEADLINE_WORD + " n/NAME ed/DATE et/TIME \n"
            + "Event Parameters: " + ItemType.EVENT_WORD + " n/NAME sd/DATE st/TIME ed/DATE et/TIME \n"
            + "Example (Task): " + COMMAND_WORD +  " " + ItemType.TASK_WORD 
            + " n/Win Facebook hackathon" + "\n"
            + "Example (Deadline): " + COMMAND_WORD +  " " + ItemType.DEADLINE_WORD 
            + " n/Cheat death ed/2000-12-13 et/12:34" + "\n"
            + "Example (Event): " + COMMAND_WORD +  " " + ItemType.EVENT_WORD
            + " n/Win at Life sd/1900-01-01 st/00:07 ed/2300-01-01 et/12:34";

    public static final String MESSAGE_SUCCESS = "Added %1$s";

    private final Item toAdd;

    /**
     * Convenience constructor for tasks. Calls primary constructor with empty fields for startDate, startTime, endDate, endTime
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String itemType, String name, Set<String> tags)
            throws IllegalValueException {
    	this(itemType, name, Date.EMPTY_DATE, Time.EMPTY_TIME, Date.EMPTY_DATE, Time.EMPTY_TIME, tags);
    }
    
    /**
     * Convenience constructor for deadlines. Calls primary constructor with empty fields for startDate and startTime
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String itemType, String name, String endDate, String endTime, Set<String> tags)
            throws IllegalValueException {
        this(itemType, name, Date.EMPTY_DATE, Time.EMPTY_TIME, endDate, endTime, tags);
    }
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String itemType, String name, String startDate, String startTime, String endDate, String endTime, Set<String> tags)
            throws IllegalValueException {
        assert itemType != null;
        assert name != null;
        assert startDate != null;
        assert startTime != null;
        assert endDate != null;
        assert endTime != null;
        assert tags != null;
        
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toAdd = new Item(
                new ItemType(itemType),
                new Name(name),
                new Date(startDate),
                new Time(startTime),
                new Date(endDate),
                new Time(endTime),
                new UniqueTagList(tagSet)
        );
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addItem(toAdd, String.format(MESSAGE_SUCCESS, toAdd));
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueItemList.DuplicateItemException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ITEM);
        }

    }

}
