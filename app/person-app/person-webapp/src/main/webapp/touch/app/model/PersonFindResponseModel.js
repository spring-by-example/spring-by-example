Ext.define('app.model.PersonFindResponseModel', {
    extend: 'Ext.data.Model',

//    config: {
//        fields: [
//            { name: 'results',                     type: 'auto'         , defaultValue: [] /* app.model.PersonModel */ }
//        ]
//    }
    
    config: {
        fields: [
            { name: 'results', type: 'auto', defaultValue: [] /* app.model.PersonModel */ }
        ],

        proxy: {
            type: 'ajax',
            url: 'http://localhost:8080/person/api/persons.json'
        }
    }
    
});
