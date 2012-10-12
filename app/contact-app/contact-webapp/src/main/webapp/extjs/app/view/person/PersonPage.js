Ext.define('contact.view.person.PersonPage', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.personPage',

    /**
     * The person model object.
     */
    person: null,

    initComponent: function() {
        this.dockedItems = [ {
            xtype: 'toolbar',
            dock: 'top',
            items: [ {
                xtype: 'component',
                flex: 1
            }, {
                xtype: 'button',
                text: 'Delete',
                action: 'deletePerson'
            }, {
                xtype: 'button',
                text: 'Save',
                action: 'savePerson'
            }, {
                xtype: 'button',
                text: 'Cancel',
                action: 'cancelPerson'
            } ]
        } ];

        this.items = [ {
            xtype: 'form',
            defaultType: 'textfield',
            bodyPadding: 20,
            margin: 20,
            items: [ {
                fieldLabel: 'First Name',
                name: 'firstName'
            }, {
                fieldLabel: 'Last Name',
                name: 'lastName',
                margins: {
                    top: 20
                }
            } ]
        } ];

        this.callParent(arguments);
    },

    updatePage: function(person) {
        var me = this;
        me.person = person;

        if (person.get('id')) {
            me.setTitle('Edit Contact');
            me.down('button[action=deletePerson]').show();
        } else {
            me.setTitle('Create Contact');
            me.down('button[action=deletePerson]').hide();
        }

        me.down('form').loadRecord(person);
    },

    submitPerson: function() {
        var me = this;

        var values = me.down('form').getValues();
        me.person.set(values);
        return me.person;
    }
});
