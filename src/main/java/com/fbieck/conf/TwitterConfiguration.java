package com.fbieck.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
public class TwitterConfiguration implements SocialConfigurer {

    @Value("${custom.twitter.consumerkey}")
    private String CONSUMERKEY;

    @Value("${custom.twitter.consumersecret}")
    private String CONSUMERSECRET;

    @Value("${custom.twitter.accesstoken}")
    private String ACCESSTOKEN;

    @Value("${custom.twitter.accesstokensecret}")
    private String ACCESSTOKENSECRET;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(new TwitterConnectionFactory(
                CONSUMERKEY,
                CONSUMERSECRET));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return null;
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

    @Bean
    public Twitter twitter(){
        return new TwitterTemplate(CONSUMERKEY, CONSUMERSECRET, ACCESSTOKEN, ACCESSTOKENSECRET);
    }
}
