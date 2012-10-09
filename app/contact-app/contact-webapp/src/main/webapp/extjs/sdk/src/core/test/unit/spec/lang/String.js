describe("Ext.String", function() {

    describe("ellipsis", function() {
        var shortString = "A short string",
            longString  = "A somewhat longer string";
        
        it("should keep short strings intact", function() {
            expect(Ext.String.ellipsis(shortString, 100)).toEqual(shortString);
        });
        
        it("should truncate a longer string", function() {
            expect(Ext.String.ellipsis(longString, 10)).toEqual("A somew...");
        });
        
        describe("word break", function() {
            var longStringWithDot  = "www.sencha.com",
                longStringWithExclamationMark = "Yeah!Yeah!Yeah!",
                longStringWithQuestionMark = "Who?When?What?";
                           
            it("should find a word break on ' '", function() {
                expect(Ext.String.ellipsis(longString, 10, true)).toEqual("A...");
            });      
            
            it("should be able to break on '.'", function() {
                expect(Ext.String.ellipsis(longStringWithDot, 9, true)).toEqual("www...");
            });  
            
            it("should be able to break on '!'", function() {
                expect(Ext.String.ellipsis(longStringWithExclamationMark, 9, true)).toEqual("Yeah...");
            }); 
            
            it("should be able to break on '?'", function() {
                expect(Ext.String.ellipsis(longStringWithQuestionMark, 8, true)).toEqual("Who...");
            });       
        });
    });
    
    describe("escapeRegex", function() {
        var str;
        
        it("should escape minus", function() {
            str = "12 - 175";
            expect(Ext.String.escapeRegex(str)).toEqual("12 \\- 175");
        });
        
        it("should escape dot", function() {
            str = "Brian is in the kitchen.";
            expect(Ext.String.escapeRegex(str)).toEqual("Brian is in the kitchen\\.");
        });
        
        it("should escape asterisk", function() {
            str = "12 * 175";
            expect(Ext.String.escapeRegex(str)).toEqual("12 \\* 175");
        });
        
        it("should escape plus", function() {
            str = "12 + 175";
            expect(Ext.String.escapeRegex(str)).toEqual("12 \\+ 175");
        });
        
        it("should escape question mark", function() {
            str = "What else ?";
            expect(Ext.String.escapeRegex(str)).toEqual("What else \\?");
        });
        
        it("should escape caret", function() {
            str = "^^";
            expect(Ext.String.escapeRegex(str)).toEqual("\\^\\^");
        });
        
        it("should escape dollar", function() {
            str = "500$";
            expect(Ext.String.escapeRegex(str)).toEqual("500\\$");
        });
        
        it("should escape open brace", function() {
            str = "something{stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\{stupid");
        });
        
        it("should escape close brace", function() {
            str = "something}stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\}stupid");
        });
        
        it("should escape open bracket", function() {
            str = "something[stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\[stupid");
        });
        
        it("should escape close bracket", function() {
            str = "something]stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\]stupid");
        });
        
        it("should escape open parenthesis", function() {
            str = "something(stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\(stupid");
        });
        
        it("should escape close parenthesis", function() {
            str = "something)stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\)stupid");
        });
        
        it("should escape vertival bar", function() {
            str = "something|stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\|stupid");
        });
        
        it("should escape forward slash", function() {
            str = "something/stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\/stupid");
        });
        
        it("should escape backslash", function() {
            str = "something\\stupid";
            expect(Ext.String.escapeRegex(str)).toEqual("something\\\\stupid");
        });
    });
    
    describe("htmlEncode", function() {
        var str;

        it("should replace ampersands", function() {
            str = "Fish & Chips";
            expect(Ext.String.htmlEncode(str)).toEqual("Fish &amp; Chips");
        });
        
        it("should replace less than", function() {
            str = "Fish > Chips";
            expect(Ext.String.htmlEncode(str)).toEqual("Fish &gt; Chips");
        });
        
        it("should replace greater than", function() {
            str = "Fish < Chips";
            expect(Ext.String.htmlEncode(str)).toEqual("Fish &lt; Chips");
        });
        
        it("should replace double quote", function() {
            str = 'Fish " Chips';
            expect(Ext.String.htmlEncode(str)).toEqual("Fish &quot; Chips");
        });

        it("should replace apostraphes", function() {
            str = "Fish ' Chips";
            expect(Ext.String.htmlEncode(str)).toEqual("Fish &#39; Chips");
        });

        describe("adding character entities", function(){

            var src = "A string with entities: èÜçñ¶",
                encoded = "A string with entities: &egrave;&Uuml;&ccedil;&ntilde;&para;";

            beforeEach(function(){
                Ext.String.addCharacterEntities({
                    '&Uuml;':'\u00dc',
                    '&ccedil;':'\u00e7',
                    '&ntilde;':'ñ',
                    '&egrave;':'è',
                    '&para;':'¶'
                });
            });

            afterEach(function(){
                Ext.String.resetCharacterEntities();
            });


            it("should allow extending the character entity set", function(){
                expect(Ext.String.htmlEncode(src)).toBe(encoded);
            });
        });
    });
    
    describe("htmlDecode", function() {
        var str;

        it("should replace ampersands", function() {
            str = "Fish &amp; Chips";
            expect(Ext.String.htmlDecode(str)).toEqual("Fish & Chips");
        });
        
        it("should replace less than", function() {
            str = "Fish &gt; Chips";
            expect(Ext.String.htmlDecode(str)).toEqual("Fish > Chips");
        });
        
        it("should replace greater than", function() {
            str = "Fish &lt; Chips";
            expect(Ext.String.htmlDecode(str)).toEqual("Fish < Chips");
        });
        
        it("should replace double quote", function() {
            str = 'Fish &quot; Chips';
            expect(Ext.String.htmlDecode(str)).toEqual('Fish " Chips');
        });

        it("should replace apostraphes", function() {
            str = "Fish &#39; Chips";
            expect(Ext.String.htmlDecode(str)).toEqual("Fish ' Chips");
        });

        describe("adding character entities", function(){

            var src = "A string with entities: èÜçñ¶",
                encoded = "A string with entities: &egrave;&Uuml;&ccedil;&ntilde;&para;";

            beforeEach(function(){
                Ext.String.addCharacterEntities({
                    '&Uuml;':'\u00dc',
                    '&ccedil;':'\u00e7',
                    '&ntilde;':'ñ',
                    '&egrave;':'è',
                    '&para;':'¶'
                });
            });

            afterEach(function(){
                Ext.String.resetCharacterEntities();
            });

            it("should allow extending the character entity set", function(){
                expect(Ext.String.htmlDecode(encoded)).toBe(src);
            });
        });
    });
    
    describe("escaping", function() {
        it("should leave an empty string alone", function() {
            expect(Ext.String.escape('')).toEqual('');
        });
        
        it("should leave a non-empty string without escapable characters alone", function() {
            expect(Ext.String.escape('Ed')).toEqual('Ed');
        });
        
        it("should correctly escape a double backslash", function() {
            expect(Ext.String.escape("\\")).toEqual("\\\\");
        });
        
        it("should correctly escape a single backslash", function() {
            expect(Ext.String.escape('\'')).toEqual('\\\'');
        });
        
        it("should correctly escape a mixture of escape and non-escape characters", function() {
            expect(Ext.String.escape('\'foo\\')).toEqual('\\\'foo\\\\');
        });
    });
    
    describe("formatting", function() {
        it("should leave a string without format parameters alone", function() {
            expect(Ext.String.format('Ed')).toEqual('Ed');
        });
        
        it("should ignore arguments that don't map to format params", function() {
            expect(Ext.String.format("{0} person", 1, 123)).toEqual("1 person");
        });
        
        it("should accept several format parameters", function() {
            expect(Ext.String.format("{0} person {1}", 1, 'came')).toEqual('1 person came');
        });
    });
    
    describe("leftPad", function() {
        it("should pad the left side of an empty string", function() {
            expect(Ext.String.leftPad("", 5)).toEqual("     ");
        });
        
        it("should pad the left side of a non-empty string", function() {
            expect(Ext.String.leftPad("Ed", 5)).toEqual("   Ed");
        });
        
        it("should not pad a string where the character count already exceeds the pad count", function() {
            expect(Ext.String.leftPad("Abraham", 5)).toEqual("Abraham");
        });
        
        it("should allow a custom padding character", function() {
            expect(Ext.String.leftPad("Ed", 5, "0")).toEqual("000Ed");
        });
    });
    
    describe("when toggling between two values", function() {
        it("should use the first toggle value if the string is not already one of the toggle values", function() {
            expect(Ext.String.toggle("Aaron", "Ed", "Abe")).toEqual("Ed");
        });
        
        it("should toggle to the second toggle value if the string is currently the first", function() {
            expect(Ext.String.toggle("Ed", "Ed", "Abe")).toEqual("Abe");
        });
        
        it("should toggle to the first toggle value if the string is currently the second", function() {
            expect(Ext.String.toggle("Abe", "Ed", "Abe")).toEqual("Ed");
        });
    });
    
    describe("trimming", function() {
        it("should not modify an empty string", function() {
            expect(Ext.String.trim("")).toEqual("");
        });
        
        it("should not modify a string with no whitespace", function() {
            expect(Ext.String.trim("Abe")).toEqual("Abe");
        });
        
        it("should trim a whitespace-only string", function() {
            expect(Ext.String.trim("     ")).toEqual("");
        });
        
        it("should trim leading whitespace", function() {
            expect(Ext.String.trim("  Ed")).toEqual("Ed");
        });
        
        it("should trim trailing whitespace", function() {
            expect(Ext.String.trim("Ed   ")).toEqual("Ed");
        });
        
        it("should trim leading and trailing whitespace", function() {
            expect(Ext.String.trim("   Ed  ")).toEqual("Ed");
        });
        
        it("should not trim whitespace between words", function() {
            expect(Ext.String.trim("Fish and chips")).toEqual("Fish and chips");
            expect(Ext.String.trim("   Fish and chips  ")).toEqual("Fish and chips");
        });
        
        it("should trim tabs", function() {
            expect(Ext.String.trim("\tEd")).toEqual("Ed");
        });
        
        it("should trim a mixture of tabs and whitespace", function() {
            expect(Ext.String.trim("\tEd   ")).toEqual("Ed");
        });
    });
    
    describe("urlAppend", function(){
        it("should leave the string untouched if the second argument is empty", function(){
            expect(Ext.String.urlAppend('sencha.com')).toEqual('sencha.com');    
        });
        
        it("should append a ? if one doesn't exist", function(){
            expect(Ext.String.urlAppend('sencha.com', 'foo=bar')).toEqual('sencha.com?foo=bar');
        });
        
        it("should append any new values with & if a ? exists", function(){
            expect(Ext.String.urlAppend('sencha.com?x=y', 'foo=bar')).toEqual('sencha.com?x=y&foo=bar');
        });
    });
    
    describe("capitalize", function(){
        it("should handle an empty string", function(){
            expect(Ext.String.capitalize('')).toEqual('');
        });
        
        it("should capitalize the first letter of the string", function(){
            expect(Ext.String.capitalize('open')).toEqual('Open');
        });
        
        it("should leave the first letter capitalized if it is already capitalized", function(){
            expect(Ext.String.capitalize('Closed')).toEqual('Closed');
        });
        
        it("should capitalize a single letter", function(){
            expect(Ext.String.capitalize('a')).toEqual('A');
        });
        
        it("should capitalize even when spaces are included", function(){
            expect(Ext.String.capitalize('this is a sentence')).toEqual('This is a sentence');
        });
    });

    describe("uncapitalize", function(){
        it("should handle an empty string", function(){
            expect(Ext.String.uncapitalize('')).toEqual('');
        });
        
        it("should uncapitalize the first letter of the string", function(){
            expect(Ext.String.uncapitalize('Foo')).toEqual('foo');
        });
        
        it("should ignore case in the rest of the string", function() {
            expect(Ext.String.uncapitalize('FooBar')).toEqual('fooBar'); 
        });
        
        it("should leave the first letter uncapitalized if it is already uncapitalized", function(){
            expect(Ext.String.uncapitalize('fooBar')).toEqual('fooBar');
        });
        
        it("should uncapitalize a single letter", function(){
            expect(Ext.String.uncapitalize('F')).toEqual('f');
        });

        it("should uncapitalize even when spaces are included", function(){
            expect(Ext.String.uncapitalize('This is a sentence')).toEqual('this is a sentence');
        });
    });
    
    describe('repeat', function() {
        it('should return an empty string if count == 0', function() {
            expect(Ext.String.repeat('an ordinary string', 0)).toBe('');
        });
        it('should repeat the pattern as many times as required using the specified separator', function() {
            expect(Ext.String.repeat('an ordinary string', 1, '/')).toBe('an ordinary string');
            expect(Ext.String.repeat('an ordinary string', 2, '&')).toBe('an ordinary string&an ordinary string');
            expect(Ext.String.repeat('an ordinary string', 3, '%')).toBe('an ordinary string%an ordinary string%an ordinary string');
        });
        it('should concatenate the repetitions if no separator is specified', function() {
            expect(Ext.String.repeat('foo', 3)).toBe('foofoofoo');
            expect(Ext.String.repeat('bar baz', 3)).toBe('bar bazbar bazbar baz');
        });
    });

    describe('splitWords', function () {
        it('should handle no args', function () {
            var words = Ext.String.splitWords();
            expect(Ext.encode(words)).toEqual('[]');
        });
        it('should handle null', function () {
            var words = Ext.String.splitWords(null);
            expect(Ext.encode(words)).toEqual('[]');
        });
        it('should handle an empty string', function () {
            var words = Ext.String.splitWords('');
            expect(Ext.encode(words)).toEqual('[]');
        });
        it('should handle one trimmed word', function () {
            var words = Ext.String.splitWords('foo');
            expect(Ext.encode(words)).toEqual('["foo"]');
        });
        it('should handle one word with spaces around it', function () {
            var words = Ext.String.splitWords(' foo ');
            expect(Ext.encode(words)).toEqual('["foo"]');
        });
        it('should handle two trimmed words', function () {
            var words = Ext.String.splitWords('foo bar');
            expect(Ext.encode(words)).toEqual('["foo","bar"]');
        });
        it('should handle two untrimmed words', function () {
            var words = Ext.String.splitWords('  foo  bar  ');
            expect(Ext.encode(words)).toEqual('["foo","bar"]');
        });
        it('should handle five trimmed words', function () {
            var words = Ext.String.splitWords('foo bar bif boo foobar');
            expect(Ext.encode(words)).toEqual('["foo","bar","bif","boo","foobar"]');
        });
        it('should handle five untrimmed words', function () {
            var words = Ext.String.splitWords(' foo   bar   bif   boo  foobar    \t');
            expect(Ext.encode(words)).toEqual('["foo","bar","bif","boo","foobar"]');
        });
    })
});
