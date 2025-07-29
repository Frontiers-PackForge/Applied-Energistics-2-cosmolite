/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 TeamAppliedEnergistics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package appeng.api.stacks;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Iterators;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import appeng.api.config.FuzzyMode;

/**
 * Associates a generic value of type T with AE keys and makes key/value pairs searchable with fuzzy mode
 * semantics.
 */
public final class KeyCounter implements Iterable<Object2LongMap.Entry<AEKey>> {

    private static final Object object = new Object();
    private VariantCounter variantCounter;

    // First map contains a mapping from AEKey#primaryKey
    private Object2ObjectOpenHashMap<Object, VariantCounter> lists;

    public Collection<Object2LongMap.Entry<AEKey>> findFuzzy(AEKey key, FuzzyMode fuzzy) {
        if (this.lists == null)
            return Collections.emptyList();
        if (key.getFuzzySearchMaxValue() > 0) {
            VariantCounter counter = this.lists.get(key.getPrimaryKey());
            if (counter != null)
                return counter.findFuzzy(key, fuzzy);
        } else if (this.variantCounter != null) {
            long l = this.variantCounter.getOrMin(key);
            if (l > Long.MIN_VALUE)
                return Collections.singleton(new Counter(l, key));
        }
        return Collections.emptyList();
    }

