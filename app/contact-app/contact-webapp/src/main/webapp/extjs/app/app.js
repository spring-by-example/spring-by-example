Ext.Loader.setConfig({
    enabled: true,
    disableCaching: false
});

Ext.application({
    name: 'contact',

    autoCreateViewport: true,

    controllers: [ 'person.PersonController' ]
});
