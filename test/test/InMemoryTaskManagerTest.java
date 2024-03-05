package test;

import managers.*;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}