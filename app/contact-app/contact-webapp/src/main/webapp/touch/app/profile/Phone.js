Ext.define('person.profile.Phone', {
    extend: 'Ext.app.Profile',

    config: {
        name: 'phone',
        namespace: 'phone',
        controllers: ['PersonController']
    },

    isActive: function() {
        return Ext.os.is.Phone;
    },

    launch: function() {
        console.log("Activating phone profile.");
//        Ext.create('TouchStyle.view.phone.Main');
        

        var personListView = {
            xtype : "personListView"
        };
        var personEditorView = {
            xtype : "personEditorView"
        };

        Ext.Viewport.add([ personListView, personEditorView ]);
    }
    
});
