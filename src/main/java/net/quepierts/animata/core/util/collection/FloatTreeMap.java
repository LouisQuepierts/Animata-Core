package net.quepierts.animata.core.util.collection;

import it.unimi.dsi.fastutil.floats.*;

public class FloatTreeMap<T> implements IFloatTreeMap<T> {
    private final Float2ObjectAVLTreeMap<T> treeMap;
    private final FloatList keyList;

    public FloatTreeMap() {
        this.treeMap = new Float2ObjectAVLTreeMap<>();
        this.keyList = new FloatArrayList();
    }

    @Override
    public int getLowerIndex(float pKey) {
        FloatSortedSet headSet = treeMap.keySet().headSet(pKey);
        return headSet.isEmpty() ? -1 : keyList.indexOf(headSet.lastFloat());
    }

    @Override
    public int getUpperIndex(float pKey) {
        FloatSortedSet tailSet = treeMap.keySet().tailSet(pKey);
        return tailSet.isEmpty() ? -1 : keyList.indexOf(tailSet.firstFloat());
    }

    @Override
    public T getLowerEntry(float pKey) {
        FloatSortedSet headSet = treeMap.keySet().headSet(pKey);
        return headSet.isEmpty() ? null : treeMap.get(headSet.lastFloat());
    }

    @Override
    public T getUpperEntry(float pKey) {
        FloatSortedSet tailSet = treeMap.keySet().tailSet(pKey);
        return tailSet.isEmpty() ? null : treeMap.get(tailSet.firstFloat());
    }

    @Override
    public T get(int pIndex) {
        if (pIndex < 0 || pIndex >= keyList.size()) {
            throw new IndexOutOfBoundsException("Index: " + pIndex + ", Size: " + keyList.size());
        }
        return treeMap.get(keyList.getFloat(pIndex));
    }

    @Override
    public T first() {
        return treeMap.isEmpty() ? null : treeMap.get(treeMap.firstFloatKey());
    }

    @Override
    public T last() {
        return treeMap.isEmpty() ? null : treeMap.get(treeMap.lastFloatKey());
    }

    @Override
    public void insert(float pKey, T pValue) {
        if (!treeMap.containsKey(pKey)) {
            // 维护同步的键列表（插入后保持有序）
            int insertPos = findInsertPosition(pKey);
            keyList.add(insertPos, pKey);
        }
        treeMap.put(pKey, pValue);
    }

    @Override
    public void removeSpecific(float pKey) {
        if (treeMap.containsKey(pKey)) {
            // 从主映射中移除
            treeMap.remove(pKey);

            // 同步更新keyList
            int removePos = keyList.indexOf(pKey);
            keyList.remove(removePos);
        }
    }

    @Override
    public void removeRange(float pMin, float pMax) {
        // 获取范围内的键集合
        Float2ObjectSortedMap<T> subMap = treeMap.subMap(pMin, pMax);

        // 从主映射中移除
        treeMap.keySet().removeAll(subMap.keySet());

        // 同步更新keyList
        FloatList toRemove = new FloatArrayList();
        for (int i = 0; i < keyList.size(); i++) {
            float key = keyList.getFloat(i);
            if (key >= pMin && key < pMax) {
                toRemove.add(key);
            }
        }
        keyList.removeAll(toRemove);
    }

    // 二分查找确定插入位置（保持有序）
    private int findInsertPosition(float pKey) {
        int low = 0;
        int high = keyList.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            float midVal = keyList.getFloat(mid);

            if (midVal < pKey) {
                low = mid + 1;
            } else if (midVal > pKey) {
                high = mid - 1;
            } else {
                return mid; // 键已存在（实际不会发生，因外层检查）
            }
        }
        return low;
    }

    // 辅助方法
    public int size() {
        return treeMap.size();
    }

    public boolean containsKey(float pKey) {
        return treeMap.containsKey(pKey);
    }
}