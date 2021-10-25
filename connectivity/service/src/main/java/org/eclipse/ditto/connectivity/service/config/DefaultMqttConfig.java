/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.connectivity.service.config;

import java.time.Duration;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.internal.utils.config.ConfigWithFallback;
import org.eclipse.ditto.internal.utils.config.ScopedConfig;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * This class is the default implementation of {@link MqttConfig}.
 */
@Immutable
final class DefaultMqttConfig implements MqttConfig {

    private static final String CONFIG_PATH = "mqtt";
    private static final String RECONNECT_PATH = "reconnect";

    private final int maxQueueSize;
    private final int eventLoopThreads;
    private final boolean cleanSession;
    private final boolean reconnectForRedelivery;
    private final Duration reconnectForRedeliveryDelay;
    private final boolean useSeparateClientForPublisher;
    private final Duration reconnectMinTimeoutForMqttBrokerInitiatedDisconnect;
    private final BackOffConfig reconnectBackOffConfig;

    private DefaultMqttConfig(final ScopedConfig config) {
        eventLoopThreads = config.getNonNegativeIntOrThrow(MqttConfigValue.EVENT_LOOP_THREADS);
        cleanSession = config.getBoolean(MqttConfigValue.CLEAN_SESSION.getConfigPath());
        reconnectForRedelivery = config.getBoolean(MqttConfigValue.RECONNECT_FOR_REDELIVERY.getConfigPath());
        reconnectForRedeliveryDelay =
                config.getNonNegativeDurationOrThrow(MqttConfigValue.RECONNECT_FOR_REDELIVERY_DELAY);
        useSeparateClientForPublisher = config.getBoolean(MqttConfigValue.SEPARATE_PUBLISHER_CLIENT.getConfigPath());
        maxQueueSize = config.getInt(MqttConfigValue.MAX_QUEUE_SIZE.getConfigPath());
        reconnectMinTimeoutForMqttBrokerInitiatedDisconnect = config.getNonNegativeDurationOrThrow(
                MqttConfigValue.RECONNECT_MIN_TIMEOUT_FOR_MQTT_BROKER_INITIATED_DISCONNECT);
        reconnectBackOffConfig = DefaultBackOffConfig.of(config.hasPath(RECONNECT_PATH)
                ? config.getConfig(RECONNECT_PATH)
                : ConfigFactory.parseString("backoff" + "={}"));
    }

    /**
     * Returns an instance of {@code DefaultMqttConfig} based on the settings of the specified Config.
     *
     * @param config is supposed to provide the settings of the JavaScript mapping config at {@value #CONFIG_PATH}.
     * @return the instance.
     * @throws org.eclipse.ditto.internal.utils.config.DittoConfigError if {@code config} is invalid.
     */
    public static DefaultMqttConfig of(final Config config) {
        return new DefaultMqttConfig(ConfigWithFallback.newInstance(config, CONFIG_PATH, MqttConfigValue.values()));
    }

    @Override
    public int getEventLoopThreads() {
        return eventLoopThreads;
    }

    @Override
    public boolean isCleanSession() {
        return cleanSession;
    }

    @Override
    public boolean shouldReconnectForRedelivery() {
        return reconnectForRedelivery;
    }

    @Override
    public Duration getReconnectForRedeliveryDelay() {
        return reconnectForRedeliveryDelay;
    }

    @Override
    public boolean shouldUseSeparatePublisherClient() {
        return useSeparateClientForPublisher;
    }

    @Override
    public Duration getReconnectMinTimeoutForMqttBrokerInitiatedDisconnect() {
        return reconnectMinTimeoutForMqttBrokerInitiatedDisconnect;
    }

    @Override
    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    @Override
    public BackOffConfig getReconnectBackOffConfig() {
        return reconnectBackOffConfig;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DefaultMqttConfig that = (DefaultMqttConfig) o;
        return Objects.equals(eventLoopThreads, that.eventLoopThreads) &&
                Objects.equals(cleanSession, that.cleanSession) &&
                Objects.equals(reconnectForRedelivery, that.reconnectForRedelivery) &&
                Objects.equals(reconnectForRedeliveryDelay, that.reconnectForRedeliveryDelay) &&
                Objects.equals(useSeparateClientForPublisher, that.useSeparateClientForPublisher) &&
                Objects.equals(reconnectMinTimeoutForMqttBrokerInitiatedDisconnect,
                        that.reconnectMinTimeoutForMqttBrokerInitiatedDisconnect) &&
                Objects.equals(maxQueueSize, that.maxQueueSize) &&
                Objects.equals(reconnectBackOffConfig, that.reconnectBackOffConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventLoopThreads, cleanSession, reconnectForRedelivery,
                reconnectForRedeliveryDelay, useSeparateClientForPublisher,
                reconnectMinTimeoutForMqttBrokerInitiatedDisconnect, maxQueueSize, reconnectBackOffConfig);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" +
                "eventLoopThreads=" + eventLoopThreads +
                ", cleanSession=" + cleanSession +
                ", reconnectForRedelivery=" + reconnectForRedelivery +
                ", reconnectForRedeliveryDelay=" + reconnectForRedeliveryDelay +
                ", useSeparateClientForPublisher=" + useSeparateClientForPublisher +
                ", reconnectMinTimeoutForMqttBrokerInitiatedDisconnect=" +
                reconnectMinTimeoutForMqttBrokerInitiatedDisconnect +
                ", maxQueueSize=" + maxQueueSize +
                ", reconnectBackOffConfig=" + reconnectBackOffConfig +
                "]";
    }

}
