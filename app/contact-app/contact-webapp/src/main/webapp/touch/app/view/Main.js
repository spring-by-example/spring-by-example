Ext.define('person.view.Main', {
    extend: 'Ext.Container',
    alias: "widget.mainView",

    requires: [
        'person.view.PersonEditor',
        'person.view.PersonList'
    ],

    config: {
        fullscreen: true
    }

});