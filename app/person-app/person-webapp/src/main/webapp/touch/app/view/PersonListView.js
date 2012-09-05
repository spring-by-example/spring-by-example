Ext.define('app.view.PersonListView', {
    extend: 'Ext.List',

    config: {
        store: Ext.create('app.store.PersonStore'),
        limit: 5,
        disableSelection: true,

        plugins: [
            { xclass: 'Ext.plugin.ListPaging' },
            { xclass: 'Ext.plugin.PullRefresh' }
        ],

        emptyText: '<p class="no-searches">No results found matching the search</p>',

        itemTpl: Ext.create('Ext.XTemplate',
    		'<br/><br/>',
            '<h2>ID: {id}</h2>',
            '<div class="tweet">',
            '<p>ID: {id} <br/><br/>First Name: {firstName} <br/><br/>Last Name: {lastName}</p>',
            '</div>')
    }
});