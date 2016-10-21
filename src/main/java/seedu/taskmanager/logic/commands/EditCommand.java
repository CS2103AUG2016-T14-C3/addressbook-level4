package seedu.taskmanager.logic.commands;

import static seedu.taskmanager.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import java.util.logging.*;
import seedu.taskmanager.commons.core.LogsCenter;
import seedu.taskmanager.commons.core.Messages;
import seedu.taskmanager.commons.core.UnmodifiableObservableList;
import seedu.taskmanager.commons.exceptions.IllegalValueException;
import seedu.taskmanager.model.item.Item;
import seedu.taskmanager.model.item.ItemDate;
import seedu.taskmanager.model.item.ItemTime;
import seedu.taskmanager.model.item.ItemType;
import seedu.taskmanager.model.item.Name;
import seedu.taskmanager.model.item.ReadOnlyItem;
import seedu.taskmanager.model.item.UniqueItemList;
import seedu.taskmanager.model.item.UniqueItemList.ItemNotFoundException;
import seedu.taskmanager.model.tag.Tag;
import seedu.taskmanager.model.tag.UniqueTagList;
import seedu.taskmanager.model.tag.UniqueTagList.DuplicateTagException;

/**
 * Edits an item identified using it's last displayed index from the task manager.
 */
public class EditCommand extends Command {
    private static final Logger logger = LogsCenter.getLogger(EditCommand.class);
    public static final String COMMAND_WORD = "edit";
    public static final String SHORT_COMMAND_WORD = "e";

    public static final String MESSAGE_USAGE = COMMAND_WORD
                                               + ": Edits the item identified by the index number used in the last item listing."
                                               + "\n" + "Parameters: INDEX (positive integer)" 
                                               + " n/[NAME]"
                                               + " sdt/[START_DATE_TIME]"
                                               + " sd/[START_DATE]"
                                               + " st/[START_TIME]"
                                               + "\n" + "                     " + "edt/[END_DATE_TIME]"
                                               + " ed/[END_DATE]"
                                               + " et/[END_TIME]"
                                               + "\n" + "Editable parameters are restricted to those the item was created with."
                                               + "\n" + "At least one parameter must be specifed (sdt/edt favoured over sd/st/ed/et)"
                                               + "\n" + "Example: " + COMMAND_WORD + " 1" + " n/buy milk";
    
    public static final String MESSAGE_EDIT_ITEM_SUCCESS = "Edited %1$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "Tag [%1$s] does not exist! Tags must exist in order to be deletable";
    
    int targetIndex;
    Name name;
    ItemDate startDate;
    ItemTime startTime;
    ItemDate endDate;
    ItemTime endTime;
    UniqueTagList tagsToAdd;
    UniqueTagList tagsToRemove;

    /*
     * Edits deadline, task, or event by index.
     *      
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(int targetIndex, String name, String startDate, String startTime, 
                       String endDate, String endTime, List<String> tagsToAdd, List<String> tagsToRemove)
            throws IllegalValueException {
        
        //At least one parameter is being edited
        assert (name != null || startDate != null || startTime!= null || endDate != null 
                || endTime != null || tagsToAdd != null || tagsToRemove != null);
        
        this.targetIndex = targetIndex;
        if (name != null) {
            this.name = new Name(name);
        }
        if (startDate != null) {
            this.startDate = new ItemDate(startDate);
        }
        if (startTime != null) {
            this.startTime = new ItemTime(startTime);
        }
        if (endDate != null) {
            this.endDate = new ItemDate(endDate);
        }
        if (endTime != null) {
            this.endTime = new ItemTime(endTime);
        }
        
        if (tagsToAdd != null) {
            this.tagsToAdd = new UniqueTagList();
            for (String tag : tagsToAdd) {
                try {
                    this.tagsToAdd.add(new Tag(tag));
                } catch (DuplicateTagException dte) {
                }
            }
        }
        
        if (tagsToRemove!= null) {
            this.tagsToRemove = new UniqueTagList();
            for (String tag : tagsToRemove) {
                this.tagsToRemove.add(new Tag(tag));
            }
        }
        
        logger.fine("EditCommand object successfully created!");
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
        
        if (isInvalidInputForItemType(itemToReplace)) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
        
        if (this.name != null) {
            itemToReplace.setName(this.name);
        }
        if (this.startDate != null) {
            itemToReplace.setStartDate(this.startDate);
        }
        if (this.startTime != null) {
            itemToReplace.setStartTime(this.startTime);
        }
        if (this.endDate != null) {
            itemToReplace.setEndDate(this.endDate);
        }
        if (this.endTime != null) {
            itemToReplace.setEndTime(this.endTime);
        }
        
        if (this.tagsToAdd != null) {
            UniqueTagList tagListToEdit = itemToEdit.getTags();
            tagListToEdit.mergeFrom(this.tagsToAdd);
            itemToReplace.setTags(tagListToEdit);
        }
        
        if (tagsToRemove != null) {
            UniqueTagList tagListToEdit = itemToEdit.getTags();

            if (this.tagsToAdd != null) {
                tagListToEdit = itemToReplace.getTags();
            }
            
            UniqueTagList updatedTagList = new UniqueTagList();
            
            for (Tag tag : tagsToRemove) {
                if (!tagListToEdit.contains(tag)) {
                    indicateAttemptToExecuteIncorrectCommand();
                    return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
                                             String.format(MESSAGE_TAG_NOT_FOUND, tag.toString())));
                }
            }
            
            for (Tag tag : tagListToEdit) {
                if (!tagsToRemove.contains(tag)) {
                    try {
                        updatedTagList.add(tag);
                    } catch (DuplicateTagException dte) {
                    }
                }
            }
            itemToReplace.setTags(updatedTagList);
        }
        
        try {
            model.replaceItem(itemToEdit, itemToReplace, String.format(MESSAGE_EDIT_ITEM_SUCCESS, itemToReplace));
        } catch (ItemNotFoundException pnfe) {
            assert false : "The target item cannot be missing";
        } catch (UniqueItemList.DuplicateItemException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ITEM);
        }
        return new CommandResult(String.format(MESSAGE_EDIT_ITEM_SUCCESS, itemToReplace));
    }

    /**
     * @param itemToReplace
     * @return true if parameters input by user is not valid for the item that is being edited
     */
    private boolean isInvalidInputForItemType(ReadOnlyItem itemToReplace) { 
        String itemType = itemToReplace.getItemType().toString();
        assert itemType.equals(ItemType.TASK_WORD) 
               || itemType.equals(ItemType.DEADLINE_WORD) 
               || itemType.equals(ItemType.EVENT_WORD);
        
        if (itemType.equals(ItemType.TASK_WORD)) {
            return startDate != null || startTime != null || endDate != null || endTime != null;
        }
        if (itemType.equals(ItemType.DEADLINE_WORD)) {
            return startDate != null || startTime != null;
        }
        if (itemType.equals(ItemType.EVENT_WORD)) {
            return false;
        }
        
        return true;
    }

}
