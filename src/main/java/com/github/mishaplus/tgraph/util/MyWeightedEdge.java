package com.github.mishaplus.tgraph.util;

import com.google.common.base.Objects;
import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge {
    public final int from;
    public final int to;
    public final int weight;

    public MyWeightedEdge(int from, int to, int weight) {
        super();
        this.from  = from;
        this.to    = to;
        this.weight = weight;
    }


    @Override
    public int hashCode() {
        //Object s = getSource();
        //Object t = getTarget();
        //return 1;//Objects.hashCode(s, t);
        return Objects.hashCode(from, to, weight);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyWeightedEdge))
            return false;
        MyWeightedEdge other = (MyWeightedEdge) obj;
        //return true;//    this.getSource().equals(other.getSource())
        //        //&& this.getTarget().equals(other.getTarget());
        return from == other.from && to == other.to && weight == other.weight;
    }

    /*
    @Override
    public Object getSource() {
        return from;
    }

    @Override
    public Object getTarget() {
        return to;
    }*/


    @Override
    public String toString() {
        return String.format("(%s-[%s]>%s)", from, weight, to);
    }
}
