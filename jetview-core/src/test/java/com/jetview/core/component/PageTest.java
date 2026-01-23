package com.jetview.core.component;

import com.jetview.core.annotation.RequestParam;
import com.jetview.core.app.JetViewContext;
import com.jetview.core.component.event.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PageTest {

    @Disabled
    @Test
    void testSerializationIntegrity() throws IOException, ClassNotFoundException {

        try (MockedStatic<JetViewContext> jetViewContextMockedStatic = Mockito.mockStatic(JetViewContext.class)) {

            var request = mock(HttpServletRequest.class);

            jetViewContextMockedStatic.when(JetViewContext::getRequest).thenReturn(request);

            // 1. Create an original object with test data
            var title = "Test Page";
            var counter = 1;
            TestPage original = new TestPage(title, counter);

            // 2. Serialize and deserialize to create a copy
            TestPage copy = cloneBySerialization(original);

            // 3. Assertions to verify correctness
            assertNotNull(copy, "The deserialized object should not be null");
            assertNotSame(original, copy, "The copy should be a different instance in memory");
            assertEquals(original, copy, "The deserialized copy should be equal to the original");

            // Verify that all non-transient fields are correctly serialized and restored
            assertEquals(title, original.getTitle(), "Title field mismatch");
            assertEquals(original.getTitle(), copy.getTitle(), "Title field mismatch");
            assertEquals(original.getComponent(), copy.getComponent(), "Component field mismatch");
            assertEquals(counter, original.getComponent().getCounter(), "Counter field mismatch");

            // Verify that transient fields are null/default after deserialization
            assertNull(copy.getSecretInfo(), "Transient field should be null after deserialization");
        }
    }

    /**
     * Helper method to serialize and deserialize an object.
     *
     * @param original The object to clone via serialization.
     * @return The deserialized copy of the object.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T cloneBySerialization(T original) throws IOException, ClassNotFoundException {

        // Serialize the object to a byte array in memory
        var baos = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();

        byte[] bytes = baos.toByteArray();

        // Deserialize the object from the byte array
        var bais = new ByteArrayInputStream(bytes);
        var ois = new ObjectInputStream(bais);
        var copy = ois.readObject();
        ois.close();

        return (T) copy;
    }

    private static class TestPage extends Page {

        private final String title;
        private final TestComponent component;

        private final transient String secretInfo;

        public TestPage(@RequestParam("title") String title,
                        @RequestParam(name = "counter") int counter) {
            this.title = title;
            this.component = new TestComponent(counter);
            this.secretInfo = "Secret info";
            setProperty("title", this::getTitle);
            setComponent("content", component);
        }

        public String getTitle() {
            return title;
        }

        public TestComponent getComponent() {
            return component;
        }

        public String getSecretInfo() {
            return secretInfo;
        }

        @Override
        public String getId() {
            return "test-page";
        }
    }

    private static class TestComponent extends Component {

        private int counter;

        public TestComponent(int counter) {
            this.counter = counter;
            setProperty("in", () -> this.counter);
            setProperty("notSerializable", Object::new);
            setListener(Event.ON_CLICK, event -> {
                ++this.counter;
                notifyStateChange();
            });
        }

        public int getCounter() {
            return counter;
        }

        @Override
        public String getId() {
            return "test-component";
        }
    }
}