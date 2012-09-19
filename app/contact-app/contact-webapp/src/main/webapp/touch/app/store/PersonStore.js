Ext.define('person.store.PersonStore', {
    extend: 'Ext.data.Store',
    
    config: {
        model: 'person.model.PersonModel',

        grouper: {
            groupFn: function(record) {
                return record.get('lastName');
            }
        },
        
    	proxy: {
    		type: 'rest',
    		url: '../api/person',
    	    headers: {
                'Content-Type' : 'application/json-type',
                'Accept': 'application/json-type'
    	    },
    		reader: {
    		    rootProperty: 'results',
    		    totalProperty: 'count'
    		}
	    }
    }

});
