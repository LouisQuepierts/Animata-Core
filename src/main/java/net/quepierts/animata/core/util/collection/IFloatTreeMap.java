package net.quepierts.animata.core.util.collection;

public interface IFloatTreeMap<T> {
    int getLowerIndex(float pKey);

    int getUpperIndex(float pKey);

    T getLowerEntry(float pKey);

    T getUpperEntry(float pKey);

    T first();

    T last();

    T get(int pIndex);

    float getKey(int pIndex);

    void insert(float pKey, T pValue);

    void removeSpecific(float pKey);

    void removeRange(float pMin, float pMax);
}
