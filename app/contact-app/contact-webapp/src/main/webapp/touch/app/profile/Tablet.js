Ext.define('person.profile.Tablet', {
    extend: 'Ext.app.Profile',

    config: {
        name: 'tablet',
        namespace: 'tablet',
        controllers: [ 'PersonController' ],
        views: [ 'Main' ]
    },

    isActive: function() {
        return !Ext.os.is.Phone;
    },

    launch: function() {
        console.log("Activating tablet profile.");
        
        Ext.create('person.view.tablet.Main');
    }
    
});
