if (!(Ext.core && Ext.core.Element)){
    var describe = function(){}; // clobber Jasmine's describe to skip these tests
    throw new Error('Ext.core.Element is missing. You must run the DOM jsBuilder script before all these tests can run');
}