    public void removeZeros() {
        if (this.lists == null)
            return;
        if (this.variantCounter != null && this.lists.size() == 1) {
            this.variantCounter.removeZeros();
            return;
        }
        var iterator = lists.object2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            VariantCounter variantCounter = entry.getValue();
            variantCounter.removeZeros();
            if (variantCounter != this.variantCounter && variantCounter.isEmpty())
                iterator.remove();
        }
    }

    public void removeEmptySubmaps() {
        if (this.lists == null)
            return;
        var iterator = lists.object2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            VariantCounter variantCounter = entry.getValue();
            if (variantCounter != this.variantCounter && variantCounter.isEmpty())
                iterator.remove();
        }
    }

    public void addAll(KeyCounter other) {
        if (this.lists == null)
            this.lists = new Object2ObjectOpenHashMap<>();
        var map = other.lists;
        for (var iterator = map.object2ObjectEntrySet().fastIterator(); iterator.hasNext();) {
            var entry = iterator.next();
            var obj = entry.getKey();
            var counter = lists.get(obj);
            if (counter == null) {
                var counter1 = entry.getValue().copy();
                if (obj == object)
                    this.variantCounter = counter1;
                this.lists.put(obj, counter1);
                continue;
            }
            counter.addAll(entry.getValue());
        }
    }

    public void removeAll(KeyCounter other) {
        if (this.lists == null)
            this.lists = new Object2ObjectOpenHashMap<>();
        var map = other.lists;
        for (var iterator = map.object2ObjectEntrySet().fastIterator(); iterator.hasNext();) {
            var entry = iterator.next();
            var obj = entry.getKey();
            var counter = lists.get(obj);
            if (counter == null) {
                var counter1 = entry.getValue().copy();
                if (obj == object)
                    this.variantCounter = counter1;
                this.lists.put(obj, counter1);
                continue;
            }
            counter.removeAll(entry.getValue());
        }
    }

    public void add(AEKey key, long amount) {
        Objects.requireNonNull(key, "key");
        getSubIndex(key).add(key, amount);
    }

    /**
     * Subtracts the given amount from the value associated with the given key.
     */
    public void remove(AEKey key, long amount) {
        add(key, -amount);
    }

    /**
     * Removes the given key from this counter, and returns the old value (or 0).
     */
    public long remove(AEKey key) {
        var counter = getSubIndexOrNull(key);
        if (counter == null)
            return 0L;
        long l = counter.remove(key);
        if (counter != this.variantCounter && counter.isEmpty())
            lists.remove(key.getPrimaryKey());
        return l;
    }

    public void set(AEKey key, long amount) {
        getSubIndex(key).set(key, amount);
    }

    public long get(AEKey key) {
        if (this.lists == null)
            return 0L;
        var counter = getSubIndexOrNull(key);
        if (counter == null)
            return 0L;
        return counter.get(key);
    }

    public void reset() {
        if (this.lists == null)
            return;
        if (this.variantCounter != null && this.lists.size() == 1) {
            this.variantCounter.reset();
            return;
        }
        for (var iterator = this.lists.object2ObjectEntrySet().fastIterator(); iterator.hasNext();)
            iterator.next().getValue().reset();
    }

    public void clear() {
        if (this.lists == null)
            return;
        if (this.variantCounter != null && this.lists.size() == 1) {
            this.variantCounter.clear();
            return;
        }
        for (var iterator = this.lists.object2ObjectEntrySet().fastIterator(); iterator.hasNext();)
            iterator.next().getValue().clear();
    }

    public boolean isEmpty() {
        if (this.lists == null)
            return true;
        if (this.variantCounter != null && this.lists.size() == 1)
            return this.variantCounter.isEmpty();
        for (var iterator = this.lists.object2ObjectEntrySet().fastIterator(); iterator.hasNext();)
            if (!iterator.next().getValue().isEmpty())
                return false;
        return true;
    }

    public int size() {
        if (this.lists == null)
            return 0;
        if (this.variantCounter != null && this.lists.size() == 1)
            return this.variantCounter.size();
        int i = 0;
        for (var iterator = this.lists.object2ObjectEntrySet().fastIterator(); iterator.hasNext();)
            i += iterator.next().getValue().size();
        return i;
    }

    @Override
    public Iterator<Object2LongMap.Entry<AEKey>> iterator() {
        if (this.lists == null)
            return Collections.emptyIterator();
        if (this.variantCounter != null && this.lists.size() == 1)
            return this.variantCounter.iterator();
        return Iterators.concat(Iterators.transform(this.lists.values().iterator(), Iterable::iterator));
    }

    private VariantCounter getSubIndex(AEKey key) {
        if (lists == null)
            this.lists = new Object2ObjectOpenHashMap<>();
        if (key.getFuzzySearchMaxValue() > 0)
            return lists.computeIfAbsent(key.getPrimaryKey(), k -> new VariantCounter.FuzzyVariantMap());
        if (this.variantCounter == null) {
            this.variantCounter = new VariantCounter.UnorderedVariantMap();
            this.lists.put(object, this.variantCounter);
        }
        return this.variantCounter;
    }

    @Nullable
    private VariantCounter getSubIndexOrNull(AEKey key) {
        if (lists == null)
            return null;
        if (key.getFuzzySearchMaxValue() > 0)
            return lists.get(key.getPrimaryKey());
        return this.variantCounter;
    }

    @Nullable
    public AEKey getFirstKey() {
        var e = getFirstEntry();
        return e != null ? e.getKey() : null;
    }

    @Nullable
    public <T extends AEKey> T getFirstKey(Class<T> keyClass) {
        var e = getFirstEntry(keyClass);
        return e != null ? keyClass.cast(e.getKey()) : null;
    }

    @Nullable
    public Object2LongMap.Entry<AEKey> getFirstEntry() {
        if (this.lists == null)
            return null;
        for (var iterator = this.lists.object2ObjectEntrySet().fastIterator(); iterator.hasNext();) {
            var it = iterator.next().getValue().iterator();
            if (it.hasNext())
                return it.next();
        }
        return null;
    }

    @Nullable
    public <T extends AEKey> Object2LongMap.Entry<AEKey> getFirstEntry(Class<T> keyClass) {
        if (this.lists == null)
            return null;
        for (var iterator = this.lists.object2ObjectEntrySet().fastIterator(); iterator.hasNext();) {
            var it = iterator.next().getValue().iterator();
            if (it.hasNext()) {
                var entry = it.next();
                if (keyClass.isInstance(entry.getKey()))
                    return entry;
            }
        }
        return null;
    }

    public Set<AEKey> keySet() {
        if (this.lists == null)
            return Collections.emptySet();
        if (this.variantCounter != null && this.lists.size() == 1)
            return this.variantCounter.keySet();
        var set = new HashSet<AEKey>(size());
        for (var iterator = this.lists.object2ObjectEntrySet().fastIterator(); iterator.hasNext();)
            for (var entry : iterator.next().getValue())
                set.add(entry.getKey());
        return set;
    }

    public static class Counter implements Object2LongMap.Entry<AEKey> {
        private final long l;
        private final AEKey key;

        public Counter(long param1Long, AEKey param1AEKey) {
            this.l = param1Long;
            this.key = param1AEKey;
        }

        public long getLongValue() {
            return this.l;
        }

        public long setValue(long param1Long) {
            return 0L;
        }

        @Override
        public AEKey getKey() {
            return this.key;
        }
    }
}
