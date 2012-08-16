// $ANTLR 2.7.5 (20050128): "ValidWhenParser.g" -> "ValidWhenParser.java"$

/*
* $Header: /cvs/springmodules/projects/validation/src/java/org/springmodules/validation/commons/validwhen/ValidWhenParserTokenTypes.java,v 1.3 2006/10/27 01:59:59 hueboness Exp $
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

public interface ValidWhenParserTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int DECIMAL_LITERAL = 4;

    int HEX_LITERAL = 5;

    int STRING_LITERAL = 6;

    int IDENTIFIER = 7;

    int LBRACKET = 8;

    int RBRACKET = 9;

    int LITERAL_null = 10;

    int THIS = 11;

    int LPAREN = 12;

    int RPAREN = 13;

    int ANDSIGN = 14;

    int ORSIGN = 15;

    int EQUALSIGN = 16;

    int GREATERTHANSIGN = 17;

    int GREATEREQUALSIGN = 18;

    int LESSTHANSIGN = 19;

    int LESSEQUALSIGN = 20;

    int NOTEQUALSIGN = 21;

    int WS = 22;
}
