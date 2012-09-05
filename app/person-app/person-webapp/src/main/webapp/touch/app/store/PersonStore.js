Ext.define('app.store.PersonStore', {
    extend: 'Ext.data.Store',
    
    config: {
//    	model: 'app.model.PersonFindResponseModel',
    	fields: [ 'firstName', 'lastName' ],
        autoLoad: true,
        
    	proxy: {
    		type: 'ajax',
    		url: '/person/api/persons.json',
//    	    headers: {
//    	        "Content-Type": "application/json"
//    	    },
    		reader: {
    		    type: 'json',
    		    rootProperty: 'results',
    		    totalProperty: 'count'
    		}
	    }
    }
})
