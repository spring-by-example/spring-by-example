Ext.define('person.controller.phone.PersonController', {
    extend: 'person.controller.PersonController',

    activatePersonEditor: function (record) {
        Ext.Viewport.animateActiveItem(this.getPersonEditorView(), this.slideLeftTransition);
    },
    activatePersonList: function () {
        Ext.Viewport.animateActiveItem(this.getPersonListView(), this.slideRightTransition);
    }

});