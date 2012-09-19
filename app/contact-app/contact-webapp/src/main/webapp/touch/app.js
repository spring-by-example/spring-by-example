Ext.application({
    name: "person",

    profiles: ['Tablet', 'Phone'],
    models: [ 'PersonFindResponseModel', 'PersonModel' ],
    stores: [ 'PersonStore' ],
//    controllers: [ 'PersonController' ],
    views: [ 'PersonList', 'PersonEditor' ],

    phoneStartupScreen: 'resources/loading/Default.png',
    tabletStartupScreen: 'resources/loading/Default~ipad.png',

    icon: {
        '57': 'resources/icons/Icon.png',
        '72': 'resources/icons/Icon~ipad.png',
        '114': 'resources/icons/Icon@2x.png',
        '144': 'resources/icons/Icon~ipad@2x.png'
    },

    isIconPrecomposed: true,

    startupImage: {
        '320x460': 'resources/startup/320x460.jpg',
        '640x920': 'resources/startup/640x920.png',
        '768x1004': 'resources/startup/768x1004.png',
        '748x1024': 'resources/startup/748x1024.png',
        '1536x2008': 'resources/startup/1536x2008.png',
        '1496x2048': 'resources/startup/1496x2048.png'
    },

    launch: function () {
        // remove startup loading indicator
        Ext.fly('appLoadingIndicator').destroy();

//        var personListView = {
//            xtype: "personListView"
//        };
//        var personEditorView = {
//            xtype: "personEditorView"
//        };
//
//        Ext.Viewport.add([personListView, personEditorView]);

    }
});