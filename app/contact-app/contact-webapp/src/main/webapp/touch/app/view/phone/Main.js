Ext.define('person.view.phone.Main', {
    extend: 'person.view.Main',
    xtype: 'mainview',
    
    config: {
//        fullscreen: true,
//        layout: 'fit',
        autoDestroy: false,

        navigationBar: {
            docked: 'top',
                
            items: [
//{ xtype: 'spacer' },
//{
//  xtype: "button",
//  text: 'New',
//  ui: 'action',
//  itemId: "newButton"
//}
                {
                    xtype: 'button',
                    id: 'newButton',
                    text: 'New',
                    align: 'right',
//                    hidden: true,
                    hideAnimation: Ext.os.is.Android ? false : {
                        type: 'fadeOut',
                        duration: 200
                    },
                    showAnimation: Ext.os.is.Android ? false : {
                        type: 'fadeIn',
                        duration: 200
                    }
                },
                {
                    xtype: 'button',
                    id: 'editButton',
                    text: 'Edit',
                    align: 'right',
                    hidden: true,
                    hideAnimation: Ext.os.is.Android ? false : {
                        type: 'fadeOut',
                        duration: 200
                    },
                    showAnimation: Ext.os.is.Android ? false : {
                        type: 'fadeIn',
                        duration: 200
                    }
                },
                {
                    xtype: 'button',
                    id: 'saveButton',
                    text: 'Save',
                    ui: 'sencha',
                    align: 'right',
//                    hidden: true,
                    hideAnimation: Ext.os.is.Android ? false : {
                        type: 'fadeOut',
                        duration: 200
                    },
                    showAnimation: Ext.os.is.Android ? false : {
                        type: 'fadeIn',
                        duration: 200
                    }
                }
            ]
        },
        
        items: [
            {
                layout: 'hbox',
//                docked: 'left',
//                width : 250,
                items: [
                    {
                        xtype: 'personListView'
                    }, 
                    {
                        xtype: 'personEditorView'    
                    }
                    
                ]
            }
//            ,
//            {
//                xtype: 'personEditorView'
//            }
        ]
    }

});