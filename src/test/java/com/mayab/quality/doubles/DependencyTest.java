package com.mayab.quality.doubles;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DependencyTest {
    private Dependency dependency;
    private SubDependency subDependency;

    @BeforeEach
    public void setup() {
        subDependency = mock(SubDependency.class);
        dependency = new Dependency(subDependency);
        when(subDependency.getClassName()).thenReturn("SubDependency");
    }

    @Test
    public void getClassName() {
        String name = dependency.getSubDependencyClassName();
        assertThat(name, is("SubDependency"));
    }

    @Test
    public void testAnswer() {
        Dependency dependency = Mockito.mock(Dependency.class);

        when(dependency.addTwo(anyInt())).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                int arg = (Integer) invocation.getArguments()[0];
                return arg + 20;
            }
        });
        assertEquals(30, dependency.addTwo(10));
    }

}
