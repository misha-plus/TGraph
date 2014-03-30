package com.github.mishaplus.tgraph.equivalence;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class SplitToRepresentersTest {
    @Test
    public void testSplit() throws Exception {
        Set<String> input = ImmutableSet.of(
                "aFirst", "bFirst",
                "aSecond", "bSecond", "cSecond",
                "aThird",
                "cFourth", "dFourth"
        );

        Set<Set<String>> expected = ImmutableSet.of(
                ImmutableSet.of("aFirst", "aSecond", "aThird"),
                ImmutableSet.of("bFirst", "bSecond"),
                ImmutableSet.of("cSecond", "cFourth"),
                ImmutableSet.of("dFourth")
        );

        Set<String> actual = new SplitToRepresenters<String>(
                (a, b) -> a.charAt(0) == b.charAt(0)
        ).split(input);

        Assert.assertEquals(expected.size(), actual.size());
        for (String representer : actual)
            Assert.assertTrue(input.contains(representer));

        Set<Character> firstCharsOfActual = Sets.newHashSet(
                actual.stream().<Character>map(s -> s.charAt(0)).iterator()
        );

        Set<Character> firstCharsOfExpected = ImmutableSortedSet.of('a', 'b', 'c', 'd');
        Assert.assertEquals(firstCharsOfExpected, firstCharsOfActual);
    }
}
