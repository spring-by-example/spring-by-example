describe("API", function() {

    var actual = window;
    var expected = {Ext:{core:{Element: function(){} }}},
    Ext = expected.Ext;

    /* Element.static-more.js */ Ext.core.Element.getActiveElement = function getActiveElement(){};
    /* Element.static-more.js */ Ext.core.Element.serializeForm = function serializeForm(){};

    APITest({Ext:expected.Ext}, {Ext:actual.Ext});

});
