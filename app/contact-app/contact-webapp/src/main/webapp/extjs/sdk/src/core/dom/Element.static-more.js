/**
 * @class Ext.dom.Element
 */
(function(){
    var doc = document,
        isCSS1 = doc.compatMode == "CSS1Compat",
        ELEMENT = Ext.Element,
        fly = function(el){
            if (!_fly) {
                _fly = new Ext.Element.Flyweight();
            }
            _fly.dom = el;
            return _fly;
        }, _fly;

    Ext.apply(ELEMENT, {

        getViewWidth : function(full) {
            return full ? ELEMENT.getDocumentWidth() : ELEMENT.getViewportWidth();
        },

        getViewHeight : function(full) {
            return full ? ELEMENT.getDocumentHeight() : ELEMENT.getViewportHeight();
        },

        getDocumentHeight: function() {
            return Math.max(!isCSS1 ? doc.body.scrollHeight : doc.documentElement.scrollHeight, ELEMENT.getViewportHeight());
        },

        getDocumentWidth: function() {
            return Math.max(!isCSS1 ? doc.body.scrollWidth : doc.documentElement.scrollWidth, ELEMENT.getViewportWidth());
        },

        getViewportHeight: function(){
            return Ext.isIE ?
                   (Ext.isStrict ? doc.documentElement.clientHeight : doc.body.clientHeight) :
                   self.innerHeight;
        },

        getViewportWidth : function() {
            return (!Ext.isStrict && !Ext.isOpera) ? doc.body.clientWidth :
                   Ext.isIE ? doc.documentElement.clientWidth : self.innerWidth;
        },

        getY : function(el) {
            return ELEMENT.getXY(el)[1];
        },

        getX : function(el) {
            return ELEMENT.getXY(el)[0];
        },

        getOffsetParent: function (el) {
            el = Ext.getDom(el);
            try {
                // accessing offsetParent can throw "Unspecified Error" in IE6-8 (not 9)
                return el.offsetParent;
            } catch (e) {
                var body = document.body; // safe bet, unless...
                return (el == body) ? null : body;
            }
        },

        getXY : function(el) {
            var p,
                pe,
                b,
                bt,
                bl,
                dbd,
                x = 0,
                y = 0,
                scroll,
                hasAbsolute,
                bd = (doc.body || doc.documentElement),
                ret;

            el = Ext.getDom(el);

            if(el != bd){
                hasAbsolute = fly(el).isStyle("position", "absolute");

                if (el.getBoundingClientRect) {
                    try {
                        b = el.getBoundingClientRect();
                        scroll = fly(document).getScroll();
                        ret = [ Math.round(b.left + scroll.left), Math.round(b.top + scroll.top) ];
                    } catch (e) {
                        // IE6-8 can also throw from getBoundingClientRect...
                    }
                }

                if (!ret) {
                    for (p = el; p; p = ELEMENT.getOffsetParent(p)) {
                        pe = fly(p);
                        x += p.offsetLeft;
                        y += p.offsetTop;

                        hasAbsolute = hasAbsolute || pe.isStyle("position", "absolute");

                        if (Ext.isGecko) {
                            y += bt = parseInt(pe.getStyle("borderTopWidth"), 10) || 0;
                            x += bl = parseInt(pe.getStyle("borderLeftWidth"), 10) || 0;

                            if (p != el && !pe.isStyle('overflow','visible')) {
                                x += bl;
                                y += bt;
                            }
                        }
                    }

                    if (Ext.isSafari && hasAbsolute) {
                        x -= bd.offsetLeft;
                        y -= bd.offsetTop;
                    }

                    if (Ext.isGecko && !hasAbsolute) {
                        dbd = fly(bd);
                        x += parseInt(dbd.getStyle("borderLeftWidth"), 10) || 0;
                        y += parseInt(dbd.getStyle("borderTopWidth"), 10) || 0;
                    }

                    p = el.parentNode;
                    while (p && p != bd) {
                        if (!Ext.isOpera || (p.tagName.toUpperCase() != 'TR' && !fly(p).isStyle("display", "inline"))) {
                            x -= p.scrollLeft;
                            y -= p.scrollTop;
                        }
                        p = p.parentNode;
                    }
                    ret = [x,y];
                }
            }
            return ret || [0,0];
        },

        setXY : function(el, xy) {
            (el = Ext.fly(el, '_setXY')).position();

            var pts = el.translatePoints(xy),
                style = el.dom.style,
                pos;

            for (pos in pts) {
                if (!isNaN(pts[pos])) {
                    style[pos] = pts[pos] + "px";
                }
            }
        },

        setX : function(el, x) {
            ELEMENT.setXY(el, [x, false]);
        },

        setY : function(el, y) {
            ELEMENT.setXY(el, [false, y]);
        }
    });
}());
