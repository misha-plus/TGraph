package com.github.mishaplus.tgraph.automata;

import com.github.mishaplus.tgraph.automata.synchronization.SqAutomataState;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Chars;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by michael on 01.04.14
 */
public class AutomataImplTest {
    Automata<Integer, Character> automata;

    @Before
    public void setUp() throws Exception {
        automata = new AutomataImpl<>();
        automata.addState(5);
        automata.addState(15);
        automata.addState(7);
        automata.addTransition(5, 7, 'a');
        automata.addTransition(5, 15,'c');
        automata.addTransition(3, 4, 'b');
    }

    @Test
    public void testGetTransitions() {
        Map<Integer, Map<Character, Integer>> expected = ImmutableMap.of(
                5, ImmutableMap.of('a', 7, /**/ 'c', 15),
                3, ImmutableMap.of('b', 4),
                4, ImmutableMap.of(),
                7, ImmutableMap.of(),
                15, ImmutableMap.of()
        );
        Assert.assertEquals(expected, automata.getTransitions());
    }

    @Test
    public void testGetTransition() {
        Assert.assertEquals(15, (int) automata.makeTransition(5, 'c'));
    }

    @Test
    public void testEquals() {
        Automata<Integer, Character> automata2 = new AutomataImpl<>();
        automata2.addState(5);
        automata2.addState(15);
        automata2.addState(7);
        automata2.addTransition(5, 7, 'a');
        automata2.addTransition(5, 15,'c');
        automata2.addTransition(3, 4, 'b');

        Assert.assertEquals(automata, automata2);
    }

    @Test
    public void testAddState() throws Exception {
        automata.addState(10);
        Assert.assertEquals(
                ImmutableSet.of(3, 4, 5, 7, 10, 15),
                automata.getStates()
        );
    }

    @Test
    public void testAddTransition() throws Exception {
        automata.addTransition(15, 17, 'e');
        Assert.assertEquals(
                ImmutableSet.of(3, 4, 5, 7, 15, 17),
                automata.getStates()
        );
    }

    @Test
    public void testGetStates() throws Exception {
        Assert.assertEquals(
                ImmutableSet.of(3, 4, 5, 7, 15),
                automata.getStates()
        );
    }

    @Test
    public void testGetOutGoing() throws Exception {
        Assert.assertEquals(
                ImmutableMap.of('a', 7, /**/ 'c', 15),
                automata.getOutGoing(5)
        );
    }

    Automata<Integer, Character> syncAutomata;

    @Before
    public void setUpSync() {
        syncAutomata = new AutomataImpl<>();
        syncAutomata.addTransition(1, 2, 'a');
        syncAutomata.addTransition(1, 3, 'b');

        syncAutomata.addTransition(2, 1, 'a');
        syncAutomata.addTransition(2, 1, 'b');

        syncAutomata.addTransition(3, 2, 'a');
        syncAutomata.addTransition(3, 2, 'b');
    }

    @Test
    public void testIsSynchronizationWord() throws Exception {
        Assert.assertTrue(syncAutomata.isSynchronizationWord(Chars.asList("aba".toCharArray())));
        Assert.assertFalse(syncAutomata.isSynchronizationWord(Chars.asList("abbb".toCharArray())));
    }

    @Test
    public void testFindSynchronizationWord() throws Exception {
        List<Character> syncWord = syncAutomata.findSynchronizationWord();
        Assert.assertTrue(syncAutomata.isSynchronizationWord(syncWord));
    }

    @Test
    public void testNotFoundSynchronizationWord() throws Exception {
        Automata<Integer, Character> nonSyncAutomata = new AutomataImpl<>();
        nonSyncAutomata.addTransition(1, 2, 'a');
        nonSyncAutomata.addTransition(1, 3, 'b');

        nonSyncAutomata.addTransition(2, 1, 'a');
        nonSyncAutomata.addTransition(2, 2, 'b');

        nonSyncAutomata.addTransition(3, 1, 'b');
        nonSyncAutomata.addTransition(3, 3, 'a');

        List<Character> nonSyncWord = nonSyncAutomata.findSynchronizationWord();
        Assert.assertNull(nonSyncWord);
    }

