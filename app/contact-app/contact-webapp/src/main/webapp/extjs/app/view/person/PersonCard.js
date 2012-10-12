Ext.define('contact.view.person.PersonCard', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.personCard',

    width: 360,
    height: 120,

    cls: 'card',

    /**
     * The person model object.
     */
    person: null,

    initComponent: function() {
        var me = this, person = me.person;
        me.title = person.get('firstName') + ' ' + person.get('lastName');

        me.layout = {
            type: 'hbox',
            padding: 5
        };

        me.items = [ {
            xtype: 'container',
            width: me.width / 3,
            layout: {
                type: 'vbox',
                padding: 5,
                defaultMargins: {
                    top: 2,
                    right: 5,
                    bottom: 10,
                    left: 5
                }
            },
            items: [ {
                xtype: 'label',
                text: 'First Name: ' + person.get('firstName')
            }, {
                xtype: 'label',
                text: 'Last Name: ' + person.get('lastName')
            } ]
        }, {
            xtype: 'container',
            layout: {
                type: 'vbox',
                padding: 5,
                defaultMargins: {
                    top: 2,
                    right: 5,
                    bottom: 2,
                    left: 5
                }
            },
            items: [ {
                xtype: 'label',
                text: 'Created by: ' + person.get('createUser')
            }, {
                xtype: 'label',
                text: 'Created at: ' + new Date(person.get('created')).toDateString()
            }, {
                xtype: 'label',
                text: 'Last updated by: ' + person.get('lastUpdateUser')
            }, {
                xtype: 'label',
                text: 'Last updated at: ' + new Date(person.get('lastUpdated')).toDateString()
            } ]
        } ];

        me.on('afterrender', me.setupClickEvent);

        me.callParent(arguments);
    },

    setupClickEvent: function() {
        var me = this;
        var el = me.getEl();
        el.on('click', function(event) {
            me.fireEvent('click', me, event);
        });
    }
});
