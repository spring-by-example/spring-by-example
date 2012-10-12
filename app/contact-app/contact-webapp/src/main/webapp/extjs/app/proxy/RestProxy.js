/**
 * A base proxy that supports parameterized URL and JSON reader.
 * 
 * @author cainwang
 */
Ext.define('contact.proxy.RestProxy', {
    extend: 'Ext.data.proxy.Ajax',
    alias: 'proxy.xrest',

    headers: {
        'Content-Type': 'application/json-type',
        'Accept': 'application/json-type'
    },

    /**
     * @property {Object} actionMethods Mapping of action name to HTTP request
     *           method. In the basic AjaxProxy these are set to 'GET' for
     *           'read' actions and 'POST' for 'create', 'update' and 'destroy'
     *           actions. The {@link Ext.data.proxy.Rest} maps these to the
     *           correct RESTful methods.
     */
    actionMethods: {
        create: 'POST',
        read: 'GET',
        update: 'POST',
        destroy: 'DELETE'
    },

    /**
     * The default parameters to replace the tokens in template URL. This is
     * mostly used for in-Store proxy to modifer the URL.
     */
    restUrlParams: null,

    /**
     * JSON attribute name that points to the root node of the model.
     */
    root: '',

    /**
     * The HTTP method for this proxy.
     * 
     * If method is not specified, it'll be derived from the action.
     */
    method: null,

    /**
     * JSON attribute name that points to the total count of records.
     */
    totalProperty: '',

    /**
     * A flag to indicate whether to get all records.
     * 
     */
    getAllRecords: false,

    /**
     * @private
     * 
     * Whether the proxy has displayed the error already or not.
     */
    errorDisplayed: false,

    errorMessage: '',

    constructor: function(config) {
        this.timeout = 3 * 60 * 1000;

        config.reader = Ext.create('Ext.data.reader.Json', {
            root: config.root,
            totalProperty: config.totalProperty
        });

        config.writer = Ext.create('contact.data.JsonWriter');
        config.writer.writeAllFields = true;

        this.callParent([ config ]);

        // We have to handle timeouts here because the response callback does
        // not state
        // if the error was caused by a timeout or not.
        this.on('exception', function(proxy, response, operation) {
            this.errorDisplayed = true;

            if (Ext.isFunction(operation.errorCallback)) {
                operation.errorCallback(this.errorMessage);
            }
        });
    },

    /**
     * Applies the default REST url parameters into the template URL.
     */
    buildUrl: function(request) {
        var restUrlParams = request.operation.restUrlParams || this.restUrlParams;

        var formattedUrl = this.formatUrl(restUrlParams);
        return formattedUrl;
    },

    /**
     * Sends a POST Ajax request with a create action.
     * 
     * @cfg {Object} config:
     * 
     * @attr {object} record The model object to create remotely.
     * @attr {array} records The array of model objects to create remotely.
     * @attr {array} restUrlParams An array of values to replace the tokens in
     *       the URL template.
     * @attr {Function} success The callback function if the request succeeds.
     * @attr {Function} error The optional callback function if the request
     *       fails.
     */
    doCreate: function(config) {
        config = config || {};
        config.action = 'create';

        return this.sendRequeset(config);
    },

    /**
     * Sends a GET Ajax request with a read action.
     * 
     * @cfg {Object} config:
     * 
     * @attr {object} record The model object to create remotely.
     * @attr {array} records The array of model objects to create remotely.
     * @attr {array} restUrlParams An array of values to replace the tokens in
     *       the URL template.
     * @attr {Function} success The callback function if the request succeeds.
     * @attr {Function} error The optional callback function if the request
     *       fails.
     */
    doRead: function(config) {
        config = config || {};
        config.action = 'read';

        return this.sendRequeset(config);
    },

    /**
     * Sends a POST Ajax request with an update action.
     * 
     * @cfg {Object} config:
     * 
     * @attr {object} record The model object to create remotely.
     * @attr {array} records The array of model objects to create remotely.
     * @attr {array} restUrlParams An array of values to replace the tokens in
     *       the URL template.
     * @attr {Function} success The callback function if the request succeeds.
     * @attr {Function} error The optional callback function if the request
     *       fails.
     */
    doUpdate: function(config) {
        config = config || {};
        config.action = 'update';

        return this.sendRequeset(config);
    },

    /**
     * Sends a POST Ajax request with a delete action.
     * 
     * @cfg {Object} config:
     * 
     * @attr {object} record The model object to create remotely.
     * @attr {array} records The array of model objects to create remotely.
     * @attr {array} restUrlParams An array of values to replace the tokens in
     *       the URL template.
     * @attr {Function} success The callback function if the request succeeds.
     * @attr {Function} error The optional callback function if the request
     *       fails.
     */
    doDestroy: function(config) {
        config = config || {};
        config.action = 'destroy';

        return this.sendRequeset(config);
    },

    /**
     * Sends the Ajax request.
     * 
     * @private
     */
    sendRequeset: function(config) {
        config = config || {};
        var operation = new Ext.data.Operation({
            action: config.action,
            url: this.url,
            successCallback: config.success,
            errorCallback: config.error,
            restUrlParams: config.restUrlParams
        });

        if (config.record) {
            operation.records = [ config.record ];
        } else if (Ext.isArray(config.records)) {
            operation.records = config.records;
        }

        return this.doRequest(operation, this.responseCallback);
    },

    /**
     * Processes the request reponse and calls successCallback function if the
     * request is successful, otherwise calls the errorCallback function.
     */
    responseCallback: function(operation) {
        if (!operation) {
            return;
        }

        if (operation.success) {
            if (Ext.isFunction(operation.successCallback)) {
                var records = operation.resultSet.records;
                if (this.getAllRecords) {
                    operation.successCallback(records);
                } else {
                    operation.successCallback(records[0]);
                }
            }
        } else {
            // In this case, let the exception error handler do messaging,
            // just call the application logic to let it know there is an error.
            if (Ext.isFunction(operation.errorCallback)) {
                operation.errorCallback(this.errorMessage);
            }
        }
    },

    /**
     * @private
     * 
     * Applies the parameters to the URL.
     */
    formatUrl: function(restUrlParams) {
        if (this.url && Ext.isArray(restUrlParams)) {
            var params = [ this.url ];
            params = params.concat(restUrlParams);

            var formattedUrl = Ext.String.format.apply(this, params);
            return formattedUrl;
        }

        return this.url;
    },

    /**
     * Returns the HTTP method name for a given request. By default this returns
     * based on a lookup on {@link #actionMethods}.
     * 
     * @param {Ext.data.Request}
     *            request The request object
     * @return {String} The HTTP method to use (should be one of 'GET', 'POST',
     *         'PUT' or 'DELETE')
     */
    getMethod: function(request) {
        if (this.method) {
            return this.method;
        }

        return this.callParent(arguments);
    },

    /*
     * Determines if there are application level errors that this proxy should
     * know about.
     */
    getApplicationLevelErrors: function(operation) {
        if (!operation.response) {
            return null;
        }

        var rawResponse = operation.response.responseText, responseObject = Ext.JSON.decode(rawResponse);

        if (responseObject.errors) {
            return responseObject.messageList;
        } else {
            return null;
        }
    }
});
