Ext.define('contact.model.PersonFindResponseModel', {
    extend: 'Ext.data.Model',
    statics: {
        CLASS_NAME: 'org.springbyexample.schema.beans.person.PersonFindResponse'
    },

    config: {
        fields: [
            { name: 'results', type: 'auto', defaultValue: [] /* app.model.PersonModel */ }
        ]
    },
    constructor: function() {
        this.callParent(arguments);
        this.set('__class', person.model.PersonFindResponseModel.CLASS_NAME);
    }
    
});
