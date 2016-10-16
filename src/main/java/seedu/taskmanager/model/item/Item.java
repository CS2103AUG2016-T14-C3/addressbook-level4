package seedu.taskmanager.model.item;

import seedu.taskmanager.commons.exceptions.IllegalValueException;
import seedu.taskmanager.commons.util.CollectionUtil;
import seedu.taskmanager.model.tag.UniqueTagList;

import java.util.Objects;

/**
 * Represents a Item in the task manager.
 * Guarantees: details are present and not null, field values are validated.
 * Date and Time can be empty strings.
 */
public class Item implements ReadOnlyItem {

    private ItemType itemType;
    private Name name;
    private ItemDate startDate;
    private ItemTime startTime;
    private ItemDate endDate;
    private ItemTime endTime;
    private boolean done;

    private UniqueTagList tags;

    /**
     * Convenience constructor for tasks. Calls primary constructor with empty fields for startDate, startTime, endDate, endTime
     *
     */
    public Item(ItemType itemType, Name name, UniqueTagList tags) {
        this(itemType, name, new ItemDate(), new ItemTime(), new ItemDate(), new ItemTime(), tags);
    }
    
    /**
     * Convenience constructor for deadlines. Calls primary constructor with empty fields for startDate and startTime
     *
     */
    public Item(ItemType itemType, Name name, ItemDate endDate, ItemTime endTime, UniqueTagList tags) {
        this(itemType, name, new ItemDate(), new ItemTime(), endDate, endTime, tags);
    }
    
    
    /**
     * Every field must be present and not null.
     */
    public Item(ItemType itemType, Name name, ItemDate startDate, ItemTime startTime, ItemDate endDate, ItemTime endTime, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(itemType, name, startDate, startTime, endDate, endTime, tags);
        this.itemType = itemType;
        this.name = name;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.done = false;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }
    
    /**
     * Every field must be present and not null.
     */
    public Item(ItemType itemType, Name name, ItemDate startDate, ItemTime startTime, ItemDate endDate, ItemTime endTime, boolean done, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(itemType, name, startDate, startTime, endDate, endTime, done, tags);
        this.itemType = itemType;
        this.name = name;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.done = done;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    /**
     * Copy constructor.
     */
    public Item(ReadOnlyItem source) {
        this(source.getItemType(), source.getName(), source.getStartDate(), source.getStartTime(), source.getEndDate(), source.getEndTime(), source.getDone(), source.getTags());
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
    
    @Override
    public ItemDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(ItemDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public ItemTime getStartTime() {
        return startTime;
    }    
    
    public void setStartTime(ItemTime startTime) {
        this.startTime = startTime;
    }
    
    @Override
    public ItemDate getEndDate() {
        return endDate;
    }

    public void setEndDate(ItemDate endDate) {
        this.endDate = endDate;
    }
    
    @Override
    public ItemTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(ItemTime endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public boolean getDone() {
        return done;
    }
    
    @Override
    public void setDone() {
        done = true;
    }
    
    @Override
    public void setUndone() {
        done = false;
    }
    
    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this item's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ReadOnlyItem // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyItem) other);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(itemType, name, startDate, startTime, endDate, endTime, done, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
