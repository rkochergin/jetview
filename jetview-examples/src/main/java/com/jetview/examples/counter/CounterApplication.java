package com.jetview.examples.counter;

import com.jetview.core.annotation.JetViewApplication;
import com.jetview.core.app.JetViewWebApplication;

/**
 * @author Roman Kochergin
 */
@JetViewApplication(name = "CounterApp", url = "/counter/*",
        pageScanPackages = "com.jetview.examples.counter")
public class CounterApplication extends JetViewWebApplication {
}
