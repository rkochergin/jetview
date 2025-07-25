package com.jetview.examples.todo;

import com.jetview.core.annotation.JetViewApplication;
import com.jetview.core.app.JetViewWebApplication;
import com.jetview.examples.todo.service.TodoService;

/**
 * @author Roman Kochergin
 */
@JetViewApplication(name = "ToDoApp", url = "/todo/*",
        pageScanPackages = "com.jetview.examples.todo")
public class TodoApplication extends JetViewWebApplication {
    @Override
    protected void init() {
        getApplicationContext().getResourceFactory()
                .registerResource(TodoService.class, new TodoService());
    }
}
