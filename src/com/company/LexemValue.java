package com.company;

public class LexemValue {
    private Lexem lexem;
    private String value;

    public LexemValue(Lexem l, String v)
    {
        lexem = l;
        value = v;
    }

    public String Diplay() {
        return lexem + " : " + value + "\n";
    }
}