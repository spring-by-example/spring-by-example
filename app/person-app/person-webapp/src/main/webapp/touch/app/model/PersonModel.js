Ext.define('person.model.PersonModel', {
    extend: 'Ext.data.Model',
    statics: {
        CLASS_NAME: 'org.springbyexample.schema.beans.person.Person'
    },
    idProperty: 'id',

    config: {
        fields: [
            { name: '__class', type: 'string' },
            { name: 'id',               type: 'int' },
            { name: 'firstName',        type: 'string' },
            { name: 'lastName',         type: 'string' },
            
            { name: 'lockVersion',      type: 'int' },
            { name: 'createUser',       type: 'string' },
            { name: 'created',          type: 'int' },
            { name: 'lastUpdateUser',   type: 'string' },
            { name: 'lastUpdated',      type: 'int' }
        ],
        validations: [
            { type: 'presence', field: 'firstName', message: 'Please enter a first name for this contact.' },
            { type: 'presence', field: 'lastName', message: 'Please enter a last name for this contact.' }
        ]
    },
    constructor: function() {
        this.callParent(arguments);
        this.set('__class', person.model.PersonModel.CLASS_NAME);
    },
    reset: function(data, id) {
        this.callParent([data, id]);
        
        this.set('lockVersion', 0);
        this.set('createUser', '');
        this.set('created', null);
        this.set('lastUpdateUser', '');
        this.set('lastUpdated', null);
    }

});