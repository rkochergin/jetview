package com.jetview.examples.todo;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;

/**
 * @author Roman Kochergin
 */
@View("templates/todo/Counter.peb")
public class Counter extends Component {

    private int completedCount;
    private int totalCount;

    public Counter(int completedCount, int totalCount) {
        this.completedCount = completedCount;
        this.totalCount = totalCount;
        addValue("completedCount", () -> this.completedCount);
        addValue("totalCount", () -> this.totalCount);
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
        notifyStateChange();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        notifyStateChange();
    }
}
