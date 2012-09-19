Ext.define("person.controller.PersonController", {
    extend: "Ext.app.Controller",
    requires: "Ext.MessageBox",

    config: {
        refs: {
            personListView: "personListView",
            personEditorView: "personEditorView",
            personList: "#personList"
        },
        control: {
            personListView: {
                newPersonCommand: "onNewPersonCommand",
                editPersonCommand: "onEditPersonCommand"
            },
            personEditorView: {
                savePersonCommand: "onSavePersonCommand",
                deletePersonCommand: "onDeletePersonCommand",
                backToHomeCommand: "onBackToHomeCommand"
            }

        }
    },
    // Transitions
    slideLeftTransition: { type: 'slide', direction: 'left' },
    slideRightTransition: { type: 'slide', direction: 'right' },

    activatePersonEditor: function (record) {
        var personEditorView = this.getPersonEditorView();
        personEditorView.setRecord(record); // load() is deprecated.
//        Ext.Viewport.animateActiveItem(personEditorView, this.slideLeftTransition);
    },
    activatePersonList: function () {
//        Ext.Viewport.animateActiveItem(this.getPersonListView(), this.slideRightTransition);
    },

    // Commands.
    onNewPersonCommand: function () {
        console.log("onNewPersonCommand");
        
        var newPerson = Ext.create("person.model.PersonModel", {
            firstName: '',
            lastName: ''
        });

        this.activatePersonEditor(newPerson);
    },
    onEditPersonCommand: function (list, record) {
        console.log("onEditPersonCommand");

        this.activatePersonEditor(record);
    },
    onSavePersonCommand: function () {
        console.log("onSavePersonCommand");

        var personEditorView = this.getPersonEditorView();

        var person = personEditorView.getRecord();
        var newValues = personEditorView.getValues();

        person.set('firstName', newValues.firstName);
        person.set('lastName', newValues.lastName);

        var errors = person.validate();

        if (!errors.isValid()) {
            var msg = '';
            
            if (errors.getByField("firstName")[0]) {
                msg = errors.getByField("firstName")[0].getMessage();
            }
            if (errors.getByField("lastName")[0]) {
                if (msg.length > 0) {
                    msg += '\n';
                }
                
                msg += errors.getByField("lastName")[0].getMessage();
            }
                
            Ext.Msg.alert('Wait!', msg, Ext.emptyFn);

            person.reject();
            return;
        }

        var personStore = Ext.getStore("PersonStore");

        if (personStore.findRecord('id', person.data.id) == null) {
            console.log("Saving record.  id=" + person.data.id);
            
            person.setId(0);
            personStore.add(person);
        } else {
            console.log("Updating record.  id=" + person.data.id);
            
            var record = personStore.findRecord('id', person.data.id);

            record.firstName = newValues.firstName;
            record.lastName = newValues.lastName;
        }

        personStore.sync();
        
        this.activatePersonList();
    },
    onDeletePersonCommand: function () {
        console.log("onDeletePersonCommand");

        var personEditorView = this.getPersonEditorView();
        var person = personEditorView.getRecord();
        var personStore = Ext.getStore("PersonStore");

        personStore.remove(person);
        personStore.sync();
        
        this.activatePersonList();
    },
    onBackToHomeCommand: function () {
        console.log("onBackToHomeCommand");
        
        this.activatePersonList();
    },

    launch: function () {
        this.callParent(arguments);
        var personStore = Ext.getStore("PersonStore");
        personStore.load();
        
        console.log("launch");
    },
    init: function () {
        this.callParent(arguments);
        console.log("init");
    }
});