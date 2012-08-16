dojo.require("dojox.cometd");
dojo.require("dojox.cometd.timestamp");

var monitor = {

    connected: false,
    meta: null,
    subscriptions: ["/monitor/servers"],
    displayCallback: null,
    tradeDisplayCallback: null,

	init: function() {	    
	    var hostName = window.location.hostname;
	    var port = window.location.port;
	    var contextPath = "\/monitor";
	    
	    var host = "http:\/\/" + hostName;
	    
	    if (port) {
	    	host += ":" + port;
	    }
	    
	    host += contextPath;
	    
	    dojox.cometd.init(host + "\/cometd\/monitor\/");

		monitor.connected = true;		

		// handle cometd failures while in the monitor
		if (monitor.meta) {
			dojo.unsubscribe(monitor.meta, null, null);
		}

		monitor.meta = dojo.subscribe("/cometd/meta", this, function(e) {
			// console.debug(e);
			if (e.action == "handshake") {
				if (e.reestablish) {
					if (e.successful) {
						for (var i = 0; i < monitor.subscriptions.length; i++) {
							dojox.cometd.subscribe(monitor.subscriptions[i], monitor, "processMessageEvent");
						}
					}
				}
			} else { 
				if (e.action == "connect") {
					if (e.successful && !monitor.connected) {
					}

					if (!e.successful && monitor.connected) {
					}
					monitor.connected = e.successful;
				}
			}
		});
	},

	subscribe: function(displayCallback, tradeDisplayCallback) {
		monitor.displayCallback = displayCallback;
		monitor.tradeDisplayCallback = tradeDisplayCallback;
		
		for (var i = 0; i < monitor.subscriptions.length; i++) {
			dojox.cometd.subscribe(monitor.subscriptions[i], monitor, "processMessageEvent");
		}
	},
	
	subscribeToChannel: function(channel) {
		var index = monitor.subscriptions.length;
		monitor.subscriptions[index] = channel;
			
		dojox.cometd.subscribe(channel, monitor, "processMessageEvent");
	},

	unsubscribeFromChannel: function(channel) {
		for (var i = 0; i < monitor.subscriptions.length; i++) {
			if (channel == monitor.subscriptions[i]) {
				// remove from subscriptions array
				monitor.subscriptions.splice(i, 1);
			}
		}
			
		dojox.cometd.unsubscribe(channel);
	},

	leave: function(){
		if (monitor.meta) {
			dojo.unsubscribe(monitor.meta);
		}
		monitor.meta = null;
		
		for (var i = 0; i < monitor.subscriptions.length; i++) {
			dojox.cometd.unsubscribe(monitor.subscriptions[i]);
		}

		dojox.cometd.disconnect();
	},
	
	processMessageEvent: function(message) {
		if (!message.data) {
			return;
		}

		if (message.channel == "/monitor/servers") {
			monitor.displayCallback(message.data);
		} else {
			monitor.tradeDisplayCallback(message.data);
		}		
	}
	
};

dojo.addOnLoad(monitor, "init");
dojo.addOnUnload(monitor, "leave");
