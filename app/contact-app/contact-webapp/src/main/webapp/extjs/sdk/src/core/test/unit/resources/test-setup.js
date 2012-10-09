Test.SandBoxImpl.prototype.addIframe = function(config) {
    var sandBox = this,
    me = new Test.SandBoxImpl();
    
    me.domReady(function() {
        me.reporter = sandBox.reporter;
        me.createIframe(config);
    });
    
    return me;
};

////////////////////////////////////////////////////////////////////////////////
var lib = {dom:{}};

////////////////////////////////////////////////////////////////////////////////
var test = {dom:{}};

////////////////////////////////////////////////////////////////////////////////
test.helpers = [{ type: "js", src: "../../../../testreporter/deploy/testreporter/jasmine.js" }];
test.helpers.push({ type: "js", src: "resources/APITest.js" });
test.helpers.push({ type: "js", src: "resources/BlockLoader.js" });
test.helpers.push({ type: "js", src: "resources/TestHelper.js" });

////////////////////////////////////////////////////////////////////////////////
lib.foundation = jsbToTestLib("../../",
    {
        "id": "foundation",
        "files": [
            { "path": "../../platform/core/src/", "name": "Ext.js" },
            { "path": "../../platform/core/src/version/", "name": "Version.js" },
            { "path": "../../platform/core/src/lang/", "name": "String.js" },
            { "path": "../../platform/core/src/lang/", "name": "Number.js" },
            { "path": "../../platform/core/src/lang/", "name": "Array.js" },
            { "path": "../../platform/core/src/lang/", "name": "Function.js" },
            { "path": "../../platform/core/src/lang/", "name": "Object.js" },
            { "path": "../../platform/core/src/lang/", "name": "Date.js" },
            { "path": "../../platform/core/src/class/", "name": "Base.js" },
            { "path": "../../platform/core/src/class/", "name": "Class.js" },
            { "path": "../../platform/core/src/class/", "name": "ClassManager.js" },
            { "path": "../../platform/core/src/class/", "name": "Loader.js" },
            { "path": "../../platform/core/src/lang/", "name": "Error.js" }
        ]
    }
);

////////////////////////////////////////////////////////////////////////////////
lib.extras = jsbToTestLib("../../",
    {
        "id": "extras",
        "files": [
            { "path": "../../platform/core/src/misc/", "name": "JSON.js" },
            { "path": "../../platform/core/src/", "name": "Ext-more.js" },
            { "path": "../../platform/core/src/util/", "name": "Format.js" },
            { "path": "../../platform/core/src/util/", "name": "TaskManager.js" },
            { "path": "../../platform/core/src/", "name": "Support.js" }
        ]
    }
);

////////////////////////////////////////////////////////////////////////////////
lib.dom.extjs = [{type:'js', src:'../../../../platform/deploy/sencha-extjs-dom.js'}];
lib.dom.touch = [{type:'js', src:'../../../../platform/deploy/sencha-touch-dom.js'}];
lib.dom.platform = jsbToTestLib("../../",
    {
        "id": "dom",
        "files": [
            { "path": "../../platform/core/src/dom/", "name": "DomHelper.js" },
            { "path": "../../platform/core/src/dom/", "name": "DomQuery.js" },

            { "path": "../../platform/core/src/dom/", "name": "Element.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.traversal.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.insertion.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.style.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.fx.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.anim.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.static.js" },
            { "path": "../../platform/core/src/dom/", "name": "CompositeElementLite.js" },

            { "path": "../../platform/core/src/util/", "name": "DelayedTask.js" },
            { "path": "../../platform/core/src/util/", "name": "Event.js" },
            { "path": "../../platform/core/src/", "name": "EventManager.js" },
            { "path": "../../platform/core/src/", "name": "EventObject.js" },

            { "path": "../../platform/core/src/dom/", "name": "Element-more.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.alignment.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.position.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.scroll.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.fx-more.js" },
            { "path": "../../platform/core/src/dom/", "name": "Element.keys.js" },
            { "path": "../../platform/core/src/dom/", "name": "CompositeElementLite-more.js" },
            { "path": "../../platform/core/src/dom/", "name": "CompositeElement.js" }
        ]
    }
);

////////////////////////////////////////////////////////////////////////////////
test.dom.shared = jsbToTestLib("",
    {
        "files": [
            { "path": "spec/dom/", "name": "sanity.js" },
            { "path": "spec/dom/", "name": "DomHelper.js" },
            // { "path": "spec/dom/", "name": "DomQuery.js" },
            { "path": "spec/dom/", "name": "Element.js" },
            { "path": "spec/dom/", "name": "Element.traversal.js" },
            { "path": "spec/dom/", "name": "Element.insertion.js" },
            { "path": "spec/dom/", "name": "Element.style.js" },
            // { "path": "spec/dom/", "name": "Element.fx.js" },
            // { "path": "spec/dom/", "name": "Element.anim.js" },
            { "path": "spec/dom/", "name": "Element.static.js" }
            // { "path": "spec/dom/", "name": "CompositeElementLite.js" },

            // { "path": "spec/dom/", "name": "Element.static-more.js" },
            // { "path": "spec/dom/", "name": "Element-more.js" },
            // { "path": "spec/dom/", "name": "Element.alignment.js" },
            // { "path": "spec/dom/", "name": "Element.position.js" },
            // { "path": "spec/dom/", "name": "Element.scroll.js" },
            // { "path": "spec/dom/", "name": "Element.fx-more.js" },
            // { "path": "spec/dom/", "name": "Element.keys.js" },
            // { "path": "spec/dom/", "name": "CompositeElementLite-more.js" },
            // { "path": "spec/dom/", "name": "CompositeElement.js" }
        ]
    }
);

////////////////////////////////////////////////////////////////////////////////
test.dom.platform = [].concat(test.dom.shared);
test.dom.platform.push({type:'js', src:'spec/dom/platform-api.js'});

////////////////////////////////////////////////////////////////////////////////
test.dom.extjs = [].concat(test.dom.shared);
test.dom.extjs.push({type:'js', src:'spec/dom/extjs-api.js'});

////////////////////////////////////////////////////////////////////////////////
test.dom.touch = [].concat(test.dom.shared);
test.dom.touch.push({type:'js', src:'spec/dom/touch-api.js'});

