package com.github.mishaplus.tgraph.equivalence;

import com.github.mishaplus.tgraph.automata.Automata;
import com.github.mishaplus.tgraph.automata.AutomataImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 06.04.14
 */
public class AutomataCharactersIsomorphismTest {
    @Test
    public void testIsEquivalent() throws Exception {
        Automata<Integer, Character> a = new AutomataImpl<>();
        a.addTransition(1, 2, 'a');
        a.addTransition(1, 3, 'b');

        a.addTransition(2, 2, 'b');
        a.addTransition(2, 3, 'a');

        a.addTransition(3, 1, 'b');
        a.addTransition(3, 2, 'a');

        Automata<Integer, Character> b = new AutomataImpl<>();
        b.addTransition(1, 2, 'b');
        b.addTransition(1, 3, 'a');

        b.addTransition(2, 2, 'a');
        b.addTransition(2, 3, 'b');

        b.addTransition(3, 1, 'a');
        b.addTransition(3, 2, 'b');

        Assert.assertTrue(new AutomataCharactersIsomorphism<Integer, Character>().isEquivalent(a, b));
    }

    @Test
    public void testIsNotEquivalent() throws Exception {
        Automata<Integer, Character> a = new AutomataImpl<>();
        a.addTransition(1, 2, 'a');
        a.addTransition(1, 3, 'b');

        a.addTransition(2, 2, 'b');
        a.addTransition(2, 3, 'a');

        a.addTransition(3, 1, 'b');
        a.addTransition(3, 2, 'a');

        Automata<Integer, Character> b = new AutomataImpl<>();
        b.addTransition(1, 2, 'b');
        b.addTransition(1, 3, 'a');

        b.addTransition(2, 2, 'a');
        b.addTransition(2, 3, 'b');

        b.addTransition(3, 1, 'b');
        b.addTransition(3, 2, 'a');

        Assert.assertFalse(new AutomataCharactersIsomorphism<Integer, Character>().isEquivalent(a, b));
    }
}
