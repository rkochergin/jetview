package com.jetview.examples.helloworld;

import com.jetview.core.annotation.JetViewApplication;
import com.jetview.core.app.JetViewWebApplication;

/**
 * @author Roman Kochergin
 */
@JetViewApplication(name = "HelloWorldApp", url = "/hello-world/*",
        pageScanPackages = "com.jetview.examples.helloworld")
public class HelloWorldApplication extends JetViewWebApplication {
}
