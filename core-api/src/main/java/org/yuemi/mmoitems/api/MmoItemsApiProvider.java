package org.yuemi.mmoitems.api;

import org.jetbrains.annotations.NotNull;

/**
 * Entry point for accessing the MmoItemsPlugin API.
 *
 * Consumers should depend on this interface, not implementation details.
 */
public interface MmoItemsApiProvider {

    /**
     * Retrieves the currently registered MmoItemsApi implementation.
     *
     * @return active API instance
     */
    @NotNull
    MmoItemsApi getApi();
}
