Ext.application({
    name: 'Person',
    
    requires: [
        'Ext.MessageBox',
        'app.model.PersonFindResponseModel',
        'app.model.PersonModel',
        'app.store.PersonStore',
        'app.view.PersonListView'
    ],

    models: [ 'PersonModel','PersonFindResponseModel' ],
    stores: [ 'PersonStore' ],
    views: [ 'PersonListView' ],
//    views: [ 'Main','PersonListView', 'List' ],

    icon: {
        '57': 'resources/icons/Icon.png',
        '72': 'resources/icons/Icon~ipad.png',
        '114': 'resources/icons/Icon@2x.png',
        '144': 'resources/icons/Icon~ipad@2x.png'
    },

    isIconPrecomposed: true,

    startupImage: {
        '320x460': 'resources/startup/320x460.jpg',
        '640x920': 'resources/startup/640x920.png',
        '768x1004': 'resources/startup/768x1004.png',
        '748x1024': 'resources/startup/748x1024.png',
        '1536x2008': 'resources/startup/1536x2008.png',
        '1496x2048': 'resources/startup/1496x2048.png'
    },

    launch: function() {
        // Destroy the #appLoadingIndicator element
        Ext.fly('appLoadingIndicator').destroy();

        // Initialize the main view
//        Ext.Viewport.add(Ext.create('app.view.Main'));
        
        // comment out line above to work
//        Ext.create("Ext.tab.Panel", {
//            fullscreen: true,
//            tabBarPosition: 'bottom',
//
//            items: [
//                {
//                    title: 'Contact',
//                    iconCls: 'user',
//                    xtype: 'formpanel',
//                    url: 'contact.php',
//                    layout: 'vbox',
//
//                    items: [
//                        {
//                            xtype: 'fieldset',
//                            title: 'Contact Us',
//                            instructions: '(email address is optional)',
//                            items: [
//                                {
//                                    xtype: 'textfield',
//                                    label: 'Name'
//                                },
//                                {
//                                    xtype: 'emailfield',
//                                    label: 'Email'
//                                },
//                                {
//                                    xtype: 'textareafield',
//                                    label: 'Message'
//                                }
//                            ]
//                        },
//                        {
//                            xtype: 'button',
//                            text: 'Send',
//                            ui: 'confirm',
//                            handler: function() {
//                                this.up('formpanel').submit();
//                            }
//                        }
//                    ]
//                }
//            ]
//        });
        
    },

    onReady: function() {
//    	Ext.Viewport.add({
//			xclass : 'app.view.List',
//			width : 380,
//			height : 420,
//			centered : true,
//			modal : true,
//			hideOnMaskTap : false
//		}).show();

        if (Ext.os.is.Phone) {
            Ext.create('app.view.PersonListView', {
                fullscreen: true
            });
        } else {
            Ext.Viewport.add({
                xclass: 'app.view.PersonListView',
                width: 380,
                height: 420,
                centered: true,
                modal: true,
                hideOnMaskTap: false
            }).show();
        }    	
    },
    
    onUpdated: function() {
        Ext.Msg.confirm(
            "Application Update",
            "This application has just successfully been updated to the latest version. Reload now?",
            function(buttonId) {
                if (buttonId === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});
