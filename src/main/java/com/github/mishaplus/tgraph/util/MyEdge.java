package com.github.mishaplus.tgraph.util;

import com.google.common.base.Objects;
import org.jgrapht.graph.DefaultEdge;

public class MyEdge /*extends DefaultEdge*/ {
    public final int from;
    public final int to;
    public final int power;

    public MyEdge(int from, int to, int power) {
        super();
        this.from  = from;
        this.to    = to;
        this.power = power;
    }


    @Override
    public int hashCode() {
        //Object s = getSource();
        //Object t = getTarget();
        //return 1;//Objects.hashCode(s, t);
        return Objects.hashCode(from, to, power);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyEdge))
            return false;
        MyEdge other = (MyEdge) obj;
        //return true;//    this.getSource().equals(other.getSource())
        //        //&& this.getTarget().equals(other.getTarget());
        return from == other.from && to == other.to && power == other.power;
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
        return String.format("(%s->%s^%s)", from, to, power);
    }
}
