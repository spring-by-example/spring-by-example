describe("Ext.Base", function() {
    
    beforeEach(function() {
        Ext.ClassManager.enableNamespaceParseCache = false;
    });

    afterEach(function() {
        window.Test = undefined;

        try {
            delete window.Test;
        } catch (e) {}
        
        Ext.ClassManager.enableNamespaceParseCache = true;
    });

    describe("borrow", function() {
        beforeEach(function() {
            Ext.define("Test.Foo", {
                a: function() {
                    return 'foo a';
                },
                b: function() {
                    return 'foo b';
                },
                c: function() {
                    return 'foo c';
                }
            });
            Ext.define("Test.Bar", {
                a: function() {
                    return 'bar a';
                }
            });
        });
        
        it("should borrow methods", function() {
            Test.Bar.borrow(Test.Foo, ['b', 'c']);
            
            var bar = new Test.Bar();
            expect(bar.a()).toEqual('bar a');
            expect(bar.b()).toEqual('foo b');
            expect(bar.c()).toEqual('foo c');
        });
    });
    
});