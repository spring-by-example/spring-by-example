Ext.define('contact.view.Viewport', {
    extend: 'Ext.container.Viewport',
    layout: 'card',

    initComponent: function() {
        this.callParent(arguments);
    },

    /**
     * Opens the specified page.
     */
    open: function(pageName) {
        var previousPage = this.layout.getActiveItem();
        if (previousPage) {
            previousPage.destroy();
        }

        var page = Ext.widget(pageName);

        this.layout.setActiveItem(page);
        page.fireEvent('updatePage');

        return this.layout.getActiveItem();
    }
});
