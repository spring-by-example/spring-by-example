Ext.define("person.view.PersonList", {
    extend: "Ext.Container",
    requires:"Ext.dataview.List",
    alias: "widget.personListView",

    config: {
        layout: {
            type: 'fit'
        },

        items: [{
            xtype: "toolbar",
//            title: "Contacts",
            docked: "top"
//                ,
//            items: [
//                { xtype: 'spacer' },
//                {
//                    xtype: "button",
//                    text: 'New',
//                    ui: 'action',
//                    itemId: "newButton"
//                }
//            ]
        }, {
            xtype: "list",
            store: "PersonStore",
            itemId:"personList",
            ui: 'round',
            loadingText: "Loading Contacts...",
            emptyText: "<div class=\"persons-list-empty-text\">No contacts found.</div>",
            onItemDisclosure: true,
            grouped: true,
            itemTpl: "<div class=\"list-item-title\">{lastName}, {firstName}</div><div class=\"list-item-narrative\"></div>",
            
            plugins: [
                      { xclass: 'Ext.plugin.ListPaging' },
                      { xclass: 'Ext.plugin.PullRefresh' }
                  ]

        }],
        
        listeners: [{
            delegate: "#newButton",
            event: "tap",
            fn: "onNewButtonTap"
        }, {
            delegate: "#personList",
            event: "disclose",
            fn: "onPersonListDisclose"
        }]
    },  
    
    onNewButtonTap: function () {
        console.log("newPersonCommand");
        this.fireEvent("newPersonCommand", this);
    },
    onPersonListDisclose: function (list, record, target, index, evt, options) {
        console.log("editPersonCommand");
        this.fireEvent('editPersonCommand', this, record);
    }
});