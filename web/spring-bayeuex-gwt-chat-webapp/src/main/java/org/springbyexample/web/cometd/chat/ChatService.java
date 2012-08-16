/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springbyexample.web.cometd.chat;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.cometd.Bayeux;
import org.cometd.Client;
import org.cometd.RemoveListener;
import org.mortbay.cometd.BayeuxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Bayeux Chat Service</p>
 * 
 * <p>Copied from <code>dojox.cometd.demo.BayeuxStartupListener.ChatService</code>.
 * 
 * @author David Winterfeldt
 * @author Dojo Cometd Authors
 */
@Component
public class ChatService extends BayeuxService {
    
    final Logger logger = LoggerFactory.getLogger(ChatService.class);
    
    final ConcurrentMap<String, Set<String>> _members = new ConcurrentHashMap<String, Set<String>>();

    /**
     * Constructor
     */
    @Autowired
    public ChatService(Bayeux bayeux) {
        super(bayeux, "chat");
        subscribe("/chat/**", "trackMembers");
    }

    /**
     * Tracks chat clients.
     */
    public void trackMembers(Client joiner, String channel,
                             Map<String, Object> data, String id) {
        if (Boolean.TRUE.equals(data.get("join"))) {
            Set<String> m = _members.get(channel);

            if (m == null) {
                Set<String> new_list = new CopyOnWriteArraySet<String>();
                m = _members.putIfAbsent(channel, new_list);
                if (m == null) {
                    m = new_list;
                }
            }

            final Set<String> members = m;
            final String username = (String) data.get("user");

            members.add(username);

            joiner.addListener(new RemoveListener() {
                public void removed(String clientId, boolean timeout) {
                    members.remove(username);

                    logger.info("members: " + members);
                }
            });
            
            logger.info("Members: " + members);
            
            send(joiner, channel, members, id);
        }
    }
    
}