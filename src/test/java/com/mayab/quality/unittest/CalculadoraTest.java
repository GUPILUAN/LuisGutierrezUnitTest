package com.mayab.quality.unittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculadoraTest {
    private Calculadora calculadoraMock;
    public static final double RESULT = 0.1;

    @Test
    void test() {
        Calculadora calculadora = new Calculadora();
        int num1 = 2;
        int num2 = 3;
        int expRes = 5;
        double resultado = calculadora.suma(num1, num2);
        assertEquals(resultado, expRes);
    }

    @Test
    void test2() {
        Calculadora calculadora = new Calculadora();
        int num1 = 10;
        int num2 = 2;
        int expRes = 5;
        double resultado = calculadora.division(num1, num2);
        assertEquals(resultado, expRes);
    }

    @Test
    void test3() {
        double x = 0.2;
        double y = 0.7;
        double expRes = 0.1;
        calculadoraMock = mock(Calculadora.class);
        when(calculadoraMock.suma(x, y)).thenReturn(RESULT);
        double resultado = calculadoraMock.suma(x, y);
        assertEquals(resultado, expRes);
    }

}
