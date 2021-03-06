/*
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reactivesocket.server;

import io.reactivesocket.ConnectionSetupPayload;
import io.reactivesocket.ReactiveSocket;
import io.reactivesocket.events.EventSource;
import io.reactivesocket.events.ServerEventListener;
import io.reactivesocket.exceptions.SetupException;
import io.reactivesocket.lease.LeaseEnforcingSocket;
import io.reactivesocket.transport.TransportServer;
import io.reactivesocket.transport.TransportServer.StartedServer;

public interface ReactiveSocketServer extends EventSource<ServerEventListener> {

    /**
     * Starts this server.
     *
     * @param acceptor Socket acceptor to use.
     *
     * @return Handle to get information about the started server.
     */
    StartedServer start(SocketAcceptor acceptor);

    static ReactiveSocketServer create(TransportServer transportServer) {
        return new DefaultReactiveSocketServer(transportServer);
    }

    /**
     * {@code ReactiveSocket} is a full duplex protocol where a client and server are identical in terms of both having
     * the capability to initiate requests to their peer. This interface provides the contract where a server accepts
     * a new {@code ReactiveSocket} for sending requests to the peer and returns a new {@code ReactiveSocket} that will
     * be used to accept requests from it's peer.
     */
    interface SocketAcceptor {

        /**
         * Accepts a new {@code ReactiveSocket} used to send requests to the peer and returns another
         * {@code ReactiveSocket} that is used for accepting requests from the peer.
         *
         * @param setup Setup as sent by the client.
         * @param sendingSocket Socket used to send requests to the peer.
         *
         * @return Socket to accept requests from the peer.
         *
         * @throws SetupException If the acceptor needs to reject the setup of this socket.
         */
        LeaseEnforcingSocket accept(ConnectionSetupPayload setup, ReactiveSocket sendingSocket);
    }
}
