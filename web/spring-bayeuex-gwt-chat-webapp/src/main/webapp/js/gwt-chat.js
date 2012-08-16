dojo.require("dojox.cometd");
dojo.require("dojox.cometd.timestamp");

var room = {

    userName: null,
    lastUserName: "",
    connected: false,
    meta: null,
    displayCallback: null,
    addUserCallback: null,
    clearUserTableCallback: null,
	
	join: function(userName, displayCallback, addUserCallback, clearUserTableCallback) {
    	if (userName == null || userName.length == 0) {
			alert('Please enter a username!');
			return;
		}

	    var hostName = window.location.hostname;
	    var port = window.location.port;
	    var contextPath = "\/chat";
	    
	    var host = "http:\/\/" + hostName;
	    
	    if (port) {
	    	host += ":" + port;
	    }
	    
	    host += contextPath;
	    
	    dojox.cometd.init(host + "\/cometd\/chat\/");

		room.connected = true;		
		room.userName = userName;
		room.displayCallback = displayCallback;
		room.addUserCallback = addUserCallback;
		room.clearUserTableCallback = clearUserTableCallback;
		
		// subscribe and join
		dojox.cometd.startBatch();
		dojox.cometd.subscribe("/chat/demo", room, "processMessageEvent");
		dojox.cometd.publish("/chat/demo", {
			user: room.userName,
			join: true,
			chat: room.userName + " has joined"
		});
		dojox.cometd.endBatch();
		
		// handle cometd failures while in the room
		if (room.meta) {
			dojo.unsubscribe(room.meta, null, null);
		}

		room.meta = dojo.subscribe("/cometd/meta", this, function(e) {
			// console.debug(e);
			if (e.action == "handshake") {
				if (e.reestablish) {
					if (e.successful) {
						dojox.cometd.subscribe("/chat/demo", room, "processMessageEvent");
						dojox.cometd.publish("/chat/demo", {
							user: room.userName,
							join: true,
							chat: room.userName + " has re-joined"
						});
					}

					room.processMessageEvent({
						data: {
							join: true,
							user: "SERVER",
							chat: "handshake " + e.successful ? "Handshake OK" : "Failed"
						}
					});
				}
			} else { 
				if (e.action == "connect") {
					if (e.successful && !room.connected) {
						room.processMessageEvent({
							data: {
								join: true,
								user: "SERVER",
								chat: "reconnected!"
							}
						});
					}

					if (!e.successful && room.connected) {
						room.processMessageEvent({
							data: {
								leave: true,
								user: "SERVER",
								chat: "disconnected!"
							}
						});
					}
					room.connected = e.successful;
				}
			}
		});
	},
	
	leave: function(){
		if (!room.userName) {
			return;
		}
		
		if (room.meta) {
			dojo.unsubscribe(room.meta);
		}
		room.meta = null;
		
		dojox.cometd.startBatch();
		dojox.cometd.unsubscribe("/chat/demo", room, "processMessageEvent");
		dojox.cometd.publish("/chat/demo", {
			user: room.userName,
			leave: true,
			chat: room.userName + " has left"
		});
		dojox.cometd.endBatch();
		
		room.userName = null;
		dojox.cometd.disconnect();
	},
	
	chat: function(text) {
		if (!text || !text.length) {
			return false;
		}

		dojox.cometd.publish("/chat/demo", {
			user: room.userName,
			chat: text
		});
	},
	
	processMessageEvent: function(message) {
		if (!message.data) {
			// console.debug("bad message format " + message);
			return;
		}
		
		if (message.data instanceof Array) {
			room.clearUserTableCallback();
			
			for (var i in message.data) {
				room.addUserCallback(message.data[i]);
			}
		} else {
			var from = message.data.user;
			var special = message.data.join || message.data.leave;
			var text = message.data.chat;

			if (!text) {
				return;
			}
			
			if (!special && from == room.lastUserName) {
				from = "...";
			}
			else {
				room.lastUserName = from;
				from += ":";
			}
			
			if (special) {
				room.lastUserName = "";
			}

			room.displayCallback(from, text);
		}
	}
	
};

dojo.addOnUnload(room, "leave");
