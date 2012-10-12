Ext.define('contact.controller.person.PersonController', {
    extend: 'Ext.app.Controller',

    models: [ 'PersonModel' ],

    stores: [ 'PersonStore' ],

    views: [ 'person.PeopleLandingPage', 'person.PersonCard', 'person.PersonPage' ],

    refs: [ {
        ref: 'viewport',
        selector: 'viewport'
    }, {
        ref: 'peopleLandingPage',
        selector: 'peopleLandingPage'
    }, {
        ref: 'personPage',
        selector: 'personPage'
    } ],

    init: function() {
        this.control({
            'viewport': {
                afterrender: this.showLandingPage
            },
            'peopleLandingPage': {
                updatePage: this.updatePage
            },
            'personCard': {
                click: this.editPerson
            },
            'button[action=createPerson]': {
                click: this.createPerson
            },
            'button[action=savePerson]': {
                click: this.savePerson
            },
            'button[action=cancelPerson]': {
                click: this.cancelPerson
            },
            'button[action=deletePerson]': {
                click: this.deletePerson
            }
        });
    },

    showLandingPage: function() {
        var me = this;
        var viewport = me.getViewport();
        viewport.open('peopleLandingPage');
    },

    updatePage: function() {
        var me = this;
        var page = me.getPeopleLandingPage();

        var personStore = Ext.getStore('PersonStore');
        personStore.load(function(records) {
            for ( var i = 0; i < records.length; i++) {
                var person = records[i];
                var card = Ext.widget('personCard', {
                    person: person
                });
                page.add(card);
            }
        });
    },

    editPerson: function(personCard) {
        var me = this;

        var person = personCard.person;
        var viewport = me.getViewport();
        var page = viewport.open('personPage');
        page.updatePage(person);
    },

    /**
     * Creates a new person.
     */
    createPerson: function() {
        var me = this;

        var person = Ext.create('contact.model.PersonModel');
        var viewport = me.getViewport();
        var page = viewport.open('personPage');
        page.updatePage(person);
    },

    savePerson: function() {
        var me = this;

        var page = me.getPersonPage();
        var person = page.submitPerson();
        var url, method;
        if (person.get('id')) {
            url = '../api/person/{0}';
            method = 'PUT';
        } else {
            url = '../api/person';
            method = 'POST';
        }

        var proxy = Ext.create('contact.proxy.RestProxy', {
            url: url,
            root: 'result',
            method: method,
            model: 'contact.model.PersonModel'
        });
        proxy.doUpdate({
            record: person,
            restParams: [ person.get('id') ],
            success: function() {
                var viewport = me.getViewport();
                viewport.open('peopleLandingPage');
            }
        });
    },

    cancelPerson: function() {
        var me = this;

        var viewport = me.getViewport();
        viewport.open('peopleLandingPage');
    },

    deletePerson: function() {
        var me = this;

        var page = me.getPersonPage();
        var person = page.submitPerson();

        var proxy = Ext.create('contact.proxy.RestProxy', {
            url: '../api/person/{0}',
            restUrlParams: [person.get('id')],
            method: 'delete',
            model: 'contact.model.PersonModel'
        });
        proxy.doDestroy({
            record: person,
            restParams: [ person.get('id') ],
            success: function() {
                var viewport = me.getViewport();
                viewport.open('peopleLandingPage');
            }
        });
    }
});
