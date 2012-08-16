// $ANTLR 2.7.5 (20050128): "ValidWhenParser.g" -> "ValidWhenLexer.java"$

/*
* $Header: /cvs/springmodules/projects/validation/src/java/org/springmodules/validation/commons/validwhen/ValidWhenLexer.java,v 1.3 2006/10/27 01:59:59 hueboness Exp $
* $Revision: 1.3 $
* $Date: 2006/10/27 01:59:59 $
*
* Copyright 2003-2004 The Apache Software Foundation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.springmodules.validation.commons.validwhen;

import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

import antlr.*;
import antlr.collections.impl.BitSet;

public class ValidWhenLexer extends antlr.CharScanner implements ValidWhenParserTokenTypes, TokenStream {

    public ValidWhenLexer(InputStream in) {
        this(new ByteBuffer(in));
    }

    public ValidWhenLexer(Reader in) {
        this(new CharBuffer(in));
    }

    public ValidWhenLexer(InputBuffer ib) {
        this(new LexerSharedInputState(ib));
    }

    public ValidWhenLexer(LexerSharedInputState state) {
        super(state);
        caseSensitiveLiterals = true;
        setCaseSensitive(false);
        literals = new Hashtable();
        literals.put(new ANTLRHashString("null", this), new Integer(10));
        literals.put(new ANTLRHashString("or", this), new Integer(15));
        literals.put(new ANTLRHashString("and", this), new Integer(14));
    }

    public Token nextToken() throws TokenStreamException {
        Token theRetToken = null;
        tryAgain:
        for (; ;) {
            Token _token = null;
            int _ttype = Token.INVALID_TYPE;
            resetText();
            try {   // for char stream error handling
                try {   // for lexical error handling
                    switch (LA(1)) {
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ': {
                            mWS(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case '"':
                        case '\'': {
                            mSTRING_LITERAL(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case '[': {
                            mLBRACKET(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case ']': {
                            mRBRACKET(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case '(': {
                            mLPAREN(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case ')': {
                            mRPAREN(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case '*': {
                            mTHIS(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case '.':
                        case '_':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'g':
                        case 'h':
                        case 'i':
                        case 'j':
                        case 'k':
                        case 'l':
                        case 'm':
                        case 'n':
                        case 'o':
                        case 'p':
                        case 'q':
                        case 'r':
                        case 's':
                        case 't':
                        case 'u':
                        case 'v':
                        case 'w':
                        case 'x':
                        case 'y':
                        case 'z': {
                            mIDENTIFIER(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case '=': {
                            mEQUALSIGN(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        case '!': {
                            mNOTEQUALSIGN(true);
                            theRetToken = _returnToken;
                            break;
                        }
                        default:
                            if ((LA(1) == '0') && (LA(2) == 'x')) {
                                mHEX_LITERAL(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '<') && (LA(2) == '=')) {
                                mLESSEQUALSIGN(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '>') && (LA(2) == '=')) {
                                mGREATEREQUALSIGN(true);
                                theRetToken = _returnToken;
                            } else if ((_tokenSet_0.member(LA(1))) && (true)) {
                                mDECIMAL_LITERAL(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '<') && (true)) {
                                mLESSTHANSIGN(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '>') && (true)) {
                                mGREATERTHANSIGN(true);
                                theRetToken = _returnToken;
                            } else {
                                if (LA(1) == EOF_CHAR) {
                                    uponEOF();
                                    _returnToken = makeToken(Token.EOF_TYPE);
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                    }
                    if (_returnToken == null) continue tryAgain; // found SKIP token
                    _ttype = _returnToken.getType();
                    _ttype = testLiteralsTable(_ttype);
                    _returnToken.setType(_ttype);
                    return _returnToken;
                }
                catch (RecognitionException e) {
                    throw new TokenStreamRecognitionException(e);
                }
            }
            catch (CharStreamException cse) {
                if (cse instanceof CharStreamIOException) {
                    throw new TokenStreamIOException(((CharStreamIOException) cse).io);
                } else {
                    throw new TokenStreamException(cse.getMessage());
                }
            }
        }
    }

    public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = WS;
        int _saveIndex;

        {
            int _cnt15 = 0;
            _loop15:
            do {
                switch (LA(1)) {
                    case ' ': {
                        match(' ');
                        break;
                    }
                    case '\t': {
                        match('\t');
                        break;
                    }
                    case '\n': {
                        match('\n');
                        break;
                    }
                    case '\r': {
                        match('\r');
                        break;
                    }
                    default: {
                        if (_cnt15 >= 1) {
                            break _loop15;
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
                }
                _cnt15++;
            } while (true);
        }
        _ttype = Token.SKIP;
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mDECIMAL_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = DECIMAL_LITERAL;
        int _saveIndex;

        {
            switch (LA(1)) {
                case '-': {
                    match('-');
                    break;
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    break;
                }
                default: {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
            }
        }
        {
            switch (LA(1)) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    {
                        {
                            matchRange('1', '9');
                        }
                        {
                            _loop22:
                            do {
                                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                    matchRange('0', '9');
                                } else {
                                    break _loop22;
                                }

                            } while (true);
                        }
                    }
                    {
                        if ((LA(1) == '.')) {
                            match('.');
                            {
                                int _cnt25 = 0;
                                _loop25:
                                do {
                                    if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                        matchRange('0', '9');
                                    } else {
                                        if (_cnt25 >= 1) {
                                            break _loop25;
                                        } else {
                                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                        }
                                    }

                                    _cnt25++;
                                } while (true);
                            }
                        } else {
                        }

                    }
                    break;
                }
                case '0': {
                    {
                        match('0');
                        match('.');
                        {
                            int _cnt28 = 0;
                            _loop28:
                            do {
                                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                    matchRange('0', '9');
                                } else {
                                    if (_cnt28 >= 1) {
                                        break _loop28;
                                    } else {
                                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                    }
                                }

                                _cnt28++;
                            } while (true);
                        }
                    }
                    break;
                }
                default: {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
            }
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mHEX_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = HEX_LITERAL;
        int _saveIndex;

        match('0');
        match('x');
        {
            int _cnt31 = 0;
            _loop31:
            do {
                switch (LA(1)) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        matchRange('0', '9');
                        break;
                    }
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f': {
                        matchRange('a', 'f');
                        break;
                    }
                    default: {
                        if (_cnt31 >= 1) {
                            break _loop31;
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
                }
                _cnt31++;
            } while (true);
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = STRING_LITERAL;
        int _saveIndex;

        switch (LA(1)) {
            case '\'': {
                {
                    match('\'');
                    {
                        int _cnt35 = 0;
                        _loop35:
                        do {
                            if ((_tokenSet_1.member(LA(1)))) {
                                matchNot('\'');
                            } else {
                                if (_cnt35 >= 1) {
                                    break _loop35;
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }

                            _cnt35++;
                        } while (true);
                    }
                    match('\'');
                }
                break;
            }
            case '"': {
                {
                    match('\"');
                    {
                        int _cnt38 = 0;
                        _loop38:
                        do {
                            if ((_tokenSet_2.member(LA(1)))) {
                                matchNot('\"');
                            } else {
                                if (_cnt38 >= 1) {
                                    break _loop38;
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }

                            _cnt38++;
                        } while (true);
                    }
                    match('\"');
                }
                break;
            }
            default: {
                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
            }
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLBRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LBRACKET;
        int _saveIndex;

        match('[');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRBRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RBRACKET;
        int _saveIndex;

        match(']');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LPAREN;
        int _saveIndex;

        match('(');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RPAREN;
        int _saveIndex;

        match(')');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mTHIS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = THIS;
        int _saveIndex;

        match("*this*");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mIDENTIFIER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = IDENTIFIER;
        int _saveIndex;

        {
            switch (LA(1)) {
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z': {
                    matchRange('a', 'z');
                    break;
                }
                case '.': {
                    match('.');
                    break;
                }
                case '_': {
                    match('_');
                    break;
                }
                default: {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
            }
        }
        {
            int _cnt47 = 0;
            _loop47:
            do {
                switch (LA(1)) {
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case 'z': {
                        matchRange('a', 'z');
                        break;
                    }
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        matchRange('0', '9');
                        break;
                    }
                    case '.': {
                        match('.');
                        break;
                    }
                    case '_': {
                        match('_');
                        break;
                    }
                    default: {
                        if (_cnt47 >= 1) {
                            break _loop47;
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
                }
                _cnt47++;
            } while (true);
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mEQUALSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = EQUALSIGN;
        int _saveIndex;

        match('=');
        match('=');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mNOTEQUALSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = NOTEQUALSIGN;
        int _saveIndex;

        match('!');
        match('=');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLESSTHANSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LESSTHANSIGN;
        int _saveIndex;

        match('<');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mGREATERTHANSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = GREATERTHANSIGN;
        int _saveIndex;

        match('>');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLESSEQUALSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LESSEQUALSIGN;
        int _saveIndex;

        match('<');
        match('=');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mGREATEREQUALSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = GREATEREQUALSIGN;
        int _saveIndex;

        match('>');
        match('=');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }


    private static final long[] mk_tokenSet_0() {
        long[] data = {287984085547089920L, 0L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
        long[] data = {-549755813889L, -1L, 0L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2() {
        long[] data = {-17179869185L, -1L, 0L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

}
