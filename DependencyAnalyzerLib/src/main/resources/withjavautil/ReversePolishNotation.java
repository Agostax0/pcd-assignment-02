package org.javaparser.samples;

import java.util.Stack;
import java.util.stream.Stream;
import java.util.List;
import java.util.Objects;
//import javax.swing.AbstractAction;
//import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
//import com.github.javaparser.javadoc.description.JavadocDescription;
//import com.github.javaparser.javadoc.description.JavadocSnippet;
//import com.github.javaparser.ast.expr.Name;
//import com.github.javaparser.*;
/**
 * A Simple Reverse Polish Notation calculator with memory function.
 */
public class ReversePolishNotation {

    // What does this do?
    public static int ONE_BILLION = 1000000000;

    private double memory = 0;

    /**
     * Takes reverse polish notation style string and returns the resulting calculation.
     *
     * @param input mathematical expression in the reverse Polish notation format
     * @return the calculation result
     */
    public Double calc(String input) {

        String[] tokens = input.split(" ");
        Stack<Double> numbers = new Stack<>();

        Stream.of(tokens).forEach(t -> {
            double a;
            double b;
            switch(t){
                case "+":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a + b);
                    break;
                case "/":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a / b);
                    break;
                case "-":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a - b);
                    break;
                case "*":
                    b = numbers.pop();
                    a = numbers.pop();
                    numbers.push(a * b);
                    break;
                default:
                    numbers.push(Double.valueOf(t));
            }
        });
        return numbers.pop();
    }

    /**
     * Memory Recall uses the number in stored memory, defaulting to 0.
     *
     * @return the double
     */
    public double memoryRecall(){
        return memory;
    }

    /**
     * Memory Clear sets the memory to 0.
     */
    public void memoryClear(){
        memory = 0;
    }


    public void memoryStore(double value){
        memory = value;
    }

}
/* EOF */
