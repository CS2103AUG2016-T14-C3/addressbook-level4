package seedu.taskmanager.commons.events.ui;

import seedu.taskmanager.commons.events.BaseEvent;
import seedu.taskmanager.model.item.ReadOnlyItem;

/**
 * Represents a selection change in the Item List Panel
 */
public class PersonPanelSelectionChangedEvent extends BaseEvent {


    private final ReadOnlyItem newSelection;

    public PersonPanelSelectionChangedEvent(ReadOnlyItem newSelection){
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ReadOnlyItem getNewSelection() {
        return newSelection;
    }
}
