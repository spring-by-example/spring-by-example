describe("Ext.Class", function() {
    var emptyFn = function(){},
        cls;

    beforeEach(function() {
        window.My = {
            awesome: {
                Class: function(){},
                Class1: function(){},
                Class2: function(){}
            },
            cool: {
                AnotherClass: function(){},
                AnotherClass1: function(){},
                AnotherClass2: function(){}
            }
        };
    });

    afterEach(function() {
        if (window.My) {
            window.My = undefined;
        }

        try {
            delete window.My;
        } catch (e) {}
    });

    // START PREPROCESSORS =================================================================== /
    describe("preprocessors", function() {

        beforeEach(function() {
            cls = Ext.Class.create(null, {});
        });

        describe("extend", function() {

            it("should extend from Base if no 'extend' property found", function() {
                var data = {};

                Ext.Class.preprocessors.extend.fn(cls, data, emptyFn, {}, emptyFn);

                expect((new cls) instanceof Ext.Base).toBeTruthy();
            });

            it("should extend from given parent class", function() {
                var data = {
                    extend: My.awesome.Class
                };

                Ext.Class.preprocessors.extend.fn(cls, data, emptyFn, {}, emptyFn);

                expect((new cls) instanceof My.awesome.Class).toBeTruthy();
            });

            it("should have superclass reference", function() {
                var data = {
                    extend: My.awesome.Class
                };

                var parentPrototype = My.awesome.Class.prototype;

                Ext.Class.preprocessors.extend.fn(cls, data, emptyFn, {}, emptyFn);

                expect(cls.superclass).toEqual(parentPrototype);
                expect((new cls).superclass).toEqual(parentPrototype);
            });
        });

        describe("other preprocessors", function() {
            beforeEach(function() {
                Ext.Class.preprocessors.extend.fn(cls, {}, emptyFn, {}, emptyFn);
            });

            describe("config", function() {

                it("should create getter if not exists", function() {
                    var data = {
                        config: {
                            someName: 'someValue'
                        }
                    };

                    Ext.Class.preprocessors.config.fn(cls, data, emptyFn, {}, emptyFn);

                    expect(data.getSomeName).toBeDefined();
                });

                it("should NOT create getter if already exists", function() {
                    var data = {
                        config: {
                            someName: 'someValue'
                        }
                    };

                    var called = false;
                    cls.prototype.getSomeName = function() {
                        called = true;
                    };

                    Ext.Class.preprocessors.config.fn(cls, data, emptyFn, {}, emptyFn);

                    expect(data.getSomeName).not.toBeDefined();
                });

                it("should create setter if not exists", function() {
                    var data = {
                        config: {
                            someName: 'someValue'
                        }
                    };

                    Ext.Class.preprocessors.config.fn(cls, data, emptyFn, {}, emptyFn);

                    expect(data.setSomeName).toBeDefined();
                });

                it("should NOT create setter if already exists", function() {
                    var data = {
                        config: {
                            someName: 'someValue'
                        }
                    };

                    var called = false;

                    cls.prototype.setSomeName = function() {
                        called = true;
                    };

                    Ext.Class.preprocessors.config.fn(cls, data, emptyFn, {}, emptyFn);

                    expect(data.setSomeName).not.toBeDefined();
                });
            });

            describe("statics", function() {
                it("should copy static properties to the class", function() {
                    var data = {
                        statics: {
                            someName: 'someValue',
                            someMethod: Ext.emptyFn
                        }
                    };

                    Ext.Class.preprocessors.statics.fn(cls, data, emptyFn, {}, emptyFn);

                    var obj = new cls;

                    expect(data.statics).not.toBeDefined();
                    expect(cls.someName).toBe('someValue');
                    expect(cls.someMethod).toBe(Ext.emptyFn);
                });
            });

            describe("inheritableStatics", function() {

                it("should store names of inheritable static properties", function() {
                    var data = {
                        inheritableStatics: {
                            someName: 'someValue',
                            someMethod: Ext.emptyFn
                        }
                    };

                    Ext.Class.preprocessors.inheritableStatics.fn(cls, data, emptyFn, {}, emptyFn);

                    var obj = new cls;

                    expect(obj.inheritableStatics).not.toBeDefined();
                    expect(cls.someName).toBe('someValue');
                    expect(cls.prototype.$inheritableStatics).toEqual(['someName', 'someMethod']);
                    expect(cls.someMethod).toBe(Ext.emptyFn);
                });

                it("should inherit inheritable statics", function() {
                    var data = {
                        inheritableStatics: {
                            someName: 'someValue',
                            someMethod: Ext.emptyFn
                        }
                    }, cls2 = Ext.Class.create(null, {});

                    Ext.Class.preprocessors.inheritableStatics.fn(cls, data, emptyFn, {}, emptyFn);
                    Ext.Class.preprocessors.extend.fn(cls2, { extend: cls }, emptyFn, {}, emptyFn);

                    expect(cls2.someName).toEqual('someValue');
                    expect(cls2.someMethod).toBe(Ext.emptyFn);
                });

                it("should NOT inherit inheritable statics if the class already has it", function() {
                    var data = {
                        inheritableStatics: {
                            someName: 'someValue',
                            someMethod: Ext.emptyFn
                        }
                    }, cls2 = Ext.Class.create(null, {});

                    cls2.someName = 'someOtherValue';
                    cls2.someMethod = function(){};

                    Ext.Class.preprocessors.inheritableStatics.fn(cls, data, emptyFn, {}, emptyFn);
                    Ext.Class.preprocessors.extend.fn(cls2, { extend: cls }, emptyFn, {}, emptyFn);

                    expect(cls2.someName).toEqual('someOtherValue');
                    expect(cls2.someMethod).not.toBe(Ext.emptyFn);
                });
            });
        });
    });

    // END PREPROCESSORS =================================================================== /

    describe("Instantiation", function() {
        var subClass, parentClass, mixinClass1, mixinClass2;

        beforeEach(function() {
            mixinClass1 = new Ext.Class({
                config: {
                    mixinConfig: 'mixinConfig'
                },

                constructor: function(config) {
                    this.initConfig(config);

                    this.mixinConstructor1Called = true;
                },

                mixinProperty1: 'mixinProperty1',

                mixinMethod1: function() {
                    this.mixinMethodCalled = true;
                }
            });

            mixinClass2 = new Ext.Class({
                constructor: function(config) {
                    this.initConfig(config);

                    this.mixinConstructor2Called = true;
                },

                mixinProperty2: 'mixinProperty2',

                mixinMethod2: function() {
                    this.mixinMethodCalled = true;
                }
            });

            parentClass = new Ext.Class({
                mixins: {
                    mixin1: mixinClass1
                },
                config: {
                    name: 'parentClass',
                    isCool: false,
                    members: {
                        abe: 'Abraham Elias',
                        ed: 'Ed Spencer'
                    },
                    hobbies: ['football', 'bowling']
                },
                constructor: function(config) {
                    this.initConfig(config);

                    this.parentConstructorCalled = true;

                    this.mixins.mixin1.constructor.apply(this, arguments);
                },

                parentProperty: 'parentProperty',

                parentMethod: function() {
                    this.parentMethodCalled = true;
                }
            });

            subClass = new Ext.Class({
                extend: parentClass,
                mixins: {
                    mixin1: mixinClass1,
                    mixin2: mixinClass2
                },
                config: {
                    name: 'subClass',
                    isCool: true,
                    members: {
                        jacky: 'Jacky Nguyen',
                        tommy: 'Tommy Maintz'
                    },
                    hobbies: ['sleeping', 'eating', 'movies'],
                    isSpecial: true
                },
                constructor: function(config) {
                    this.initConfig(config);

                    this.subConstrutorCalled = true;

                    subClass.superclass.constructor.apply(this, arguments);

                    this.mixins.mixin2.constructor.apply(this, arguments);
                },
                myOwnMethod: function() {
                    this.myOwnMethodCalled = true;
                }
            });
        });

        describe("addStatics", function() {
            it("single with name - value arguments", function() {
                var called = false;

                subClass.addStatics({
                    staticMethod: function(){
                        called = true;
                    }
                });

                expect(subClass.staticMethod).toBeDefined();
                subClass.staticMethod();

                expect(called).toBeTruthy();
            });

            it("multiple with object map argument", function() {
                subClass.addStatics({
                    staticProperty: 'something',
                    staticMethod: function(){}
                });

                expect(subClass.staticProperty).toEqual('something');
                expect(subClass.staticMethod).toBeDefined();
            });
        });


        describe("override", function() {
            it("should override", function() {
                subClass.override({
                    myOwnMethod: function(){
                        this.isOverridden = true;

                        this.callOverridden(arguments);
                    }
                });

                var obj = new subClass;
                obj.myOwnMethod();

                expect(obj.isOverridden).toBe(true);
                expect(obj.myOwnMethodCalled).toBe(true);
            });
        });

        describe("define override", function() {
            var obj,
                createFnsCalled;

            beforeEach(function () {
                createFnsCalled = [];
                function onCreated () {
                    createFnsCalled.push(this.$className);
                }

                Ext.define('Foo.UnusedOverride', {
                    override: 'Foo.Nothing',

                    foo: function (x) {
                        return this.callParent([x*2]);
                    }
                }, onCreated);

                // this override comes before its target:
                Ext.define('Foo.SingletonOverride', {
                    override: 'Foo.Singleton',

                    foo: function (x) {
                        return this.callParent([x*2]);
                    }
                }, onCreated);

                Ext.define('Foo.Singleton', {
                    singleton: true,
                    foo: function (x) {
                        return x;
                    }
                });

                Ext.define('Foo.SomeClass', {
                    prop: 1,

                    constructor: function () {
                        this.prop = 2;
                    },

                    method1: function(x) {
                        return 'b' + x;
                    },

                    statics: {
                        staticMethod: function (x) {
                            return 'B' + x;
                        }
                    }
                });

                // this override comes after its target:
                Ext.define('Foo.SomeClassOverride', {
                    override: 'Foo.SomeClass',

                    constructor: function () {
                        this.callParent(arguments);
                        this.prop *= 21;
                    },

                    method1: function(x) {
                        return 'a' + this.callParent([x*2]) + 'c';
                    },

                    method2: function() {
                        return 'two';
                    },

                    statics: {
                        newStatic: function () {
                            return 'boo';
                        },
                        staticMethod: function (x) {
                            return 'A' + this.callParent([x*2]) + 'C';
                        }
                    }
                }, onCreated);

                obj = Ext.create('Foo.SomeClass');
            });

            afterEach(function () {
                var classes = Ext.ClassManager.classes,
                    alternateToName = Ext.ClassManager.maps.alternateToName;
                try {
                    delete Ext.global.Foo;
                } catch (e) {
                    Ext.global.Foo = null;
                }
                obj = null;

                Ext.each(['Foo.SingletonOverride', 'Foo.Singleton', 'Foo.SomeClassOverride', 'Foo.SomeClass'],
                    function (className) {
                        try {
                            delete classes[className];
                            delete alternateToName[className];
                        } catch(e) {
                            classes[className] = null;
                            alternateToName[className] = null;
                        }
                    });
            });

            it("should call the createdFn", function () {
                expect(createFnsCalled.length).toEqual(2);
                expect(createFnsCalled[0]).toEqual('Foo.Singleton');
                expect(createFnsCalled[1]).toEqual('Foo.SomeClass');
            });

            it("can override constructor", function() {
                expect(obj.prop).toEqual(42);
            });

            it("can add new methods", function() {
                expect(obj.method2()).toEqual('two');
            });

            it("can add new static methods", function() {
                expect(Foo.SomeClass.newStatic()).toEqual('boo');
            });

            it("callParent should work for instance methods", function() {
                expect(obj.method1(21)).toEqual('ab42c');
            });

            it("callParent should work for static methods", function() {
                expect(Foo.SomeClass.staticMethod(21)).toEqual('AB42C');
            });

            it('works with singletons', function () {
                expect(Foo.Singleton.foo(21)).toEqual(42);
            });
        });

        describe("mixin", function() {
            it("should have all properties of mixins", function() {
                var obj = new subClass;
                expect(obj.mixinProperty1).toEqual('mixinProperty1');
                expect(obj.mixinProperty2).toEqual('mixinProperty2');
                expect(obj.mixinMethod1).toBeDefined();
                expect(obj.mixinMethod2).toBeDefined();
                expect(obj.config.mixinConfig).toEqual('mixinConfig');
            });
        });

        describe("config", function() {
            it("should merge properly", function() {
                var obj = new subClass;
                expect(obj.config).toEqual({
                    mixinConfig: 'mixinConfig',
                    name: 'subClass',
                    isCool: true,
                    members: {
                        abe: 'Abraham Elias',
                        ed: 'Ed Spencer',
                        jacky: 'Jacky Nguyen',
                        tommy: 'Tommy Maintz'
                    },
                    hobbies: ['sleeping', 'eating', 'movies'],
                    isSpecial: true
                });
            });

            it("should apply default config", function() {
                var obj = new subClass;
                expect(obj.getName()).toEqual('subClass');
                expect(obj.getIsCool()).toEqual(true);
                expect(obj.getHobbies()).toEqual(['sleeping', 'eating', 'movies']);
            });

            it("should apply with supplied config", function() {
                var obj = new subClass({
                    name: 'newName',
                    isCool: false,
                    members: {
                        aaron: 'Aaron Conran'
                    }
                });

                expect(obj.getName()).toEqual('newName');
                expect(obj.getIsCool()).toEqual(false);
                expect(obj.getMembers().aaron).toEqual('Aaron Conran');
            });

            it("should not share the same config", function() {
                var obj1 = new subClass({
                    name: 'newName',
                    isCool: false,
                    members: {
                        aaron: 'Aaron Conran'
                    }
                });

                var obj2 = new subClass();

                expect(obj2.getName()).not.toEqual('newName');
            });
        });

        describe("overriden methods", function() {
            it("should call self constructor", function() {
                var obj = new subClass;
                expect(obj.subConstrutorCalled).toBeTruthy();
            });

            it("should call parent constructor", function() {
                var obj = new subClass;
                expect(obj.parentConstructorCalled).toBeTruthy();
            });

            it("should call mixins constructors", function() {
                var obj = new subClass;
                expect(obj.mixinConstructor1Called).toBeTruthy();
                expect(obj.mixinConstructor2Called).toBeTruthy();
            });
        });

    });

});
