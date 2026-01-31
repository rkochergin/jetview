package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@View("templates/bootstrap/Table.peb")
public class BsTable extends Component {
    public BsTable() {
        setListener("fetch", event -> {
            Integer offset = event.getParam("offset", Integer.class);
            Integer limit = event.getParam("limit", Integer.class);
            List<Item> items = getItems(offset, limit);
            notifyStateChange(Map.of(
                    "property", "rows",
                    "size", items.size(),
                    "markup", renderRows(items)
                    ));
        });
    }

    private String renderRows(List<Item> items) {
        return items.stream()
                .map( item -> "<tr><td>%s</td><td>%s</td></tr>".formatted(item.id(), item.value()))
                .collect(Collectors.joining());
    }

    private List<Item> getItems(int offset, int limit) {
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        int max = Math.min(offset + limit, 123);
        return IntStream.range(offset, max)
                .mapToObj(i -> new Item(i, UUID.randomUUID().toString()))
                .toList();
    }

    private record Item(int id, String value) {}
}
