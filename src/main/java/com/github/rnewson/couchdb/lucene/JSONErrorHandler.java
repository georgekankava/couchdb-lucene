/*
 * Copyright Robert Newson
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

package com.github.rnewson.couchdb.lucene;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.rnewson.couchdb.lucene.util.ServletUtils;

/**
 * Convert errors to CouchDB-style JSON objects.
 *
 * @author rnewson
 */
public final class JSONErrorHandler extends ErrorHandler {

    private final Logger logger = Logger.getLogger(JSONErrorHandler.class.getName());

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        final String reason = baseRequest.getResponse().getReason();
        try {
            if (reason != null && reason.startsWith("{")) {
                ServletUtils.sendJsonError(request, response, baseRequest.getResponse().getStatus(),
                        new JSONObject(reason));
            } else {
                ServletUtils.sendJsonError(request, response, baseRequest.getResponse().getStatus(),
                        reason);
            }
        } catch (final JSONException e) {
            logger.warn("Exception during JSON processing", e);
            response.sendError(500);
        }

    }

}
