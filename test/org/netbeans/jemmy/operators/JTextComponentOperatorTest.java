package org.netbeans.jemmy.operators;

import org.testng.annotations.Test;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import static org.testng.Assert.assertEquals;

public class JTextComponentOperatorTest {
    @Test
    public void selection() {
        JTextComponent text = new JTextField("0123456789");
        JTextComponentOperator textOper = new JTextComponentOperator(text);
        assertEquals(textOper.getDump().get(JTextComponentOperator.SELECTED_TEXT_DPROP), "");
        text.select(1,1);
        assertEquals(textOper.getDump().get(JTextComponentOperator.SELECTED_TEXT_DPROP), "");
        text.select(1,2);
        assertEquals(textOper.getDump().get(JTextComponentOperator.SELECTED_TEXT_DPROP), "1");
    }
}
