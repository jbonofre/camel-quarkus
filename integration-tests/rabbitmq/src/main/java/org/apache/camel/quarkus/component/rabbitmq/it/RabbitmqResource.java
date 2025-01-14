/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.component.rabbitmq.it;

import java.net.URI;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;

@Path("/rabbitmq")
@ApplicationScoped
public class RabbitmqResource {
    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    ConsumerTemplate consumerTemplate;

    @Path("/{exchangeName}/{queueName}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String consumeRabbitmqMessage(@PathParam("exchangeName") String exchangeName,
            @PathParam("queueName") String queueName) {
        return consumerTemplate.receiveBody("rabbitmq:" + exchangeName + "?queue=" + queueName, 5000,
                String.class);
    }

    @Path("/{exchangeName}/{queueName}")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response produceRabbitmqMessage(@PathParam("exchangeName") String exchangeName,
            @PathParam("queueName") String queueName, String message) throws Exception {
        producerTemplate.sendBody("rabbitmq:" + exchangeName + "?queue=" + queueName, message);
        return Response.created(new URI("https://camel.apache.org/")).build();
    }
}
