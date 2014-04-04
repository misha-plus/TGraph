package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.IntegerMatrix;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by michael on 04.04.14
 */
public class GaussEigenvectorFinderImplTest {
    @Test
    public void testFindPositiveEigenvector() throws Exception {
        IntegerMatrix matrix = new IntegerMatrix(new int[][]{
                {1, 2, 0},
                {0, 1, 2},
                {1, 1, 1}
        });

        List<Integer> friedmanEigenvector = new GaussEigenvectorFinderImpl()
                .findPositiveEigenvector(matrix, 3);

        List<Integer> expected = Arrays.asList(1, 2, 2);

        Assert.assertEquals(expected, friedmanEigenvector);
    }
}