    @Test
    public void testToPairAutomata() throws Exception {
        // {1, 2}, 'a' -> {2, 1} = {1, 2}
        // {1, 2}, 'b' -> {3, 1} = {1, 3}

        // {1, 3}, 'a' -> q0
        // {1, 3}, 'b' -> {3, 2} = {2, 3}

        // {2, 3}, 'a' -> {1, 2}
        // {2, 3}, 'b' -> {1, 2}

        Automata<SqAutomataState<Integer>, Character> expected = new AutomataImpl<>();

        SqAutomataState<Integer> q0 = SqAutomataState.createCollapsingState();
        BiFunction<Integer, Integer, SqAutomataState<Integer>> mul = SqAutomataState::new;

        expected.addTransition(q0, q0, 'a');
        expected.addTransition(q0, q0, 'b');

        expected.addTransition(mul.apply(1, 2), mul.apply(1, 2), 'a');
        expected.addTransition(mul.apply(1, 2), mul.apply(1, 3), 'b');

        expected.addTransition(mul.apply(1, 3), q0, 'a');
        expected.addTransition(mul.apply(1, 3), mul.apply(2, 3), 'b');

        expected.addTransition(mul.apply(2, 3), mul.apply(1, 2), 'a');
        expected.addTransition(mul.apply(2, 3), mul.apply(1, 2), 'b');

        Assert.assertEquals(expected, syncAutomata.toPairAutomata());
    }

    @Test
    public void testFindShortestPath() throws Exception {
        Automata<Integer, Character> automata = new AutomataImpl<>();
        automata.addTransition(1, 2, 'a');
        automata.addTransition(2, 3, 'b');

        automata.addTransition(1, 3, 'b');

        automata.addTransition(3, 1, 'a');

        automata.addTransition(3, 5, 'b');
        automata.addTransition(5, 6, 'a');

        automata.addTransition(3, 6, 'c');

        List<Character> expectedShortestWord = Arrays.asList('b', 'c');
        Assert.assertEquals(
                expectedShortestWord,
                automata.findShortestPath(ImmutableSet.of(1, 2), 6)
        );
    }

    Automata<Integer, Character> transitionChecksAutomata;

    @Before
    public void setUpTransitionChecksAutomata() {
        transitionChecksAutomata = new AutomataImpl<>();
        transitionChecksAutomata.addTransition(1, 3, 'a');
        transitionChecksAutomata.addTransition(2, 4, 'a');
        transitionChecksAutomata.addTransition(3, 7, 'b');
        transitionChecksAutomata.addTransition(4, 8, 'b');
        transitionChecksAutomata.addTransition(7, 9, 'a');
        transitionChecksAutomata.addTransition(8, 10,'a');
    }

    @Test
    public void testMakeStateCharTransition() throws Exception {
        Assert.assertEquals(
                7, (int) transitionChecksAutomata.makeTransition(3, 'b')
        );
    }

    @Test
    public void testMakeSetCharTransition() throws Exception {
        Assert.assertEquals(
                ImmutableSet.of(3, 4),
                transitionChecksAutomata.makeTransition(
                        ImmutableSet.of(1, 2), 'a'
                )
        );
    }

    @Test
    public void testMakeStateWordTransition() throws Exception {
        Assert.assertEquals(
                9,
                (int) transitionChecksAutomata.makeTransition(
                        1,
                        Arrays.asList('a', 'b', 'a')
                )
        );
    }

    @Test
    public void testMakeSetWordTransition() throws Exception {
        Assert.assertEquals(
                ImmutableSet.of(9, 10),
                transitionChecksAutomata.makeTransition(
                        ImmutableSet.of(1, 2),
                        Arrays.asList('a', 'b', 'a')
                )
        );
    }
}
