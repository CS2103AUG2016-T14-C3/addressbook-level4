package seedu.taskmanager.storage;

import com.google.common.eventbus.Subscribe;

import seedu.taskmanager.commons.core.ComponentManager;
import seedu.taskmanager.commons.core.LogsCenter;
import seedu.taskmanager.commons.events.model.TaskManagerChangedEvent;
import seedu.taskmanager.commons.events.storage.DataSavingExceptionEvent;
import seedu.taskmanager.commons.events.storage.SaveLocationChangedEvent;
import seedu.taskmanager.commons.exceptions.DataConversionException;
import seedu.taskmanager.model.ReadOnlyTaskManager;
import seedu.taskmanager.model.TaskManager;
import seedu.taskmanager.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of TaskManager data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private XmlTaskManagerStorage taskManagerStorage;
    private JsonUserPrefStorage userPrefStorage;


    public StorageManager(String taskManagerFilePath, String userPrefsFilePath) {
        super();
        this.taskManagerStorage = new XmlTaskManagerStorage(taskManagerFilePath);
        this.userPrefStorage = new JsonUserPrefStorage(userPrefsFilePath);
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefStorage.saveUserPrefs(userPrefs);
    }


    // ================ TaskManager methods ==============================

    @Override
    public String getTaskManagerFilePath() {
        return taskManagerStorage.getTaskManagerFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, FileNotFoundException {
        logger.fine("Attempting to read data from file: " + taskManagerStorage.getTaskManagerFilePath());

        return taskManagerStorage.readTaskManager(taskManagerStorage.getTaskManagerFilePath());
    }
    
    //@@author A0143641M
    public Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskManagerStorage.readTaskManager(filePath);
    }
    //@@author

    @Override
    public void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException {
        taskManagerStorage.saveTaskManager(taskManager, taskManagerStorage.getTaskManagerFilePath());
    }


    @Override
    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    
    //@@author A0143641M
    @Subscribe
    public void handleStoragePathChangedEvent(SaveLocationChangedEvent spce) {
        logger.info(LogsCenter.getEventHandlingLogMessage(spce, "Storage file path changed"));
        try {
            // copying previous data from old save file to new save file
            ReadOnlyTaskManager oldTaskManager = readTaskManager(spce.oldLocation).orElse(new TaskManager());
            ((XmlTaskManagerStorage) taskManagerStorage).setTaskManagerFilePath(spce.newLocation);
            saveTaskManager(oldTaskManager);
        } catch (IOException  | DataConversionException e) {
            raise(new DataSavingExceptionEvent(e));
        } 
    }

}
