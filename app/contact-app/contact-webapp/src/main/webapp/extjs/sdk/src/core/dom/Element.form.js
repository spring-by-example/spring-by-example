/**
 * @class Ext.dom.Element
 */
Ext.apply(Ext.core.Element, {
    /**
     * Serializes a DOM form into a url encoded string
     * @param {Object} form The form
     * @return {String} The url encoded form
     * @static
     */
    serializeForm: function(form) {
        var fElements = form.elements || (document.forms[form] || Ext.getDom(form)).elements,
            hasSubmit = false,
            encoder   = encodeURIComponent,
            data      = '',
            e,
            eLen      = fElements.length,
            el, name, type, options, hasValue,
            o, oLen, opt;

        for (e = 0; e < eLen; e++) {
            el      = fElements[e];
            name    = el.name;
            type    = el.type;
            options = el.options;

            if (!el.disabled && name) {
                if (/select-(one|multiple)/i.test(type)) {
                    oLen = options.length;

                    for (o = 0; o < oLen; o++) {
                        opt = options[o];

                        if (opt.selected) {
                            hasValue = opt.hasAttribute ? opt.hasAttribute('value') : opt.getAttributeNode('value').specified;
                            data += Ext.String.format("{0}={1}&", encoder(name), encoder(hasValue ? opt.value : opt.text));
                        }
                    }
                } else if (!(/file|undefined|reset|button/i.test(type))) {
                    if (!(/radio|checkbox/i.test(type) && !el.checked) && !(type == 'submit' && hasSubmit)) {
                        data += encoder(name) + '=' + encoder(el.value) + '&';
                        hasSubmit = /submit/i.test(type);
                    }
                }
            }
        }

        return data.substr(0, data.length - 1);
    }
});
