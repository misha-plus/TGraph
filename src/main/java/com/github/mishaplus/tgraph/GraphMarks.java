package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.numbersets.strategies.TernaryLogic;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public class GraphMarks {
    final Map<MarkType, Object> marks = new HashMap<>();
    public void setMark(MarkType markType, Object value) {
        marks.put(markType, value);
    }

    @Override
    public String toString() {
        return marks.toString();
    }

    @Override
    public int hashCode() {
        return marks.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GraphMarks && marks.equals(((GraphMarks) obj).marks);
    }

    public boolean isTotallySynchronizable() {
        if (marks.containsKey(MarkType.isTotallySynchronizable)) {
            Object isTS = marks.get(MarkType.isTotallySynchronizable);
            Preconditions.checkState(isTS instanceof Boolean);
            //noinspection ConstantConditions
            return (Boolean) isTS;
        }
        throw new IllegalStateException();
    }

    public boolean isPartitionable() {
        Object stored = getObject(MarkType.isPartitionable);
        Preconditions.checkState(stored instanceof TernaryLogic);
        //noinspection ConstantConditions
        TernaryLogic result = (TernaryLogic) stored;
        if (result == TernaryLogic.Yes)
            return true;
        else if (result == TernaryLogic.No)
            return false;
        else
            throw new IllegalStateException("Maybe state");
    }

    public boolean getBoolean(MarkType markType) {
        Object stored = getObject(markType);
        Preconditions.checkState(stored instanceof Boolean);
        //noinspection ConstantConditions
        return (Boolean) stored;
    }

    public Object getObject(MarkType markType) {
        if (marks.containsKey(markType)) {
            Object stored = marks.get(markType);
            Preconditions.checkState(stored != null);
            //noinspection ConstantConditions
            return stored;
        }
        throw new IllegalStateException();
    }
}
