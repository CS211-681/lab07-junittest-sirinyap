package ku.cs.services;
                          // T: Generic Type, K, V, E
public interface Datasource<T> {
    T readData();
    void writeData(T data);
}
