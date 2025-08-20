package ku.cs.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class FXComponent {

    public interface ComponentController {
        void receiveData(Object data);

        default Object provideData() {
            return null;
        }
    }

    private static final Map<String, String> componentPaths = new HashMap<>();
    private static final Map<String, Object> componentData = new ConcurrentHashMap<>();
    private static final Map<Node, String> nodeToDataKey = new ConcurrentHashMap<>();
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);

    private FXComponent() {}

    // ======================== REGISTRATION ========================

    public static void register(String componentName, String componentPath) {
        if (!componentPaths.containsKey(componentName) || !componentPaths.get(componentName).equals(componentPath)) {
            componentPaths.put(componentName, componentPath);
        }
    }

    // ======================== LOADING METHODS (รองรับ Parent) ========================

    public static void loadTo(Parent container, String componentName) throws IOException {
        loadTo(container, componentName, null);
    }

    public static void loadTo(Parent container, String componentName, Object data) throws IOException {
        validateComponentRegistration(componentName);
        String componentPath = componentPaths.get(componentName);

        // ล้าง container และ data เดิม
        clearContainerData(container);

        // โหลด component ใหม่
        Node componentNode = loadComponentAsNode(componentPath, data);
        String dataKey = generateUniqueDataKey(componentName);

        // เก็บข้อมูลและ mapping
        if (data != null) {
            componentData.put(dataKey, data);
            nodeToDataKey.put(componentNode, dataKey);
        }

        // แทนที่ใน container ตามประเภท
        setContentToContainer(container, componentNode);
    }

    public static void addTo(Parent container, String componentName) throws IOException {
        addTo(container, componentName, null);
    }

    public static void addTo(Parent container, String componentName, Object data) throws IOException {
        validateComponentRegistration(componentName);
        String componentPath = componentPaths.get(componentName);

        // โหลด component ใหม่
        Node componentNode = loadComponentAsNode(componentPath, data);
        String dataKey = generateUniqueDataKey(componentName);

        // เก็บข้อมูลและ mapping
        if (data != null) {
            componentData.put(dataKey, data);
            nodeToDataKey.put(componentNode, dataKey);
        }

        // เพิ่มใน container ตามประเภท
        addContentToContainer(container, componentNode);
    }

    // ======================== CONTAINER-SPECIFIC METHODS ========================

    /**
     * แทนที่เนื้อหาใน container
     */
    private static void setContentToContainer(Parent container, Node content) {
        if (container instanceof Pane) {
            Pane pane = (Pane) container;
            pane.getChildren().clear();
            pane.getChildren().add(content);

        } else if (container instanceof ScrollPane) {
            ScrollPane scrollPane = (ScrollPane) container;
            scrollPane.setContent(content);

        } else if (container instanceof SplitPane) {
            SplitPane splitPane = (SplitPane) container;
            splitPane.getItems().clear();
            splitPane.getItems().add(content);

        } else if (container instanceof TabPane) {
            TabPane tabPane = (TabPane) container;
            Tab newTab = new Tab("Component", content);
            tabPane.getTabs().clear();
            tabPane.getTabs().add(newTab);

        } else {
            throw new UnsupportedOperationException(
                    "Container type '" + container.getClass().getSimpleName() +
                            "' is not supported for setContent operation"
            );
        }
    }

    /**
     * เพิ่มเนื้อหาใน container
     */
    private static void addContentToContainer(Parent container, Node content) {
        if (container instanceof Pane) {
            Pane pane = (Pane) container;
            pane.getChildren().add(content);

        } else if (container instanceof ScrollPane) {
            // ScrollPane ไม่สามารถ add ได้ เฉพาะ set เท่านั้น
            throw new UnsupportedOperationException(
                    "ScrollPane does not support addTo operation. Use loadTo instead."
            );

        } else if (container instanceof SplitPane) {
            SplitPane splitPane = (SplitPane) container;
            splitPane.getItems().add(content);

        } else if (container instanceof TabPane) {
            TabPane tabPane = (TabPane) container;
            Tab newTab = new Tab("Component " + (tabPane.getTabs().size() + 1), content);
            tabPane.getTabs().add(newTab);

        } else {
            throw new UnsupportedOperationException(
                    "Container type '" + container.getClass().getSimpleName() +
                            "' is not supported for addTo operation"
            );
        }
    }

    // ======================== DATA RETRIEVAL ========================

    public static Object getData(Node componentNode) {
        String dataKey = nodeToDataKey.get(componentNode);
        return dataKey != null ? componentData.get(dataKey) : null;
    }

    public static Object getData(Parent container, String componentName) {
        for (Node child : getContainerChildren(container)) {
            String dataKey = nodeToDataKey.get(child);
            if (dataKey != null && dataKey.contains(":" + componentName + ":")) {
                return componentData.get(dataKey);
            }
        }
        return null;
    }

    public static Object[] getAllData(Parent container, String componentName) {
        return getContainerChildren(container).stream()
                .map(nodeToDataKey::get)
                .filter(dataKey -> dataKey != null && dataKey.contains(":" + componentName + ":"))
                .map(componentData::get)
                .filter(data -> data != null)
                .toArray();
    }

    public static Object getData(String componentName) {
        return componentData.entrySet().stream()
                .filter(entry -> entry.getKey().contains(":" + componentName + ":"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public static Object[] getAllData(String componentName) {
        return componentData.entrySet().stream()
                .filter(entry -> entry.getKey().contains(":" + componentName + ":"))
                .map(Map.Entry::getValue)
                .toArray();
    }

    public static void updateData(Node componentNode, Object newData) {
        String dataKey = nodeToDataKey.get(componentNode);
        if (dataKey != null) {
            if (newData != null) {
                componentData.put(dataKey, newData);
            } else {
                componentData.remove(dataKey);
                nodeToDataKey.remove(componentNode);
            }
        }
    }

    public static void removeData(Node componentNode) {
        String dataKey = nodeToDataKey.remove(componentNode);
        if (dataKey != null) {
            componentData.remove(dataKey);
        }
    }

    public static void clearContainerData(Parent container) {
        getContainerChildren(container).forEach(child -> {
            String dataKey = nodeToDataKey.remove(child);
            if (dataKey != null) {
                componentData.remove(dataKey);
            }
        });
    }

    // ======================== HELPER METHODS ========================

    /**
     * รับ children จาก container ตามประเภท
     */
    private static java.util.List<Node> getContainerChildren(Parent container) {
        if (container instanceof Pane) {
            return ((Pane) container).getChildrenUnmodifiable();

        } else if (container instanceof ScrollPane) {
            ScrollPane scrollPane = (ScrollPane) container;
            Node content = scrollPane.getContent();
            return content != null ? java.util.List.of(content) : java.util.List.of();

        } else if (container instanceof SplitPane) {
            return ((SplitPane) container).getItems();

        } else if (container instanceof TabPane) {
            return ((TabPane) container).getTabs().stream()
                    .map(Tab::getContent)
                    .filter(content -> content != null)
                    .collect(java.util.stream.Collectors.toList());

        } else {
            return java.util.List.of();
        }
    }

    private static String generateUniqueDataKey(String componentName) {
        int instanceId = instanceCounter.incrementAndGet();
        long timestamp = System.nanoTime();
        return "instance:" + componentName + ":" + instanceId + ":" + timestamp;
    }

    private static void validateComponentRegistration(String componentName) {
        if (!componentPaths.containsKey(componentName)) {
            throw new IllegalArgumentException(
                    "Component '" + componentName + "' is not registered. " +
                            "Available components: " + String.join(", ", componentPaths.keySet())
            );
        }
    }

    private static Node loadComponentAsNode(String componentPath, Object data) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(FXComponent.class.getResource("/" + componentPath));

            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML resource: " + componentPath);
            }

            Node componentNode = loader.load();

            if (data != null) {
                Object controller = loader.getController();
                if (controller instanceof ComponentController) {
                    ((ComponentController) controller).receiveData(data);
                } else if (controller != null) {
                    try {
                        Method receiveDataMethod = controller.getClass().getMethod("receiveData", Object.class);
                        receiveDataMethod.invoke(controller, data);
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException(
                                "Controller '" + controller.getClass().getSimpleName() +
                                        "' must implement FXComponentLoader.ComponentController interface " +
                                        "or have receiveData(Object data) method to receive data."
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to send data to controller: " + e.getMessage(), e);
                    }
                }
            }

            return componentNode;

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to load component from path: " + componentPath, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error loading component: " + componentPath, e);
        }
    }

    // ======================== UTILITY METHODS (เหมือนเดิม) ========================

    public static String[] getRegisteredComponents() {
        return componentPaths.keySet().toArray(new String[0]);
    }

    public static boolean isRegistered(String componentName) {
        return componentPaths.containsKey(componentName);
    }

    public static String getComponentPath(String componentName) {
        return componentPaths.get(componentName);
    }

    public static int getRegisteredCount() {
        return componentPaths.size();
    }

    public static int getDataCount() {
        return componentData.size();
    }

    public static boolean unregister(String componentName) {
        return componentPaths.remove(componentName) != null;
    }

    public static void clearRegistry() {
        componentPaths.clear();
    }

    public static void clearAllData() {
        componentData.clear();
        nodeToDataKey.clear();
    }

    public static void clearAll() {
        componentPaths.clear();
        componentData.clear();
        nodeToDataKey.clear();
        instanceCounter.set(0);
    }

    public static void printRegisteredComponents() {
        System.out.println("Registered Components (" + componentPaths.size() + "):");
        componentPaths.forEach((name, path) ->
                System.out.println("  " + name + " -> " + path));
    }

    public static void printStoredData() {
        System.out.println("Stored Component Data (" + componentData.size() + "):");
        componentData.forEach((key, data) ->
                System.out.println("  " + key + " -> " + data.getClass().getSimpleName()));
    }
}