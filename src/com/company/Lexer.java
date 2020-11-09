package com.company;
import java.util.ArrayList;
import java.util.List;

enum Lexem {
    HEXNUMBER,
    DECNUMBER,
    FLOATNUMBER,
    SYMBOL,
    STRING,
    DIRECTIVE,
    RESERVED,
    OPERATOR,
    PUNCTATION,
    IDENTIFIER,
    ERROR,
    PACKAGE
}

public class Lexer {

    private List<LexemValue> lexems = new ArrayList<LexemValue>();

    private boolean inComment = false;
    private boolean inQuotes = false;
    private boolean inSingularQuotes = false;

    private String reserved = "boolean|break|byte|case|char|const|default|do|double|else|enum|false|final|finally|float|for|goto|if|import|int|long|new|null|package|return|short|switch|throw|true|try|void|while|continue";
    private String directive = "import";
    private String package_ = "^(java\\.)[A-Za-z.]+";
    private String whitespace = "\\s";
    private String hexNumber = "0x[0-9A-Fa-f]+";
    private String decNumber = "[1-9][0-9]*";
    private String floatingPointNumber = "[0-9]*\\.?[0-9]*";
    private String operator = ">=|!=|\\+\\+|--|==|\\+=|-=|\\*=|/=|<=|\\+|-|=|/|\\*|%|>|<|!|\\^|&|\\|?";
    private String punctation = "\\(|\\)|\\[|\\]|\\{|}|,|;|:";
    private String quote = "\"";
    private Character singularQuote = '\'';
    private String startComment = "\\*//";
    private String identifier = "^[a-zA-Z_][a-zA-Z0-9_]*$";

    public void DiplayResults()
    {
        for (LexemValue t : lexems)
        {
            System.out.print(t.Diplay());
        }
    }



    private void addLexem(StringBuilder w)
    {
        String word = w.toString();
        LexemValue lv;
        if (word.matches(reserved))
        {
            lv = new LexemValue(Lexem.RESERVED, word);
        }
        else if(word.matches(directive))
        {
            lv = new LexemValue(Lexem.DIRECTIVE, word);
        }
        else if(word.matches(package_))
        {
            lv = new LexemValue(Lexem.PACKAGE, word);
        }
        else if(word.matches(decNumber))
        {
            lv = new LexemValue(Lexem.DECNUMBER, word);
        }
        else if(word.matches(floatingPointNumber))
        {
            lv = new LexemValue(Lexem.FLOATNUMBER, word);
        }
        else if(word.matches(hexNumber))
        {
            lv = new LexemValue(Lexem.HEXNUMBER, word);
        }
        else if(word.matches(identifier))
        {
            lv = new LexemValue(Lexem.IDENTIFIER, word);
        }
        else
        {
            lv = new LexemValue(Lexem.ERROR, word);
        }
        lexems.add(lv);
    }

    public void eatLine(String l)
    {
        String line = l.trim();
        inComment = false;
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < line.length(); i++)
        {
            if (inComment)
            {
            }
            else if (inQuotes)
            {
                if (Character.toString(line.charAt(i)).matches(quote))
                {
                    inQuotes = false;
                    word.append(line.charAt(i));
                    LexemValue lv = new LexemValue(Lexem.STRING, word.toString());
                    lexems.add(lv);
                    word = new StringBuilder();
                }
                else
                {
                    word.append(line.charAt(i));
                }
            }
            else if(inSingularQuotes)
            {
                if (line.charAt(i)==singularQuote)
                {
                    inSingularQuotes = false;
                    word.append(line.charAt(i));
                    LexemValue lv;
                    if(word.length()==3) {
                        lv = new LexemValue(Lexem.SYMBOL, word.toString());
                    }
                    else
                    {
                        lv = new LexemValue(Lexem.ERROR, word.toString());
                    }
                    lexems.add(lv);
                    word = new StringBuilder();
                }
                else
                {
                    word.append(line.charAt(i));
                }
            }
            else if (Character.toString(line.charAt(i)).matches(quote))
            {
                inQuotes = true;
                word.append(line.charAt(i));
            }
            else if (Character.toString(line.charAt(i)).matches(punctation))
            {
                if (word.length() != 0)
                {
                    addLexem(word);
                    word = new StringBuilder();
                }
                LexemValue lv = new LexemValue(Lexem.PUNCTATION, Character.toString(line.charAt(i)));
                lexems.add(lv);
            }
            else if (line.charAt(i)==singularQuote)
            {
                inSingularQuotes = true;
                word.append(line.charAt(i));
            }
            else if (Character.toString(line.charAt(i)).matches(operator))
            {
                if (word.length() != 0)
                {
                    addLexem(word);
                    word = new StringBuilder();
                }
                if (i != (line.length() - 1))
                {
                    String doubleOp = Character.toString(line.charAt(i)) + Character.toString(line.charAt(i + 1));
                    if (doubleOp.equals("//"))
                    {
                        inComment = true;
                        i++;
                    }
                    else if (doubleOp.matches(operator))
                    {
                        LexemValue lv = new LexemValue(Lexem.OPERATOR, doubleOp);
                        lexems.add(lv);
                        i++;
                    }
                    else
                    {
                        LexemValue token = new LexemValue(Lexem.OPERATOR, Character.toString(line.charAt(i)));
                        lexems.add(token);
                    }
                }
                else {
                    LexemValue lv = new LexemValue(Lexem.OPERATOR, Character.toString(line.charAt(i)));
                    lexems.add(lv);
                }
            }
            else if ((Character.toString(line.charAt(i)).matches(whitespace)))
            {
                if (word.length() != 0)
                {
                    addLexem(word);
                    word = new StringBuilder();
                }
            }
            else {
                word.append(line.charAt(i));
                if(i==line.length() - 1)
                    addLexem(word);
            }
        }
    }
}
