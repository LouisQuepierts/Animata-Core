package net.quepierts.animata.core.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public final class OrdinaryKey {
    private final String key;
    private final int ordinal;

    public static final class Manager {
        private final Map<String, OrdinaryKey> str2key = new HashMap<>();
        private final List<OrdinaryKey> ordinal2key = new ArrayList<>();
        private boolean frozen = false;

        public OrdinaryKey create(String name) {
            if (frozen) {
                throw new RuntimeException("This manager is already frozen");
            }
            int ordinal = str2key.size();
            OrdinaryKey key = new OrdinaryKey(name, ordinal);
            ordinal2key.add(key);
            str2key.put(name, key);
            return key;
        }

        public OrdinaryKey get(String name) {
            return str2key.get(name);
        }

        public OrdinaryKey get(int ordinal) {
            return ordinal2key.get(ordinal);
        }

        public int size() {
            return ordinal2key.size();
        }

        public void freeze() {
            if (frozen) {
                throw new RuntimeException("This manager is already frozen");
            }
            frozen = true;
        }
    }
}
