package com.jetview.examples.elements;

import com.jetview.core.annotation.JetViewApplication;
import com.jetview.core.app.JetViewWebApplication;

/**
 * @author Roman Kochergin
 */
@JetViewApplication(name = "ElementsApp", url = "/elements/*",
        pageScanPackages = "com.jetview.examples.elements")
public class ElementsApplication extends JetViewWebApplication {
}
