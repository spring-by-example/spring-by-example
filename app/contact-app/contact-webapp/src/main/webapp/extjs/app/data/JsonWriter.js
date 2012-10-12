Ext.define('contact.data.JsonWriter', {
    extend: 'Ext.data.writer.Json',
    
    /**
     * Writes the raw data object instead of Model.data.
     * 
     * Formats the data for each record before sending it to the server. This method should be overridden to format the
     * data in a way that differs from the default.
     * @param {Object} record The record that we are writing to the server.
     * @return {Object} An object literal of name/value keys to be written to the server. By default this method returns
     * the data property on the record.
     */
    getRecordData: function(record) {
        return record.data;
    }
});