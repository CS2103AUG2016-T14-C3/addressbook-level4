package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.taskmanager.commons.core.Messages;

public class ListdeadlineCommandTest extends TaskManagerGuiTest {
	
	
	@Test
	public void ListDeadlineCommand() {
		commandBox.runCommand("listdeadline");
		assertListSize(3);
		
	}
	
	@Test
    public void find_invalidCommand3_fail() {
        commandBox.runCommand("listdeadlines");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }
	
	
	
	private void assertListdeadlineCommandSuccess() {
        commandBox.runCommand("listdeadline");
        assertResultMessage("Listed all deadline");
    }

}
