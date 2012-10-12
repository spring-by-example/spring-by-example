Ext.define('contact.view.person.PeopleLandingPage', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.peopleLandingPage',

    title: 'Contacts',

    layout: 'auto',

    initComponent: function() {
        this.dockedItems = [ {
            xtype: 'toolbar',
            dock: 'top',
            items: [ {
                xtype: 'component',
                flex: 1
            }, {
                xtype: 'button',
                text: 'Create',
                action: 'createPerson'
            } ]
        } ];

        this.callParent(arguments);
    }
});
