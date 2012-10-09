describe("Ext.util.Format", function() {
    var savedFormatLocale = {
            thousandSeparator: Ext.util.Format.thousandSeparator,
            decimalSeparator: Ext.util.Format.decimalSeparator,
            currencySign: Ext.util.Format.currencySign
        };

    describe("undef", function() {
        it("should return the value itself if defined", function() {
            expect(Ext.util.Format.undef("this is a defined value")).toBe("this is a defined value");
            expect(Ext.util.Format.undef(12345)).toBe(12345);
            expect(Ext.util.Format.undef(12345.67)).toBe(12345.67);
            expect(Ext.util.Format.undef(true)).toBe(true);
        });
        it("should return an empty string if the value is undefined", function() {
            expect(Ext.util.Format.undef(undefined)).toBe("");
        });
    });

    describe("defaultValue", function() {
        it("should return the value itself if defined", function () {
            expect(Ext.util.Format.defaultValue("value", "default value")).toBe("value");
        });
        it("should return the default value if the value is undefined", function() {
            expect(Ext.util.Format.defaultValue(undefined, "default value")).toBe("default value");
        });
        it("should return the default value if the value is empty", function() {
            expect(Ext.util.Format.defaultValue("", "default value")).toBe("default value");
        });
    });

    describe("substr", function() {
        it("should truncate the string from the start position", function() {
            expect(Ext.util.Format.substr("abc", 0, 1)).toBe("a");
            expect(Ext.util.Format.substr("abc", 1, 1)).toBe("b");
            expect(Ext.util.Format.substr("abc", 2, 1)).toBe("c");
        });
        it("should truncate the string at the specified length", function() {
            expect(Ext.util.Format.substr("abc", 0, 0)).toBe("");
            expect(Ext.util.Format.substr("abc", 0, 1)).toBe("a");
            expect(Ext.util.Format.substr("abc", 0, 2)).toBe("ab");
            expect(Ext.util.Format.substr("abc", 0, 3)).toBe("abc");
        });
        it("should convert non-string values to its string representation and then truncate", function() {
            expect(Ext.util.Format.substr(1234, 1, 2)).toBe("23");
            expect(Ext.util.Format.substr(true, 1, 2)).toBe("ru");
        });
        it("should start at the end of the string if start value is negative", function() {
            expect(Ext.util.Format.substr("abc", -1, 1)).toBe("c");
            expect(Ext.util.Format.substr("abc", -2, 1)).toBe("b");
            expect(Ext.util.Format.substr("abc", -3, 1)).toBe("a");
            expect(Ext.util.Format.substr("abc", -4, 1)).toBe("a");
            expect(Ext.util.Format.substr("abc", -5, 1)).toBe("a");
        });
        it("should return empty string if start position is out of bounds", function() {
            expect(Ext.util.Format.substr("abc", 4, 1)).toBe("");
            expect(Ext.util.Format.substr("abc", 5, 1)).toBe("");
            expect(Ext.util.Format.substr("abc", 6, 1)).toBe("");
        });
        it("should return empty string if length is negative", function() {
            expect(Ext.util.Format.substr("abc", 0, -1)).toBe("");
            expect(Ext.util.Format.substr("abc", 0, -2)).toBe("");
            expect(Ext.util.Format.substr("abc", 0, -3)).toBe("");
        });
        it("should return the whole string if specified length is greater than string length", function() {
            expect(Ext.util.Format.substr("abc", 0, 4)).toBe("abc");
            expect(Ext.util.Format.substr("abc", 0, 5)).toBe("abc");
            expect(Ext.util.Format.substr("abc", 1, 3)).toBe("bc");
            expect(Ext.util.Format.substr("abc", 2, 2)).toBe("c");
        });
    });

    describe("lowercase", function() {
        it("should preserve lowercase strings", function() {
            expect(Ext.util.Format.lowercase("lowercase string")).toBe("lowercase string");
        });
        it("should convert uppercase strings to lowercase", function() {
            expect(Ext.util.Format.lowercase("UPPERCASE STRING")).toBe("uppercase string");
        });
        it("should convert mixed lowercase/uppercase strings to lowercase", function() {
            expect(Ext.util.Format.lowercase("MIXED string")).toBe("mixed string");
            expect(Ext.util.Format.lowercase("mixed STRING")).toBe("mixed string");
            expect(Ext.util.Format.lowercase("MiXeD sTrIng")).toBe("mixed string");
        });
        it("should be null/undefined safe", function() {
            expect(Ext.util.Format.lowercase(undefined)).toBe("undefined");
            expect(Ext.util.Format.lowercase(null)).toBe("null");
        });
        it("should cast non-string values before processing", function() {
            expect(Ext.util.Format.lowercase(123)).toBe("123");
            expect(Ext.util.Format.lowercase(true)).toBe("true");
        });
    });

    describe("uppercase", function() {
        it("should preserve uppercase strings", function() {
            expect(Ext.util.Format.uppercase("UPPERCASE STRING")).toBe("UPPERCASE STRING");
        });
        it("should convert lowercase strings to uppercase", function() {
            expect(Ext.util.Format.uppercase("lowercase string")).toBe("LOWERCASE STRING");
        });
        it("should convert mixed lowercase/uppercase strings to uppercase", function() {
            expect(Ext.util.Format.uppercase("MIXED string")).toBe("MIXED STRING");
            expect(Ext.util.Format.uppercase("mixed STRING")).toBe("MIXED STRING");
            expect(Ext.util.Format.uppercase("MiXeD sTrIng")).toBe("MIXED STRING");
        });
        it("should be null/undefined safe", function() {
            expect(Ext.util.Format.uppercase(undefined)).toBe("UNDEFINED");
            expect(Ext.util.Format.uppercase(null)).toBe("NULL");
        });
        it("should cast non-string values before processing", function() {
            expect(Ext.util.Format.uppercase(123)).toBe("123");
            expect(Ext.util.Format.uppercase(true)).toBe("TRUE");
        });
    });

    describe("usMoney", function(){
        it("should format with 2 decimals, prefixed by a dollar sign", function() {
            expect(Ext.util.Format.usMoney(1234.567)).toEqual("$1,234.57");
        });
        it("should format with 2 decimals, prefixed by a negative sign, and a dollar sign", function() {
            expect(Ext.util.Format.usMoney(-1234.567)).toEqual("-$1,234.57");
        });
        it("should format with a comma as a thousand separator", function() {
            expect(Ext.util.Format.usMoney(1234567.89)).toEqual("$1,234,567.89");
        });
    });


    describe("currency in FR locale", function(){
        beforeEach(function() {
            Ext.apply(Ext.util.Format, {
                thousandSeparator: '.',
                decimalSeparator: ',',
                currencySign: '\u20ac',
                dateFormat: 'd/m/Y'
            });
        });
        afterEach(function() {
            Ext.apply(Ext.util.Format, savedFormatLocale);
        });

        it("should format with 2 decimals, prefixed by a euro sign", function() {
            expect(Ext.util.Format.currency(1234.567)).toEqual("\u20ac1.234,57");
        });
        it("should format with 2 decimals, prefixed by a negative sign, and a euro sign", function() {
            expect(Ext.util.Format.currency(-1234.567)).toEqual("-\u20ac1.234,57");
        });
    });

    describe("currency", function() {
        it("should allow 0 for a decimal value", function(){
            expect(Ext.util.Format.currency(100, '$', 0)).toBe('$100');
        });
        it("should position currency signal where specified", function() {
          expect(Ext.util.Format.currency(123.45, '$', 2)).toBe("$123.45");
          expect(Ext.util.Format.currency(123.45, '$', 2, false)).toBe("$123.45");
          expect(Ext.util.Format.currency(123.45, '$', 2, true)).toBe("123.45$");
        });
    });

    describe("number in default (US) locale", function() {
        it("should format with no decimals", function() {
            expect(Ext.util.Format.number(1, "0")).toEqual("1");
        });
        it("should format with two decimals", function() {
            expect(Ext.util.Format.number(1, "0.00")).toEqual("1.00");
        });
        it("should format+round with two decimals, and no thousand separators", function() {
            expect(Ext.util.Format.number(1234.567, "0.00")).toEqual("1234.57");
        });
        it("should format+round with two decimals, and ',' as the thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ",0.00")).toEqual("1,234.57");
        });
        it("should format+round with no decimals, and ',' as the thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ",0")).toEqual("1,235");
        });
    });

    describe("number using FR locale", function() {
        var savedFormatLocale = {
            thousandSeparator: Ext.util.Format.thousandSeparator,
            decimalSeparator: Ext.util.Format.decimalSeparator,
            currencySign: Ext.util.Format.currencySign,
            dateFormat: Ext.util.Format.dateFormat
        };

        beforeEach(function() {
            Ext.apply(Ext.util.Format, {
                thousandSeparator: '.',
                decimalSeparator: ',',
                currencySign: '\u20ac',
                dateFormat: 'd/m/Y'
            });
        });
        afterEach(function() {
            Ext.apply(Ext.util.Format, savedFormatLocale);
        });

        it("should format with no decimals", function() {
            expect(Ext.util.Format.number(1, "0")).toEqual("1");
        });
        it("should format with two decimals", function() {
            expect(Ext.util.Format.number(1, "0.00")).toEqual("1,00");
        });
        it("should format+round with two decimals, and no thousand separators", function() {
            expect(Ext.util.Format.number(1234.567, "0.00")).toEqual("1234,57");
        });
        it("should format+round with two decimals after a ',', and '.' as the thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ",0.00")).toEqual("1.234,57");
        });
        it("should format+round with no decimals, and '.' as the thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ",0")).toEqual("1.235");
        });
    });

    // In Ext4, the "/i" suffix allows you to use locale-specific separators in the format string, as opposed
    // to US/UK conventions. Output however ALWAYS follows the local settings in the Format singleton which may
    // be overridden by locale files.
    describe("number using FR locale with /i", function() {
        var savedFormatLocale = {
            thousandSeparator: Ext.util.Format.thousandSeparator,
            decimalSeparator: Ext.util.Format.decimalSeparator,
            currencySign: Ext.util.Format.currencySign,
            dateFormat: Ext.util.Format.dateFormat
        };

        // set up the FR formatting locale
        beforeEach(function() {
            Ext.apply(Ext.util.Format, {
                thousandSeparator: '.',
                decimalSeparator: ',',
                currencySign: '\u20ac',
                dateFormat: 'd/m/Y'
            });
        });
        afterEach(function() {
            Ext.apply(Ext.util.Format, savedFormatLocale);
        });

        // Demonstrate "Incorrect" use with "/i". '.' means thousand separator and ',' means decimal in FR locale.
        // Read carefully. In the formatting strings below, '.' is taken to mean thousand separator, and
        // ',' is taken to mean decimal separator
        it("should format with no decimals", function() {
            expect(Ext.util.Format.number(1, "0.00/i")).toEqual("1");
        });
        it("should format+round with no decimals, and '.' as thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, "0.00/i")).toEqual("1.235");
        });
        it("should format+round with three decimals after a ',', and '.' as the thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ",0.00/i")).toEqual("1.234,567");
        });
        it("should format+round with one decimal, and no thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ",0/i")).toEqual("1234,6");
        });

        // Correct usage
        it("should format with two decimals", function() {
            expect(Ext.util.Format.number(1, "0,00/i")).toEqual("1,00");
        });
        it("should format+round with two decimals, and no thousand separators", function() {
            expect(Ext.util.Format.number(1234.567, "0,00/i")).toEqual("1234,57");
        });
        it("should format+round with two decimals after a ',', and '.' as the thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ".0,00/i")).toEqual("1.234,57");
        });
        it("should format+round with no decimals, and '.' as the thousand separator", function() {
            expect(Ext.util.Format.number(1234.567, ".0/i")).toEqual("1.235");
        });

    });

    it("should check for a 0 value before appending negative", function(){
        expect(Ext.util.Format.number(-2.842170943040401e-14, "0,000.00")).toEqual('0.00');
    });

    describe("number", function() {
        it("should return the number itself if formatString is not specified", function() {
            expect(Ext.util.Format.number(12345.67, undefined)).toBe(12345.67);
            expect(Ext.util.Format.number(12345.67, null)).toBe(12345.67);
        });
        it("should return empty string if value is not a number", function() {
            expect(Ext.util.Format.number("this is not a number", "0.00")).toBe("");
        });

        it("should raise error if more than one decimal point is specified in the format string", function() {
            expect(function() { Ext.util.Format.number("1234.67", "0.0.00") }).toRaiseExtError();
        });
    });

    describe("date", function() {
        it("should return empty string for undefined values", function() {
            expect(Ext.util.Format.date(undefined)).toBe('');
        });
        it("should return empty string for null values", function() {
            expect(Ext.util.Format.date(null)).toBe('');
        });
        it("should parse string dates", function() {
            expect(Ext.util.Format.date("10/15/81")).toBe("10/15/1981");
        });
        it("should format according to Ext.Date.defaultFormat if no format was specified", function() {
            var date = new Date(1981, 9, 15, 15, 46, 30);
            expect(Ext.util.Format.date(date)).toBe("10/15/1981");
        });
        it("should format according to specified format when specified", function() {
            var date = new Date(1981, 9, 15, 15, 46, 30);
            expect(Ext.util.Format.date(date, "d/m/Y H:i:s")).toBe("15/10/1981 15:46:30");
        });
    });

    describe("dateRenderer", function() {
        it("should return a function that formats dates with the specified format", function() {
            var date = new Date(1981, 9, 15, 15, 46, 30);
            expect(Ext.util.Format.dateRenderer("d/m/Y H:i:s").call(this, date)).toBe("15/10/1981 15:46:30");
        });
    });

    describe("stripTags", function() {
        it("should return undefined if value is undefined", function() {
            expect(Ext.util.Format.stripTags(undefined)).toBeUndefined();
        });
        it("should return null if value is null", function() {
            expect(Ext.util.Format.stripTags(null)).toBeNull();
        });
        it("should return the exact original value if it doesn't contains any tags", function() {
            expect(Ext.util.Format.stripTags("this string contains no tags")).toBe("this string contains no tags");
        });
        it("should strip tags when found", function() {
            expect(Ext.util.Format.stripTags("<p>this string <b>contains</b> tags</p>")).toBe("this string contains tags");
        });
    });

    describe("stripScripts", function() {
        it("should return undefined if value is undefined", function() {
            expect(Ext.util.Format.stripScripts(undefined)).toBeUndefined();
        });
        it("should return null if value is null", function() {
            expect(Ext.util.Format.stripScripts(null)).toBeNull();
        });
        it("should return the exact original value if it doesn't contains any scripts", function() {
            expect(Ext.util.Format.stripTags("this string contains no scripts")).toBe("this string contains no scripts");
        });
        it("should stript scripts when found", function() {
            expect(Ext.util.Format.stripScripts("<script>alert('foo');</script>this string <b>contains</b> scripts")).toBe("this string <b>contains</b> scripts");
        });
    });

    describe("fileSize", function() {
        it("should return the size in bytes if size < 1024", function() {
            expect(Ext.util.Format.fileSize(-9999999999)).toBe("-9999999999 bytes");
            expect(Ext.util.Format.fileSize(0)).toBe("0 bytes");
            expect(Ext.util.Format.fileSize(1023)).toBe("1023 bytes");
        });
        it("should return the size in kilobytes if 1024 <= size < 1MB", function() {
            expect(Ext.util.Format.fileSize(1024)).toBe("1 KB");
            expect(Ext.util.Format.fileSize(1024 * 1024 - 1)).toBe("1024 KB");
        });
        it("should return the size in megabytes if size >= 1MB", function() {
            expect(Ext.util.Format.fileSize(1024 * 1024)).toBe("1 MB");
            expect(Ext.util.Format.fileSize(1024 * 1024 * 100)).toBe("100 MB");
            expect(Ext.util.Format.fileSize(1024 * 1024 * 1024)).toBe("1024 MB");
        });
    });

    describe("math", function() {
        it("should return the first argument and the evaluation of the second argument", function() {
            expect(Ext.util.Format.math(12, '+ 12')).toBe(24);
            expect(Ext.util.Format.math(12, '* 12')).toBe(144);
        });
    });

    describe("round", function() {
        it("should preserve the original value if the precision is not specified", function() {
            expect(Ext.util.Format.round(1234.56)).toBe(1234.56);
        });
        it("should preserve the original value if precision is not a number", function() {
            expect(Ext.util.Format.round(1234.56, "invalid precision")).toBe(1234.56);
        });
        it("should round the value to the specified precision", function() {
            expect(Ext.util.Format.round(1234.50, 1)).toBe(1234.5);
            expect(Ext.util.Format.round(1234.54, 1)).toBe(1234.5);
            expect(Ext.util.Format.round(1234.55, 1)).toBe(1234.6);
            expect(Ext.util.Format.round(1234.59, 1)).toBe(1234.6);
        });
    });

    describe("numberRenderer", function() {
        it("should return a function that formats a number with the specified format", function() {
            expect(Ext.util.Format.numberRenderer("0.00")(123.321)).toBe("123.32");
        });
    });

    describe("plural", function() {
        it("should return the singular form if count == 1", function() {
            expect(Ext.util.Format.plural(1, "car")).toBe("1 car");
            expect(Ext.util.Format.plural(1, "child", "children")).toBe("1 child");
        });
        it("should return the plural as singular+s by default if count <> 1", function() {
            expect(Ext.util.Format.plural(0, "car")).toBe("0 cars");
            expect(Ext.util.Format.plural(2, "car")).toBe("2 cars");
        });
        it("should return the specified plural if count <> 1", function() {
            expect(Ext.util.Format.plural(0, "child", "children")).toBe("0 children");
            expect(Ext.util.Format.plural(2, "child", "children")).toBe("2 children");
        });
    });

    describe("nl2br", function() {
        it("should convert newline characters to <br/>", function() {
            expect(Ext.util.Format.nl2br("first line\nsecond line")).toBe("first line<br/>second line");
        });
        it("should be null/undefined safe", function() {
            expect(Ext.util.Format.nl2br(undefined)).toBe("");
            expect(Ext.util.Format.nl2br(null)).toBe("");
        });
    });

    describe("capitalize", function() {
        it("should be alias to Ext.String.capitalize", function() {
            expect(Ext.util.Format.capitalize).toBe(Ext.String.capitalize);
        });
    });

    describe("ellipsis", function() {
        it("should be alias to Ext.String.ellipsis", function() {
            expect(Ext.util.Format.ellipsis).toBe(Ext.String.ellipsis);
        });
    });

    describe("format", function() {
        it("should be alias to Ext.String.format", function() {
            expect(Ext.util.Format.format).toBe(Ext.String.format);
        });
    });

    describe("htmlDecode", function() {
        it("should be alias to Ext.String.htmlDecode", function() {
            expect(Ext.util.Format.htmlDecode).toBe(Ext.String.htmlDecode);
        });
    });

    describe("htmlEncode", function() {
        it("should be alias to Ext.String.htmlEncode", function() {
            expect(Ext.util.Format.htmlEncode).toBe(Ext.String.htmlEncode);
        });
    });

    describe("leftPad", function() {
        it("should be alias to Ext.String.leftPad", function() {
            expect(Ext.util.Format.leftPad).toBe(Ext.String.leftPad);
        });
    });

    describe("trim", function() {
        it("should be alias to Ext.String.trim", function() {
            expect(Ext.util.Format.trim).toBe(Ext.String.trim);
        });
    });

    describe("parseBox", function() {
        it("should return a box when 4 margins are specified", function() {
            expect(Ext.util.Format.parseBox("1 2 3 4")).toEqual({
                top: 1,
                right: 2,
                bottom: 3,
                left: 4
        });
      });
      it("should return a box when 3 margins are specified", function() {
          expect(Ext.util.Format.parseBox("1 2 3")).toEqual({
              top: 1,
              right: 2,
              bottom: 3,
              left: 2
          });
      });
      it("should return a box when 2 margins are specified", function() {
          expect(Ext.util.Format.parseBox("1 2")).toEqual({
              top: 1,
              right: 2,
              bottom: 1,
              left: 2
          });
      });
      it("should return a box when 1 margin is specified", function() {
          expect(Ext.util.Format.parseBox("1")).toEqual({
              top: 1,
              right: 1,
              bottom: 1,
              left: 1
          });
      });
      it("should return a box when 1 margin is specified as number", function() {
          expect(Ext.util.Format.parseBox(1)).toEqual({
              top: 1,
              right: 1,
              bottom: 1,
              left: 1
          });
      });
      it("should return a 0 margin box when no margin is specified", function() {
          var zeroMarginBox = {
             top: 0,
             right: 0,
             bottom: 0,
             left: 0
          };
          expect(Ext.util.Format.parseBox(undefined)).toEqual(zeroMarginBox);
          expect(Ext.util.Format.parseBox(null)).toEqual(zeroMarginBox);
          expect(Ext.util.Format.parseBox("")).toEqual(zeroMarginBox);
          expect(Ext.util.Format.parseBox("    ")).toEqual(zeroMarginBox);
      });
    });

    describe("escapeRegex", function() {
        it("should escape regular expressions", function() {
            expect(Ext.util.Format.escapeRegex("-.*+?^${}()|[]/\\abc0123")).toBe("\\-\\.\\*\\+\\?\\^\\$\\{\\}\\(\\)\\|\\[\\]\\/\\\\abc0123");
        });
    });

});
