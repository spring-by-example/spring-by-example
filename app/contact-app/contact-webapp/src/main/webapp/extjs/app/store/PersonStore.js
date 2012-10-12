Ext.define('contact.store.PersonStore', {
    extend: 'Ext.data.Store',

    model: 'contact.model.PersonModel',

    grouper: {
        groupFn: function(record) {
            return record.get('lastName');
        }
    },

    proxy: {
        type: 'ajax',
        url: '../api/person',
        headers: {
            'Content-Type': 'application/json-type',
            'Accept': 'application/json-type'
        },
        reader: {
            root: 'results',
            totalProperty: 'count'
        }
    }
});
