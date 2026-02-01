package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Composite;
import com.jetview.core.component.Container;
import com.jetview.core.component.Page;

import java.util.List;
import java.util.Random;

/**
 * @author Roman Kochergin
 */
@Path("/progress")
@View("templates/bootstrap/BootstrapProgressPage.peb")
public class BootstrapProgressPage extends Page {

    public BootstrapProgressPage() {
        setComponent("Rows", new Composite<>(List.of(
                new Row(), new Row(), new Row(), new Row(), new Row()
        )));
    }

    @View("templates/bootstrap/ProgressRow.peb")
    private static class Row extends Container {

        private final BsProgress progress;
        private final BsButton button;

        public Row() {
            progress = new BsProgress();
            button = new BsButton("Run task");
            button.setClickHandler(_ -> {
                int min = 0;
                int max = 900;
                progress.setMin(min);
                progress.setMax(max);
                progress.setValue(min);
                button.setEnabled(false);
                runTask(min, max, new Random().nextInt(50));
            });
            progress.setCompleteHandler(_ -> button.setEnabled(true));
            setComponent("Button", button);
            setComponent("Progress", progress);
        }

        private void runTask(int min, int max, int sleep) {
            new Thread(() -> {
                for (int i = min; i <= max; i++) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    progress.setValue(i);
                }
            }).start();
        }
    }
}
