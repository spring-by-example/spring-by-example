// Mocking the document variable in the browser env.
function Doc() {
}
Doc.prototype.getElementById = function(id) {
    return null;
}
var document = new Doc();

// overide the default getPropertyValue with a version
// that is aware of a form object injected using Rhino
ValangValidator.Rule.prototype.getPropertyValue = function(propertyName) {
    var javaResult = formObject[propertyName]
    if (javaResult) {
        var result
        if (javaResult['getClass'] && javaResult.getClass() == new java.util.Date().getClass()) {
            // convert java Date to JavaScript Date
            result = eval('new Date(' + javaResult.getTime() + ')');
        } else {

            var toEval = javaResult.toString();

            // toEval is either a javascript string or a java.util.String. if its a java.lang.String then converting
            // it to a javascript string.
            if (typeof(toEval) != 'string') {
                toEval = new String(toEval);
            }
            toEval = '\'' + toEval.replace('\'', '\\\'') + '\'';
            result = eval(toEval);
        }
        return result
    } else {
        return null
    }
}
// there is no form in Rhino so just do nothing
ValangValidator.prototype._findForm = function(name) {
    return null
}
ValangValidator.prototype._giveRulesSameOrderAsFormFields = function(failedRules) {
    return failedRules
}

// enable verbose logging and save log messages in an array so we can check 
// them out in Java
ValangValidator.Logger.logFunctionCalls(ValangValidator.Rule.prototype)
//ValangValidator.Logger.logFunctionCalls(ValangValidator.prototype)
var logMessages = new Array()
ValangValidator.Logger.log = function(msg) {
    logMessages.push(this._indentString(' ') + msg);
}