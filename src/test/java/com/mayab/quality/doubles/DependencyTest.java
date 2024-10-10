package com.mayab.quality.doubles;

import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DependencyTest {
    private Dependency dependency;
    private SubDependency subDependency;

    @BeforeEach
    public void setup() {
        subDependency = mock(SubDependency.class);
        dependency = new Dependency(subDependency);
    }

    @Test
    public void getClassName() {
        String name = dependency.getSubDependencyClassName();
        assertThat(name, is(null));
    }

}
