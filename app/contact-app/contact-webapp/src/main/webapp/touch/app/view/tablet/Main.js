Ext.define('person.view.tablet.Main', {
    extend: 'person.view.Main',
    
    config: {
        fullscreen: true,
        layout: 'fit',

        items: [
            {
                layout: 'fit',
                docked: 'left',
                width : 300,
                items: [
                    {
                        xtype: 'personListView'
                    }
                ]
            }
            ,
            {
                xtype: 'personEditorView'
            }
        ]
    }

});