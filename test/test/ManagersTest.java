package test;

import managers.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void testTaskManagerShouldNotNullAfterInit() {
        InMemoryTaskManager testTaskManager = Managers.getDefault();
        assertNotNull(testTaskManager, "TaskManager after init is null");
    }

    @Test
    public void testHistoryManagerShouldNotNullAfterInit() {
        HistoryManager testHistoryManager = Managers.getDefaultHistory();
        assertNotNull(testHistoryManager, "HistoryManager after init is null");
    }


}