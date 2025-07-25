package com.jetview.examples.todo;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.AjaxBehavior;

import java.io.Serializable;

/**
 * @author Roman Kochergin
 */
@View("templates/todo/AddForm.peb")
public class AddForm extends Component {

    public AddForm(NewItemHandler handler) {
        addBehavior(new AjaxBehavior("onSubmit", event -> {
            var title = event.getParam("title", String.class);
            handler.onNewItem(title);
        }));
    }

    @FunctionalInterface
    public interface NewItemHandler extends Serializable {
        void onNewItem(String title);
    }
}
