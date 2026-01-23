package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.JetViewApplication;
import com.jetview.core.app.JetViewWebApplication;

/**
 * @author Roman Kochergin
 */
@JetViewApplication(name = "BootstrapApp", url = "/bootstrap/*",
        pageScanPackages = "com.jetview.examples.bootstrap")
public class BootstrapApplication extends JetViewWebApplication {
}
