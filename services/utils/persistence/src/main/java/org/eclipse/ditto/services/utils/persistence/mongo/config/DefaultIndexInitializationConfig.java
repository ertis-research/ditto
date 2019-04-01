/*
 * Copyright (c) 2017-2018 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.utils.persistence.mongo.config;

import java.io.Serializable;
import java.util.Objects;

import org.eclipse.ditto.services.utils.config.ConfigWithFallback;
import org.eclipse.ditto.services.utils.config.ScopedConfig;

import com.typesafe.config.Config;

public class DefaultIndexInitializationConfig implements IndexInitializationConfig, Serializable {

    private static final String CONFIG_PATH = "index-initialization";

    private static final long serialVersionUID = 8748011719306228350L;

    private final boolean indexInitializationEnabled;

    private DefaultIndexInitializationConfig(final ScopedConfig config) {
        indexInitializationEnabled = config.getBoolean(IndexInitializerConfigValue.ENABLED.getConfigPath());;
    }

    /**
     * Returns an instance of {@code IndexInitializationConfig} which tries to obtain its properties from the given Config.
     *
     * @param config the Config which contains nested MongoDB settings at path {@value #CONFIG_PATH}.
     * @return the instance.
     * @throws org.eclipse.ditto.services.utils.config.DittoConfigError if {@code config} is invalid.
     */
    public static DefaultIndexInitializationConfig of(final Config config) {
        return new DefaultIndexInitializationConfig(ConfigWithFallback.newInstance(config, CONFIG_PATH, IndexInitializerConfigValue.values()));
    }

    @Override
    public boolean isIndexInitializationConfigEnabled() {
        return indexInitializationEnabled;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DefaultIndexInitializationConfig that = (DefaultIndexInitializationConfig) o;
        return Objects.equals(indexInitializationEnabled, that.indexInitializationEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indexInitializationEnabled);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" +
                "indexInitializationEnabled=" + indexInitializationEnabled +
                "]";
    }
}

