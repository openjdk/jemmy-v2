package org.netbeans.jemmy.operators;

import org.testng.annotations.Test;

import java.awt.TextComponent;
import java.awt.TextField;

import static org.testng.Assert.assertEquals;

public class TextComponentOperatorTest {
    @Test
    public void selection() {
        TextComponent text = new TextField("0123456789");
        TextComponentOperator textOper = new TextComponentOperator(text);
        assertEquals(textOper.getDump().get(TextComponentOperator.SELECTED_TEXT_DPROP), "");
        text.select(1,1);
        assertEquals(textOper.getDump().get(TextComponentOperator.SELECTED_TEXT_DPROP), "");
        text.select(1,2);
        assertEquals(textOper.getDump().get(TextComponentOperator.SELECTED_TEXT_DPROP), "1");
    }
}
