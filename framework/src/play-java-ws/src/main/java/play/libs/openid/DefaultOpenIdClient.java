/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package play.libs.openid;

import play.core.Execution;
import play.libs.F;
import play.libs.Scala;
import play.mvc.Http;
import scala.runtime.AbstractFunction1;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class DefaultOpenIdClient implements OpenIdClient {

    private final play.api.libs.openid.OpenIdClient client;

    @Inject
    public DefaultOpenIdClient(play.api.libs.openid.OpenIdClient client) {
        this.client = client;
    }

    /**
     * Retrieve the URL where the user should be redirected to start the OpenID authentication process
     */
    @Override
    public F.Promise<String> redirectURL(String openID, String callbackURL) {
        return redirectURL(openID, callbackURL, null, null, null);
    }

    /**
     * Retrieve the URL where the user should be redirected to start the OpenID authentication process
     */
    @Override
    public F.Promise<String> redirectURL(String openID, String callbackURL, Map<String, String> axRequired) {
        return redirectURL(openID, callbackURL, axRequired, null, null);
    }

    /**
     * Retrieve the URL where the user should be redirected to start the OpenID authentication process
     */
    @Override
    public F.Promise<String> redirectURL(String openID,
                                         String callbackURL,
                                         Map<String, String> axRequired,
                                         Map<String, String> axOptional) {
        return redirectURL(openID, callbackURL, axRequired, axOptional, null);
    }

    /**
     * Retrieve the URL where the user should be redirected to start the OpenID authentication process
     */
    @Override
    public F.Promise<String> redirectURL(String openID,
                                         String callbackURL,
                                         Map<String, String> axRequired,
                                         Map<String, String> axOptional,
                                         String realm) {
        if (axRequired == null) axRequired = new HashMap<String, String>();
        if (axOptional == null) axOptional = new HashMap<String, String>();
        return F.Promise.wrap(client.redirectURL(openID,
                callbackURL,
                play.utils.Conversions.newSeq(
                  scala.collection.JavaConverters.mapAsScalaMapConverter(axRequired).asScala()),
                play.utils.Conversions.newSeq(
                  scala.collection.JavaConverters.mapAsScalaMapConverter(axOptional).asScala()),
                Scala.Option(realm)));
    }

    /**
     * Check the identity of the user from the current request, that should be the callback from the OpenID server
     */
    @Override
    public F.Promise<UserInfo> verifiedId(Http.RequestHeader request) {
        scala.concurrent.Future<UserInfo> scalaPromise = client.verifiedId(request.queryString()).map(
                new AbstractFunction1<play.api.libs.openid.UserInfo, UserInfo>() {
                    @Override
                    public UserInfo apply(play.api.libs.openid.UserInfo scalaUserInfo) {
                        return new UserInfo(scalaUserInfo.id(), scala.collection.JavaConverters.mapAsJavaMapConverter(scalaUserInfo.attributes()).asJava());
                    }
                }, Execution.internalContext());
        return F.Promise.wrap(scalaPromise);
    }

    /**
     * Check the identity of the user from the current request, that should be the callback from the OpenID server
     */
    @Override
    public F.Promise<UserInfo> verifiedId() {
        return verifiedId(Http.Context.current().request());
    }
}
