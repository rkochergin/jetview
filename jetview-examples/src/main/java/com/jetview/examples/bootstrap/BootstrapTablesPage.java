package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
@Path("/tables")
@View("templates/bootstrap/BootstrapTablesPage.peb")
public class BootstrapTablesPage extends Page {
    public BootstrapTablesPage() {
        setComponent("Table", new BsTable());
    }
}
