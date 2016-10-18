package seedu.taskmanager.model;

import javafx.collections.transformation.FilteredList;

import seedu.taskmanager.commons.core.ComponentManager;
import seedu.taskmanager.commons.core.LogsCenter;
import seedu.taskmanager.commons.core.UnmodifiableObservableList;
import seedu.taskmanager.commons.events.model.TaskManagerChangedEvent;
import seedu.taskmanager.commons.events.ui.ChangeDoneEvent;
import seedu.taskmanager.commons.events.ui.FilterEvent;
import seedu.taskmanager.commons.util.StringUtil;
import seedu.taskmanager.model.item.Item;
import seedu.taskmanager.model.item.ReadOnlyItem;
import seedu.taskmanager.model.item.UniqueItemList;
import seedu.taskmanager.model.item.UniqueItemList.ItemNotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.Stack;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskManager taskManager;
    private final FilteredList<Item> filteredItems;
    private Stack<HistoryTaskManager> history;
    private final String initialHistory = "";

    /**
     * Initializes a ModelManager with the given AddressBook
     * AddressBook and its variables should not be null
     */
    public ModelManager(TaskManager src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

        taskManager = new TaskManager(src);
        filteredItems = new FilteredList<>(taskManager.getItems());
        history = new Stack<HistoryTaskManager>();
        HistoryTaskManager newHistory = new HistoryTaskManager(src, initialHistory);
        history.push(newHistory);
    }

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        filteredItems = new FilteredList<>(taskManager.getItems());
        history = new Stack<HistoryTaskManager>();
        HistoryTaskManager newHistory = new HistoryTaskManager(initialData, initialHistory);
        history.push(newHistory);
    }

    @Override
    public void resetData(ReadOnlyTaskManager newData, String actionTaken) {
        taskManager.resetData(newData);
        indicateAddressBookChanged(actionTaken);
    }

    @Override
    public ReadOnlyTaskManager getAddressBook() {
        return taskManager;
    }
    
    @Override
    public synchronized void setDone(ReadOnlyItem target, String actionTaken) throws ItemNotFoundException {
        taskManager.setDone(target);
        raise(new ChangeDoneEvent());
        indicateAddressBookChanged(actionTaken);
    }
    
    @Override
    public synchronized void setUndone(ReadOnlyItem target, String actionTaken) throws ItemNotFoundException {
        taskManager.setUndone(target);
        raise(new ChangeDoneEvent());
        indicateAddressBookChanged(actionTaken);
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged(String actionTaken) {
        ReadOnlyTaskManager newData = new TaskManager(taskManager);
        HistoryTaskManager newHistory = new HistoryTaskManager(newData, actionTaken);
        history.push(newHistory);
        raise(new TaskManagerChangedEvent(taskManager));
        raise(new FilterEvent(filteredItems));
    }
    
    @Override
    public String undoAction() {
    	if (history.empty()) {
    	    return null;
    	} else {
            HistoryTaskManager currentData = history.pop();
            if (history.empty()) {
                history.push(currentData);
            	return null;
            } else {
            	HistoryTaskManager oldData = history.pop();
                taskManager.resetData(oldData.getPastTaskManager());
                indicateAddressBookChanged(oldData.getActionTaken());
                return currentData.getActionTaken();
            }
    	}
    }

    @Override
    public synchronized void deleteItem(ReadOnlyItem target, String actionTaken) throws ItemNotFoundException {
        taskManager.removeItem(target);
        indicateAddressBookChanged(actionTaken);
    }

    @Override
    public synchronized void addItem(Item item, String actionTaken) throws UniqueItemList.DuplicateItemException {
        taskManager.addItem(item);
        updateFilteredListToShowAll();
        indicateAddressBookChanged(actionTaken);
    }
    
    @Override
    public synchronized void replaceItem(ReadOnlyItem target, Item toReplace, String actionTaken) 
            throws ItemNotFoundException, UniqueItemList.DuplicateItemException {
        taskManager.replaceItem(target, toReplace);
        updateFilteredListToShowAll();
        indicateAddressBookChanged(actionTaken);
    }

    //=========== Filtered Item List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList() {
        return new UnmodifiableObservableList<>(filteredItems);
    }

    @Override
    public void updateFilteredListToShowAll() {
    	filteredItems.setPredicate(null);
    	raise(new FilterEvent(filteredItems));
    }
    
    
    @Override
    public void updateFilteredListToShowTask() {
        final String[] itemType = {"task"}; 
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(itemType));
        updateFilteredPersonList(new PredicateExpression(new ItemTypeQualifier(keywordSet)));
    }
    
    @Override
    public void updateFilteredListToShowDeadline() {
    	final String[] itemType = {"deadline"}; 
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(itemType));
        updateFilteredPersonList(new PredicateExpression(new ItemTypeQualifier(keywordSet)));
    }
    
    @Override
    public void updateFilteredListToShowEvent() {
    	final String[] itemType = {"event"}; 
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(itemType));
        updateFilteredPersonList(new PredicateExpression(new ItemTypeQualifier(keywordSet)));
    }

    @Override
    public void updateFilteredPersonList(Set<String> keywords){
        updateFilteredPersonList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredPersonList(Expression expression) {
        filteredItems.setPredicate(expression::satisfies);
        raise(new FilterEvent(filteredItems));
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyItem person);
        String toString();
    }
    
    private class HistoryTaskManager {
        
        private String actionTaken;
        private ReadOnlyTaskManager pastTaskManager;
        
        HistoryTaskManager(TaskManager pastTaskManager, String actionTaken) {
        	this.pastTaskManager = pastTaskManager;
            this.actionTaken = actionTaken;
		}

		HistoryTaskManager(ReadOnlyTaskManager pastTaskManager, String actionTaken) {
            this.pastTaskManager = pastTaskManager;
            this.actionTaken = actionTaken;
        }
        
        public String getActionTaken() {
            return actionTaken;
        }
        
        public ReadOnlyTaskManager getPastTaskManager() {
            return pastTaskManager;
        }
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyItem person) {
            return qualifier.run(person);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyItem person);
        String toString();
    }
    
    private class ItemTypeQualifier implements Qualifier {
        private Set<String> itemType;

        ItemTypeQualifier(Set<String> itemType) {
            this.itemType = itemType;
        }

        @Override
        public boolean run(ReadOnlyItem person) {
            return itemType.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(person.getItemType().value, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", itemType);
        }
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyItem person) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(person.getName().value, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
